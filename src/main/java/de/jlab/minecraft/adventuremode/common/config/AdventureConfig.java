package de.jlab.minecraft.adventuremode.common.config;

import java.util.HashMap;

import de.jlab.minecraft.adventuremode.AdventureMode;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AdventureMode.MODID, category = "")
@Config.LangKey("adventuremode.config.title")
public class AdventureConfig {

    @SubscribeEvent
    public static void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(AdventureMode.MODID)) {
            ConfigManager.sync(AdventureMode.MODID, Config.Type.INSTANCE);
            AdventureMode.instance.getEventScheduler().updateConfig();
        }
    }
	
	public static Items items = new Items();
	
	public static Events events = new Events();
	
	public static class Items {
		@Name("replace_tools")
		@RequiresWorldRestart
		@Comment("Replace vanilla shovel, axe, pickaxe and hoe.")
		public boolean replaceTools = true;
	}
	
	public static class Events {
		@Comment("Maximum number of parallel events.")
		@RangeInt(min = 1, max = 10)
		public int maxEvents = 5;
		
		@Comment("Updaterate for event generators (in seconds).")
		@RangeInt(min = 1, max = 60 * 5)
		public int generatorUpdateRate = 30;
		
		@Comment("Updaterate for events (in seconds).")
		@RangeInt(min = 1, max = 10)
		public int eventUpdateRate = 1;
		
		@Comment("Minimum distance between two events (in meters).")
		@RangeInt(min = 1, max = 10)
		public int eventMinDistance = 600;
			
		public Boss boss = new Boss();
		
		public static class Boss {
			@Comment("Determines if boss events should be enabled.")
			public boolean enabled = true;
			
			@Comment("Cool down (in minutes) before the next boss event can start.")
			public int cooldown = 120;
			
			@Comment("Configure probabilities of appearance for boss events.")		
			public Probabilities probabilities = new Probabilities(); 
						
			@Comment("Minimum number of adds escorting the boss.")
			@RangeInt(min = 0, max = 20)
			public int minAdds = 2;
			
			@Comment("Maximum number of adds escorting the boss.")
			@RangeInt(min = 0, max = 20)
			public int maxAdds = 5;
			
			private static HashMap<String, Double> BOSS_TYPES = new HashMap<>();
			
			@Comment("List of boss mob names which should spawn during a boss event with a percentage of spawning chance.")
			public HashMap<String, Double> bossTypes = BOSS_TYPES;
						
			static {
				BOSS_TYPES.put("minecraft:witch", 60d);
				BOSS_TYPES.put("minecraft:wither_skeleton", 40d);
	        }
			
			
			private static HashMap<String, Double> ADD_TYPES = new HashMap<>();
			
			@Comment("List of mob names which should spawn as adds during a boss event with a percentage of spawning chance.")
			public HashMap<String, Double> addTypes = ADD_TYPES;

			static {
				ADD_TYPES.put("minecraft:zombie", 70d);
				ADD_TYPES.put("minecraft:skeleton", 20d);
				ADD_TYPES.put("minecraft:enderman", 10d);
	        }
		}
		
		public Invasion invasion = new Invasion();
		
		public static class Invasion {
			@Comment("Determines if invasion events should be enabled.")
			public boolean enabled = true;
			
			@Comment("Cool down (in minutes) before the next invasion can start.")
			public int cooldown = 180;
					
			@Comment("Configure probabilities of appearance for invasion events.")		
			public Probabilities probabilities = new Probabilities(); 
			
			@Comment("Closest possible spawn distance (in meters) of monsters in an invasion.")
			@RangeInt(min = 5, max = 128)
			public int spawnRadiusMin = 16;
			
			@Comment("Greatest possible spawn distance (in meters) of monsters in an invasion.")
			@RangeInt(min = 5, max = 128)
			public int spawnRadiusMax = 48;
			
			@Comment("Shortest time-limit (in seconds) for an invasion to end.")
			@RangeInt(min = 60, max = 86400)
			public int timeLimitMin = 300;
			
			@Comment("Longest time-limit (in seconds) for an invasion to end.")
			@RangeInt(min = 60, max = 86400)
			public int timeLimitMax = 1800;
			
			@Comment("Lowest kill-limit for an invasion to end.")
			@RangeInt(min = 10, max = 5000)
			public int killLimitMin = 25;
			
			@Comment("Highest kill-limit for an invasion to end.")
			@RangeInt(min = 10, max = 5000)
			public int killLimitMax = 200;

			@Comment("How many mobs may minimal exist at once in one invasion.")
			@RangeInt(min = 5, max = 300)
			public int monsterLimitMin = 50;
			
			@Comment("How many mobs may maximal exist at once in one invasion.")
			@RangeInt(min = 5, max = 300)
			public int monsterLimitMax = 100;

			private static HashMap<String, Double> MONSTER_TYPES = new HashMap<>();
			
			@Comment("List of mob names which should spawn during an invasion with a percentage of spawning chance.")
			public HashMap<String, Double> monsterTypes = MONSTER_TYPES;
						
			static {
				MONSTER_TYPES.put("minecraft:zombie", 50d);
				MONSTER_TYPES.put("minecraft:skeleton", 35d);
				MONSTER_TYPES.put("minecraft:spider", 10d);
				MONSTER_TYPES.put("minecraft:pigzombie", 5d);
	        }
			
			@Comment("Determines if remaining mobs will be despawned when invasion ends (boolean).")
			public boolean despawnOnEnd = true;
		}
	}
	
	public static class Probabilities {
		@Comment("Probability for an event at any given time.")
		@RangeDouble(min = 0, max = 100)
		public double anyTime = 0.5;
		@Comment({ 
			"Probability based on moon fullness (if moon is at least quarter full).", 
			"This is multiplied with the factor of moon fullness:",
			"For a probability of 10% at half moon => 10% * 0.5 = 5%"
			})
		@RangeDouble(min = 0, max = 100)
		public double moonFactor = 10;
		@Comment("Probability for an event during thunderstorms.")
		@RangeDouble(min = 0, max = 100)
		public double thunderstorm = 20;
		@Comment("Probability for an event during thunderstorms at full moon.")
		@RangeDouble(min = 0, max = 100)
		public double fullMoonThunderstorm = 60;
	}
	
}
