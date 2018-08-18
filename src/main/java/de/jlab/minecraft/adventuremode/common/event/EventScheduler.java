package de.jlab.minecraft.adventuremode.common.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.network.AdventureNetworkHandler;
import de.jlab.minecraft.adventuremode.common.network.EventMessage;
import de.jlab.minecraft.adventuremode.utils.ChanceCalculator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid=AdventureMode.MODID)
public class EventScheduler {
	
	public static final String EVENTTYPE_ALL = "all";
	
	private long eventUpdateTime = 0;
	private long generatorUpdateTime = 0;
	private ArrayList<EventGenerator> generatorList = new ArrayList<EventGenerator>();
	private ChanceCalculator eventChance = new ChanceCalculator();
	
	private ArrayList<Event> eventList = new ArrayList<Event>();
	private Event[] eventCache = new Event[0];
		
	public EventScheduler() {
		AdventureNetworkHandler.INSTANCE.registerMessage(this.eventUpdateMessageHandler, EventMessage.class, 0, Side.CLIENT);
	}
	
	public void updateConfig() {
		this.generatorList.clear();
		for (EventType eventType : EventType.values()) {
			try {
				EventGenerator generator = eventType.getGenerator();
				if (generator.isEnabled()) {
					this.generatorList.add(generator);
				}
			} catch (Exception e) {
				AdventureMode.logger.error("Could not load EventGenerator [" + eventType.name() + "]!", e);
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
		refreshEventCache();
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
	    for (Event event : this.eventCache) {
	    	if (event.affectsPlayer(player)) {
	    		resultList.add(event);
	    	}
	    }
	        
	    return resultList;
	}
	
	public Event[] getActiveEvents() {
		return this.eventCache;
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
				if (generator.startEvent(event, player)) {
					this.eventList.add(event);
				}
				else {
					event.end();
				}
				refreshEventCache();
				if (Side.SERVER == FMLCommonHandler.instance().getSide()) {
					AdventureNetworkHandler.INSTANCE.sendTo(new EventMessage(EventMessage.TYPE_STARTED, event), (EntityPlayerMP)player);
				}
			}
		}
	}
	
	private void updateEvents(World world) {
		boolean eventEnded = false;
		
		for (Iterator<Event> eventIterator = this.eventList.iterator(); eventIterator.hasNext(); ) {
			Event event = eventIterator.next();
			if (!event.isActive()) {
				eventEnded = true;
				AdventureMode.logger.info("Stopping event [" + event.getGenerator().getType() + "] at [" + event.getPosition() + "]");
				event.end();
				eventIterator.remove();
				if (Side.SERVER == FMLCommonHandler.instance().getSide()) {
					AdventureNetworkHandler.INSTANCE.sendToAll(new EventMessage(EventMessage.TYPE_ENDED, event));
				}
			} else {
				event.update();
			}
		}
		
		if (eventEnded) {
			refreshEventCache();
		}
	}
	
	private void updateGenerators(World world) {
		boolean eventStarted = false;
		
		List<EntityPlayer> playerList = world.playerEntities;
		for (EntityPlayer player: playerList) {
			for (EventGenerator generator : this.generatorList) {
				double probability = generator.getProbability(player);
				if (probability > 0 && eventChance.calculateChance(probability)) {
					Event event = generator.createEvent();
					if (mayCreateEvent(event)) {
						eventStarted = true;
						if (generator.startEvent(event, player)) {
							this.eventList.add(event);
						}
						else {
							event.end();
						}
					}
				}
			}
		}
		
		if (eventStarted) {
			refreshEventCache();
		}
		
		if (Side.SERVER == FMLCommonHandler.instance().getSide()) {
			AdventureNetworkHandler.INSTANCE.sendToAll(new EventMessage(EventMessage.TYPE_UPDATE, this.eventCache));
		}
	}
	
	private boolean mayCreateEvent(Event newEvent) {
		int maxevents = AdventureConfig.events.maxEvents;
		// check if maximum possible parallel events is reached
		if (this.eventList.size() >= maxevents) {
            AdventureMode.logger.info("Skipping event from " + newEvent.getClass().getSimpleName() + ": Too many events (" + this.eventList.size() + "/" + maxevents + ")");
			return false;
		}
		
		// check if another event is too close
		for (Event event : this.eventList) {
			if (newEvent.getPosition().distanceSq(event.getPosition()) < (Integer)AdventureConfig.events.eventMinDistance) {
				AdventureMode.logger.info("Skipping event [" + newEvent.getClass().getSimpleName() + "]: Event too close (" + event.getClass().getSimpleName() + ")");
				return false;
			}
		}
		
		return true;
	}
	
	private void refreshEventCache() {
		this.eventCache = this.eventList.toArray(new Event[this.eventList.size()]);
	}
	
	  //////////////////
     /// NETWORKING ///
	//////////////////	
	
	private IMessageHandler<EventMessage, IMessage> eventUpdateMessageHandler = new IMessageHandler<EventMessage, IMessage>() {

		@Override
		public IMessage onMessage(EventMessage message, MessageContext ctx) {
			int type = message.getType(); 
			if (type == EventMessage.TYPE_UPDATE) {
				EventScheduler.this.eventList.clear();
			}
			for (Event event : message.getEvents()) {
				if (type == EventMessage.TYPE_UPDATE || type == EventMessage.TYPE_STARTED) {
					EventScheduler.this.eventList.add(event);
				}
				else {
					EventScheduler.this.eventList.remove(event);
				}
			}
					
			EventScheduler.this.refreshEventCache();
			
			return null;
		}
		
	};
	
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
    	if (Side.SERVER == FMLCommonHandler.instance().getSide()) {
			AdventureNetworkHandler.INSTANCE.sendTo(new EventMessage(EventMessage.TYPE_UPDATE, this.eventList.toArray(new Event[0])), (EntityPlayerMP)event.player);
		}
    }
	
	  /////////////////////////
     /// TickHandler stuff ///
	/////////////////////////
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == Phase.END) {
			// reset eventDelay if delay has passed
			if (System.currentTimeMillis() - this.eventUpdateTime >= AdventureConfig.events.eventUpdateRate * 1000l) {
				this.updateEvents(event.world);
				this.eventUpdateTime = System.currentTimeMillis();
			}
			// reset generatorDelay if delay has passed
			if (System.currentTimeMillis() - this.generatorUpdateTime >= AdventureConfig.events.generatorUpdateRate * 1000l) {
				this.updateGenerators(event.world);
				this.generatorUpdateTime = System.currentTimeMillis();
			}		
		}
	}

}
