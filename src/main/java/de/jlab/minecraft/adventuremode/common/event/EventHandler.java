package de.jlab.minecraft.mods.adventuremode.event;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.network.PacketAM001EventStarted;
import de.jlab.minecraft.mods.adventuremode.network.PacketAM002EventStopped;
import de.jlab.minecraft.mods.adventuremode.network.PacketAM000EventUpdate;
import de.jlab.minecraft.mods.adventuremode.network.PacketAMEvent;
import de.jlab.minecraft.mods.adventuremode.utils.ChanceCalculator;

public class EventHandler implements IScheduledTickHandler {
	
	public static final String EVENTTYPE_ALL = "all";
	
	private EventHandlerConfigStore configstore;
	
	private int eventDelay = 0;
	private int generatorDelay = 0;
	private ArrayList<EventGenerator> generatorList = new ArrayList<EventGenerator>();
	private ChanceCalculator eventChance = new ChanceCalculator();
	
	private ArrayList<Event> eventList = new ArrayList<Event>();
		
	/**
	 * Reads the configuration for EventHandler and triggers reading of configuration for EventGenerators!
	 * @param config
	 */
	public void readConfig(Configuration config) {
		// read own config
		this.configstore = new EventHandlerConfigStore(config);
		
		// TODO implement dynamic Generators (classnames as stringlist in config?)
		String[] generatorNames = (String[])this.configstore.getProperty(EventHandlerConfigStore.CATEGORY, EventHandlerConfigStore.PROPERTY_GENERATORS);
		for (String generatorName : generatorNames) {
			try {
				Class<?> generatorClass = Class.forName(generatorName);
				EventGenerator generator = (EventGenerator)generatorClass.newInstance();
				generator.readConfig(config);
				if ((Boolean)generator.getConfigStore().getProperty(generator.getConfigStore().getDefaultCategory(), EventConfigStore.PROPERTY_ENABLED)) {
					this.generatorList.add(generator);
				} else {
					AdventureMode.log.warning("Events of type [" + generator.getType() + "] are disabled!");
				}
			} catch (Exception e) {
				AdventureMode.log.log(Level.SEVERE, "Could not load EventGenerator [" + generatorName + "]!", new RuntimeException("Could not load EventGenerator [" + generatorName + "]!", e));
			}
		}
	}
	
	public void stopEvents(String type) {
		for (Iterator<Event> eventIterator = this.eventList.iterator(); eventIterator.hasNext(); ) {
			Event event = eventIterator.next();
			if (EVENTTYPE_ALL.equalsIgnoreCase(type) || event.getGenerator().getType().equalsIgnoreCase(type)) {
				eventIterator.remove();
				event.end();
			}
		}
	}
	
	public void reset() {
		this.stopEvents(EVENTTYPE_ALL);
		for (EventGenerator generator : this.generatorList) {
			generator.reset();
		}
	}
	
	public EventGenerator getEventGenerator(String type) {
		for (Iterator<EventGenerator> generatorIterator = this.generatorList.iterator(); generatorIterator.hasNext(); ) {
			EventGenerator generator = generatorIterator.next();
			if (generator.getType().equalsIgnoreCase(type)) {
				return generator;
			}
		}
		
		return null;
	}
	
	public ArrayList<Event> getAffectingEvents(EntityPlayer player) {
		ArrayList<Event> resultList = new ArrayList<Event>();
	    for (Event event : eventList) {
	    	if (event.affectsPlayer(player)) {
	    		resultList.add(event);
	    	}
	    }
	    return resultList;
	}
	
	public ArrayList<Event> getActiveEvents() {
		return this.eventList;
	}
	
	/**
	 * External method to start an event (without chance calculation)
	 * @param type type of event
	 * @param player EntityPlayer to start event at
	 */
	public void startEvent(String type, EntityPlayer player) {
		for (EventGenerator generator : this.generatorList) {
			if (generator.getType().equalsIgnoreCase(type)) {
				Event event = generator.createEvent();
				generator.startEvent(event, player);
				this.eventList.add(event);
				// SYNC EVENT WITH CLIENTS
				/*
				if (Side.SERVER == FMLCommonHandler.instance().getEffectiveSide()) {
					PacketDispatcher.sendPacketToPlayer(new PacketAM001EventStarted(event).makePacket(), (Player)player);
				}
				*/
			}
		}
	}
	
