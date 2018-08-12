package de.jlab.minecraft.mods.adventuremode.event.invasion;

import net.minecraftforge.common.Configuration;
import de.jlab.minecraft.mods.adventuremode.event.EventConfigStore;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class InvasionEventConfigStore extends ConfigStore implements EventConfigStore {

	public static final String CATEGORY = "invasionevent";
	
	public static final String PROPERTY_SPAWNRADIUSMIN 	= "spawnradius_min";
	public static final String PROPERTY_SPAWNRADIUSMAX 	= "spawnradius_max";
	public static final String PROPERTY_CHANCES 		= "chances";
	
	public static final String PROPERTY_LIMITTIMEMIN	= "limit_time_min";
	public static final String PROPERTY_LIMITTIMEMAX	= "limit_time_max";
	public static final String PROPERTY_LIMITKILLMIN	= "limit_kill_min";
	public static final String PROPERTY_LIMITKILLMAX	= "limit_kill_max";
	
	public static final String PROPERTY_COOLDOWN 		= "cooldown";
	
	public static final String PROPERTY_MONSTERSMIN		= "monster_min";
	public static final String PROPERTY_MONSTERSMAX		= "monster_max";
	
	public static final String PROPERTY_MONSTERTYPES 	= "monster_types";
	public static final String PROPERTY_MONSTERCHANCES 	= "monster_chances";
	
	public static final String PROPERTY_DESPAWNAFTER	= "despawnafter";

	public InvasionEventConfigStore(Configuration config) {
		super(config);
	}
	
	@Override
	public void readConfig() {
		this.readProperty(CATEGORY, PROPERTY_ENABLED, true, "Determines if invasion events are enabled at all (boolean)");
		this.readProperty(CATEGORY, PROPERTY_SPAWNRADIUSMIN, 16, "Closest possible spawn distance (in meters) of monsters in an invasion");
		this.readProperty(CATEGORY, PROPERTY_SPAWNRADIUSMAX, 48, "Greatest possible spawn distance (in meters) of monsters in an invasion");
		this.readProperty(CATEGORY, PROPERTY_CHANCES, new double[] { 5, 20, 40, 100 }, "Chances for Invasion (in percent): anytime, moon fullness * value, thunderstorm, thunderstorm & fullmoon");

		this.readProperty(CATEGORY, PROPERTY_LIMITTIMEMIN, 300, "Shortest time-limit (in seconds) for an invasion to end");
		this.readProperty(CATEGORY, PROPERTY_LIMITTIMEMAX, 1800, "Longest time-limit (in seconds) for an invasion to end");
		this.readProperty(CATEGORY, PROPERTY_LIMITKILLMIN, 25, "Lowest kill-limit for an invasion to end");
		this.readProperty(CATEGORY, PROPERTY_LIMITKILLMAX, 200, "Highest kill-limit for an invasion to end");

		this.readProperty(CATEGORY, PROPERTY_COOLDOWN, 3600, "Cooldown (in seconds) before the next invasion can start");
		
		this.readProperty(CATEGORY, PROPERTY_MONSTERSMIN, 50, "How many monsters may minimal exist at once in one invasion");
		this.readProperty(CATEGORY, PROPERTY_MONSTERSMAX, 100, "How many monsters may maximal exist at once in one invasion");
		
		this.readProperty(CATEGORY, PROPERTY_MONSTERTYPES, new String[] { "Zombie", "Skeleton", "Spider", "PigZombie" }, "List of monster names which should spawn during an invasion");
		this.readProperty(CATEGORY, PROPERTY_MONSTERCHANCES, new double[] { 50, 35, 10, 5 }, "List of chances (in percent) for spawning the monsters given in monster_types");
				
		this.readProperty(CATEGORY, PROPERTY_DESPAWNAFTER, true, "Determines if monsters will be despawned when invasion ends (boolean)");
		
		// check chances property for length
		if (((double[])this.getProperty(CATEGORY, PROPERTY_CHANCES)).length != 4) {
			throw new IllegalArgumentException("Property " + CATEGORY + "." + PROPERTY_CHANCES + " must contain 4 numeric values!");
		}
		
		// check if number of monster chances equals number of monster names
		int monsterchanceslength = ((String[])this.getProperty(CATEGORY, PROPERTY_MONSTERTYPES)).length;
		if (((double[])this.getProperty(CATEGORY, PROPERTY_MONSTERCHANCES)).length < monsterchanceslength) {
			throw new IllegalArgumentException("Property " + CATEGORY + "." + PROPERTY_MONSTERCHANCES + " must contain " + monsterchanceslength + " numeric values!");
		}
	}

	@Override
	public String getDefaultCategory() {
		return CATEGORY;
	}

}
