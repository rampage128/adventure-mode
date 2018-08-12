package de.jlab.minecraft.mods.adventuremode.event.invasion;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.Configuration;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class InvasionEventGenerator extends EventGenerator {

	private boolean canInvade 			= false;
	private boolean invadeableChanged 	= false;
	
	private long lastInvasion 		= 0;
	private boolean invasionCreated = false;
	
	@Override
	public Event createEvent() {
		return new InvasionEvent(this);
	}

	@Override
	public double getChance(EntityPlayer player) {
		// initialize spawning criteria with daytime or thunderstorm
		if (player.worldObj.isDaytime() && !player.worldObj.isThundering()) {
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
		if (this.lastInvasion != 0 && player.worldObj.getTotalWorldTime() - this.lastInvasion < (Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_COOLDOWN) * 20) {
			return 0;
		}
		
		// get moon fullness
		float moonfullness = player.worldObj.func_130001_d();
        
		// retrieve chances from config
		double[] chances = (double[])this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_CHANCES);
		
		// get base chance
		double chance = chances[0];
        // get moon phase chance multiplied by moon fullness
        if (moonfullness > 0.25f) {
        	chance = Math.max(chance, chances[1] * moonfullness);
        }
        // get thunderstorm chance
        if (player.worldObj.isThundering()) {
        	chance = Math.max(chance, chances[2]);
        }
        
        // get chance for thunderstorm & full moon
        if (moonfullness == 1 && player.worldObj.isThundering()) {
        	chance = chances[3];
        }
		
		return chance;
	}

	@Override
	protected ConfigStore createConfigStore(Configuration config) {
		return new InvasionEventConfigStore(config);
	}

	@Override
	public String getType() {
		return "invasion";
	}

	@Override
	public void reset() {
		this.invasionCreated 	= false;
		this.lastInvasion 		= 0;
	}

	@Override
	protected void onEventStarted(Event event, EntityPlayer player) {
		this.invasionCreated = true;
		this.lastInvasion = player.worldObj.getTotalWorldTime();
	}

}
