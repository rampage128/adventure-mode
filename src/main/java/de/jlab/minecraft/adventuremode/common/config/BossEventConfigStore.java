package de.jlab.minecraft.mods.adventuremode.event.boss;

import net.minecraftforge.common.Configuration;
import de.jlab.minecraft.mods.adventuremode.event.EventConfigStore;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class BossEventConfigStore extends ConfigStore implements EventConfigStore {

	public static final String CATEGORY = "bossevent";
	
	public static final String PROPERTY_CHANCES 	= "chances";
	public static final String PROPERTY_BOSSTYPES 	= "boss_types";
	public static final String PROPERTY_BOSSCHANCES = "boss_chances";
	public static final String PROPERTY_COOLDOWN 	= "cooldown";
	
	public static final String PROPERTY_ADDSMIN 	= "adds_min";
	public static final String PROPERTY_ADDSMAX 	= "adds_max";
	public static final String PROPERTY_ADDTYPES 	= "add_types";
	public static final String PROPERTY_ADDCHANCES 	= "add_chances";
	
	public BossEventConfigStore(Configuration config) {
		super(config);
	}

	@Override
	public void readConfig() {
		this.readProperty(CATEGORY, PROPERTY_ENABLED, true, "Determines if boss events are enabled at all (boolean)");
		this.readProperty(CATEGORY, PROPERTY_CHANCES, new double[] { 0.5, 10, 20, 60 }, "Chances for a boss event (in percent): anytime, moon fullness * value, thunderstorm, thunderstorm & fullmoon");		
		this.readProperty(CATEGORY, PROPERTY_BOSSTYPES, new String[] { "Witch", "Giant" }, "List of boss names which should spawn during a boss event");
		this.readProperty(CATEGORY, PROPERTY_BOSSCHANCES, new double[] { 70, 20 }, "List of chances (in percent) for spawning the bosses given in boss_types");
		this.readProperty(CATEGORY, PROPERTY_COOLDOWN, 172800, "Cooldown (in seconds) before the next boss event can start");
		
		this.readProperty(CATEGORY, PROPERTY_ADDSMIN, 2, "Minimum number of adds escorting the boss");
		this.readProperty(CATEGORY, PROPERTY_ADDSMAX, 8, "Maximum number of adds escorting the boss");
		this.readProperty(CATEGORY, PROPERTY_ADDTYPES, new String[] { "Zombie", "Skeleton", "Enderman" }, "List of monster names which should spawn as adds during a boss event");
		this.readProperty(CATEGORY, PROPERTY_ADDCHANCES, new double[] { 70, 20, 10 }, "List of chances (in percent) for spawning the adds given in add_types");
		
		// check chances property for length
		if (((double[])this.getProperty(CATEGORY, PROPERTY_CHANCES)).length != 4) {
			throw new IllegalArgumentException("Property " + CATEGORY + "." + PROPERTY_CHANCES + " must contain 4 numeric values!");
		}
		
		// check if number of boss chances equals number of monster names
		int bosschanceslength = ((String[])this.getProperty(CATEGORY, PROPERTY_BOSSTYPES)).length;
		if (((double[])this.getProperty(CATEGORY, PROPERTY_BOSSCHANCES)).length < bosschanceslength) {
			throw new IllegalArgumentException("Property " + CATEGORY + "." + PROPERTY_BOSSCHANCES + " must contain " + bosschanceslength + " numeric values!");
		}
		
		// check if number of add chances equals number of monster names
		int addchanceslength = ((String[])this.getProperty(CATEGORY, PROPERTY_ADDTYPES)).length;
		if (((double[])this.getProperty(CATEGORY, PROPERTY_ADDCHANCES)).length < addchanceslength) {
			throw new IllegalArgumentException("Property " + CATEGORY + "." + PROPERTY_ADDCHANCES + " must contain " + addchanceslength + " numeric values!");
		}
	}

	@Override
	public String getDefaultCategory() {
		return CATEGORY;
	}

}
