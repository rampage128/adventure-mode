package de.jlab.minecraft.mods.adventuremode.event;

import net.minecraftforge.common.Configuration;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class EventHandlerConfigStore extends ConfigStore {

	public static final String CATEGORY = "events";
	
	public static final String PROPERTY_GENERATOR_RATE 		= "generator_rate";
	public static final String PROPERTY_EVENT_RATE 			= "event_rate";
	public static final String PROPERTY_EVENT_MINDISTANCE 	= "event_mindistance";
	public static final String PROPERTY_MAXEVENTS 			= "maxevents";
	
	public static final String PROPERTY_GENERATORS 			= "generators";

	public EventHandlerConfigStore(Configuration config) {
		super(config);
	}
	
	@Override
	public void readConfig() {
		this.readProperty(CATEGORY, PROPERTY_MAXEVENTS, 5, "Maximum parallel events possible");
		this.readProperty(CATEGORY, PROPERTY_GENERATOR_RATE, 30, "Updaterate for event generators (in seconds)");
		this.readProperty(CATEGORY, PROPERTY_EVENT_RATE, 1, "Updaterate for events (in seconds)");
		this.readProperty(CATEGORY, PROPERTY_EVENT_MINDISTANCE, 600, "Minimum distance between two events (in meters)");
		
		this.readProperty(CATEGORY, PROPERTY_GENERATORS, new String[] { "de.jlab.minecraft.mods.adventuremode.event.invasion.InvasionEventGenerator", "de.jlab.minecraft.mods.adventuremode.event.boss.BossEventGenerator" }, "List of EventGenerator implementation classes (fully qualified names!)");
	}

	@Override
	public String getDefaultCategory() {
		return CATEGORY;
	}

}
