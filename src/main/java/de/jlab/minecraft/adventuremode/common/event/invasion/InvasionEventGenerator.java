package de.jlab.minecraft.adventuremode.common.event.invasion;

import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.event.Event;
import de.jlab.minecraft.adventuremode.common.event.EventGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class InvasionEventGenerator extends EventGenerator {

	private boolean canInvade 			= false;
	private boolean invadeableChanged 	= false;
	
	private long lastInvasion 		= 0;
	
	@Override
	public Event createEvent() {
		return new InvasionEvent(this);
	}

	@Override
	public double getChance(EntityPlayer player) {
		World world = player.getEntityWorld();
		// initialize spawning criteria with daytime or thunderstorm
		if (world.isDaytime() && !world.isThundering()) {
			if (canInvade) {
				invadeableChanged = true;
			}
			canInvade = false;
        } else {
        	if (!canInvade) {
        		invadeableChanged = true;
        	}
        	canInvade = true;
        }
		
		// if cannot invade or invasion state did not change return 0 chance!
		if (!canInvade || !invadeableChanged) {
			return 0;
		}
		
		// reset invadeable change flag
		invadeableChanged = false;
		
		// check if cooldown is over
		if (this.lastInvasion != 0 && world.getTotalWorldTime() - this.lastInvasion < AdventureConfig.events.invasion.cooldown * 20) {
			return 0;
		}
		
		// get world state
		float moonFullness = player.getEntityWorld().getCurrentMoonPhaseFactor();
		boolean isFullMoon = moonFullness == 1;
		boolean isThundering = player.getEntityWorld().isThundering();
        
		// retrieve chances from config
		double[] chances = AdventureConfig.events.invasion.chances; //(double[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_CHANCES);
		
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
	public String getType() {
		return "invasion";
	}

	@Override
	public void reset() {
		this.lastInvasion = 0;
	}

	@Override
	protected void onEventStarted(Event event, EntityPlayer player) {
		this.lastInvasion = player.getEntityWorld().getTotalWorldTime();
	}

	@Override
	public boolean isEnabled() {
		return AdventureConfig.events.invasion.enabled;
	}

}
