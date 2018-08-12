package de.jlab.minecraft.mods.adventuremode.event.boss;

import java.util.Calendar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.Configuration;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.event.invasion.InvasionEventConfigStore;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class BossEventGenerator extends EventGenerator {

	private long lastBossEvent = 0;
	
	@Override
	public Event createEvent() {
		return new BossEvent(this);
	}

	@Override
	public double getChance(EntityPlayer player) {
		// if we did check already and cooldown is not over yet we return 0 chance!
		if (lastBossEvent != 0 && player.worldObj.getTotalWorldTime() - lastBossEvent < (Integer)this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_COOLDOWN)) {
			return 0;
		}
				
		lastBossEvent = player.worldObj.getTotalWorldTime();
		
		// get moon fullness
		float moonfullness = player.worldObj.func_130001_d();
        
		// retrieve chances from config
		double[] chances = (double[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_CHANCES);
		
		// get base chance
		double chance = chances[0];
        
        // get moon phase chance multiplied by moon fullness
      	chance = Math.max(chance, chances[1] * moonfullness);
      	
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
	protected void onEventStarted(Event event, EntityPlayer player) {

	}

	@Override
	protected ConfigStore createConfigStore(Configuration config) {
		return new BossEventConfigStore(config);
	}

	@Override
	public String getType() {
		return "boss";
	}

	@Override
	public void reset() {

	}

}
