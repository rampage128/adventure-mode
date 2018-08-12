package de.jlab.minecraft.adventuremode.common.config;

import java.util.HashMap;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.event.EventType;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;

@Config(modid = AdventureMode.MODID, category = "")
public class AdventureConfig {

	public static Items items = new Items();
	
	public static Events events = new Events();
	
	public static class Items {
		@Name("replace_tools")
		@RequiresWorldRestart
		@Comment("Determines if vanilla tools will be replaced with adventure tools (boolean)")
		public boolean replaceTools = true;
	}
	
	public static class Events {
		@Comment("Maximum number of parallel events")
		@RangeInt(min = 1, max = 10)
		public int maxEvents = 5;
		
		@Comment("Updaterate for event generators (in seconds)")
		@RangeInt(min = 1, max = 60 * 5)
		public int generatorUpdateRate = 30;
		
		@Comment("Updaterate for events (in seconds)")
		@RangeInt(min = 1, max = 10)
		public int eventUpdateRate = 1;
		
		@Comment("Minimum distance between two events (in meters)")
		@RangeInt(min = 1, max = 10)
		public int eventMinDistance = 600;
		
		@Comment({ 
			"List of possible event types. Use this to disable events you do not like.",
			"Possible values: INVASION, BOSS"
		})
		public EventType[] possibleEventTypes = { 
			EventType.BOSS,
			EventType.INVASION
		};
		
		public EventType testType = EventType.BOSS;
		
		public Boss boss = new Boss();
		
		public static class Boss {
			@Comment("Cooldown (in seconds) before the next boss event can start")
			public int cooldown = 180;
			
			@Comment("Chances for a boss event (in percent): anytime, moon fullness * value, thunderstorm, thunderstorm & fullmoon")
			public double[] chances = { 0.5, 10, 20, 60 };
			
			@Comment("Minimum number of adds escorting the boss")
			@RangeInt(min = 0, max = 20)
			public int minAdds = 2;
			
			@Comment("Maximum number of adds escorting the boss")
			@RangeInt(min = 0, max = 20)
			public int maxAdds = 5;
			
			private static HashMap<String, Double> BOSS_TYPES = new HashMap<>();
			
			@Comment("List of boss names which should spawn during a boss event with a percentage of spawning chance")
			public HashMap<String, Double> bossTypes = BOSS_TYPES;
						
			static {
				BOSS_TYPES.put("minecraft:witch", 70d);
				BOSS_TYPES.put("minecraft:giant", 30d);
	        }
			
			
			private static HashMap<String, Double> ADD_TYPES = new HashMap<>();
			
			@Comment("List of monster names which should spawn as adds during a boss event with a percentage of spawning chance")
			public HashMap<String, Double> addTypes = ADD_TYPES;

			static {
				ADD_TYPES.put("minecraft:zombie", 70d);
				ADD_TYPES.put("minecraft:skeleton", 20d);
				ADD_TYPES.put("minecraft:enderman", 10d);
	        }
		}
		
		public Invasion invasion = new Invasion();
		
		public static class Invasion {
			@Comment("Cooldown (in seconds) before the next invasion can start")
			public int cooldown = 3600;
			
			@Comment("Chances for Invasion (in percent): anytime, moon fullness * value, thunderstorm, thunderstorm & fullmoon")
			public double[] chances = { 0.5, 10, 20, 60 };
			
			@Comment("Closest possible spawn distance (in meters) of monsters in an invasion")
			@RangeInt(min = 5, max = 128)
			public int spawnRadiusMin = 16;
			
			@Comment("Greatest possible spawn distance (in meters) of monsters in an invasion")
			@RangeInt(min = 5, max = 128)
			public int spawnRadiusMax = 48;
			
			@Comment("Shortest time-limit (in seconds) for an invasion to end")
			@RangeInt(min = 60, max = 86400)
			public int timeLimitMin = 300;
			
			@Comment("Longest time-limit (in seconds) for an invasion to end")
			@RangeInt(min = 60, max = 86400)
			public int timeLimitMax = 1800;
			
			@Comment("Lowest kill-limit for an invasion to end")
			@RangeInt(min = 10, max = 5000)
			public int killLimitMin = 25;
			
			@Comment("Highest kill-limit for an invasion to end")
			@RangeInt(min = 10, max = 5000)
			public int killLimitMax = 200;

			@Comment("How many monsters may minimal exist at once in one invasion")
			@RangeInt(min = 5, max = 300)
			public int monsterLimitMin = 50;
			
			@Comment("How many monsters may maximal exist at once in one invasion")
			@RangeInt(min = 5, max = 300)
			public int monsterLimitMax = 100;

			private static HashMap<String, Double> MONSTER_TYPES = new HashMap<>();
			
			@Comment("List of monster names which should spawn during an invasion with a percentage of spawning chance")
			public HashMap<String, Double> monsterTypes = MONSTER_TYPES;
						
			static {
				MONSTER_TYPES.put("minecraft:zombie", 50d);
				MONSTER_TYPES.put("minecraft:skeleton", 35d);
				MONSTER_TYPES.put("minecraft:spider", 10d);
				MONSTER_TYPES.put("minecraft:pigzombie", 5d);
	        }
			
			@Comment("Determines if monsters will be despawned when invasion ends (boolean)")
			public boolean despawnOnEnd = true;
		}
	}
	
}