	/**
	 * Updates the EventHandler on each scheduled tick.
	 * At first generators get updated at rate of generator updates for each player!
	 * Then all current Events are updated at the event update rate
	 * @param world The WorldServer to perform event detection on
	 */
	private void update(WorldServer world) {
		if (world == null) {
			return;
		}

		Side side = FMLCommonHandler.instance().getEffectiveSide();
		List playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		
		for (Iterator playerIterator = playerList.iterator(); playerIterator.hasNext(); ) {
			EntityPlayer player = (EntityPlayer)playerIterator.next();
			// Update generators after generatorDelay
			if (this.generatorDelay == 0) {
				// calculate chance for each generator per player
				double chance = 0;
				Event event = null;
				for (EventGenerator generator : this.generatorList) {
					chance = generator.getChance(player);
					event = generator.createEvent();
					// if chance is > 0, event may spawn and calculated chance occurs we create the event!
					if (chance > 0 && mayCreateEvent(event) && eventChance.calculateChance(chance)) {
						generator.startEvent(event, player);
						this.eventList.add(event);
					}
				}
			}
			
			// SYNC EVENT WITH CLIENTS
			if (Side.SERVER == FMLCommonHandler.instance().getSide()) {
				PacketDispatcher.sendPacketToPlayer(new PacketAM000EventUpdate((Event[])this.eventList.toArray(new Event[this.eventList.size()])).makePacket(), (Player)player);
			}
		}
		
		// update events after eventDelay
		if (this.eventDelay == 0) {
			for (Iterator<Event> eventIterator = this.eventList.iterator(); eventIterator.hasNext(); ) {
				Event event = eventIterator.next();
				if (!event.isActive()) {
					FMLRelaunchLog.info("Stopping event [" + event.getGenerator().getType() + "] at [" + event.getPosition() + "]");
					event.end();
					eventIterator.remove();
					/*
					// SYNC EVENT WITH CLIENTS
					for (Iterator playerIterator = playerList.iterator(); playerIterator.hasNext(); )
						if (Side.SERVER == FMLCommonHandler.instance().getEffectiveSide()) {
							PacketDispatcher.sendPacketToPlayer(new PacketAM002EventStopped(event).makePacket(), (Player)playerIterator.next());
						}
					}
					 */
				} else {
					event.update();
				}
			}
		}
		
	}
	
	/**
	 * Returns if an event may be created!
	 * Excludes creation through distance to another event, max number of events
	 * @param player EntityPlayer to check for
	 * @return true if the event may be created
	 */
	private boolean mayCreateEvent(Event newEvent) {

		int maxevents = (Integer)this.configstore.getProperty(this.configstore.CATEGORY, this.configstore.PROPERTY_MAXEVENTS);
		// check if maximum possible parallel events is reached
		if (this.eventList.size() >= maxevents) {
            FMLRelaunchLog.info("Skipping event from " + newEvent.getClass().getSimpleName() + ": Too many events (" + this.eventList.size() + "/" + maxevents + ")");
			return false;
		}
		
		// check if another event is too close
		for (Event event : this.eventList) {
			if (newEvent.getPosition().distanceTo(event.getPosition()) < (Integer)this.configstore.getProperty(configstore.CATEGORY, configstore.PROPERTY_EVENT_MINDISTANCE)) {
				FMLRelaunchLog.info("Skipping event [" + newEvent.getClass().getSimpleName() + "]: Event too close (" + event.getClass().getSimpleName() + ")");
				return false;
			}
		}
		
		return true;
		
	}
	
	  //////////////////
     /// NETWORKING ///
	//////////////////	
	
	public void handlePacket(PacketAMEvent packet) {
		if (Side.CLIENT == FMLCommonHandler.instance().getEffectiveSide()) {
			/*
			if (packet instanceof PacketAM001EventStarted) {
				if (!this.eventList.contains(packet.getEvents()[0])) {
					this.eventList.add(packet.getEvents()[0]);
				}
			} else if (packet instanceof PacketAM002EventStopped) {
				this.eventList.remove(packet.getEvents()[0]);
			} else
				*/
			if (packet instanceof PacketAM000EventUpdate) {
				this.eventList.clear();
				for (int i = 0; i < packet.getEvents().length; i++) {
					Event event = packet.getEvents()[i];
					if (event instanceof PositionalEvent && Minecraft.getMinecraft().theWorld.provider.dimensionId != ((PositionalEvent)event).getDimensionId()) {
						continue;
					}
					this.eventList.add(event);					
				}
			}
		}
	}
	
	public void onPlayerLogin(EntityPlayer player) {
		if (Side.SERVER == FMLCommonHandler.instance().getEffectiveSide()) {
			PacketDispatcher.sendPacketToPlayer(new PacketAM000EventUpdate((Event[])this.eventList.toArray(new Event[this.eventList.size()])).makePacket(), (Player)player);
		}
	}
	
	  /////////////////////////
     /// TickHandler stuff ///
	/////////////////////////
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// We do nothing here! Everything happens in tickEnd()
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// reset eventDelay if delay has passed
		if (this.eventDelay >= (Integer)this.configstore.getProperty(this.configstore.CATEGORY, this.configstore.PROPERTY_EVENT_RATE) * 20l) {
			this.eventDelay = 0;
		}
		// reset generatorDelay if delay has passed
		if (this.generatorDelay >= (Integer)this.configstore.getProperty(this.configstore.CATEGORY, this.configstore.PROPERTY_GENERATOR_RATE) * 20l) {
			this.generatorDelay = 0;
		}
		// update handler
		update((WorldServer)tickData[0]);
		// increment delays
		this.eventDelay 	+= nextTickSpacing();
		this.generatorDelay += nextTickSpacing();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "AdventureMode EventHandler";
	}

	@Override
	public int nextTickSpacing() {
		return 20;
	}
	
	/* REDUNDANT CLIENT DATA */
	/*
	private HashMap<String, String> clientAffectingEvents = new HashMap<String, String>();
	
	public String getClientAffectingEvents(EntityPlayer player) {
		return this.clientAffectingEvents.get(player.username);
	}
	
	public void setClientAffectingEvents(EntityPlayer player, String events) {
		this.clientAffectingEvents.put(player.username, events);
	}
	*/

}
