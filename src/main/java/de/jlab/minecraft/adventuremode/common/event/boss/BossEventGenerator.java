package de.jlab.minecraft.adventuremode.common.event.boss;

import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.event.Event;
import de.jlab.minecraft.adventuremode.common.event.EventGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class BossEventGenerator extends EventGenerator {

	private long lastBossEvent = 0;
	
	@Override
	public Event createEvent() {
		return new BossEvent(this);
	}

	@Override
	public double getChance(EntityPlayer player) {
		// if we did check already and cooldown is not over yet we return 0 chance!
		if (lastBossEvent != 0 && player.getEntityWorld().getTotalWorldTime() - lastBossEvent < AdventureConfig.events.boss.cooldown) {
			return 0;
		}
				
		lastBossEvent = player.getEntityWorld().getTotalWorldTime();
		
		// get world state
		float moonFullness = player.getEntityWorld().getCurrentMoonPhaseFactor();
		boolean isFullMoon = moonFullness == 1;
		boolean isThundering = player.getEntityWorld().isThundering();
        
		// retrieve chances from config
		double[] chances = AdventureConfig.events.boss.chances; //(double[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_CHANCES);
		
		// get base chance
		double chance = chances[0];
        
        // get moon phase chance multiplied by moon fullness
		if (moonFullness > 0.25f) {
			chance = Math.max(chance, chances[1] * moonFullness);
		}
      	
        // get thunderstorm chance
        if (isThundering) {
        	chance = chances[2];
        }
        
        // get chance for thunderstorm & full moon
        if (isFullMoon && isThundering) {
        	chance = chances[3];
        }
		
		return chance;
	}

	@Override
	protected void onEventStarted(Event event, EntityPlayer player) {}

	@Override
	public String getType() {
		return "boss";
	}

	@Override
	public void reset() {

	}

}
