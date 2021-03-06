package de.jlab.minecraft.adventuremode.common.event;

import de.jlab.minecraft.adventuremode.AdventureMode;
import net.minecraft.entity.player.EntityPlayer;

public abstract class EventGenerator {

	/**
	 * Creates an event around the given player
	 * @param player
	 * @return
	 */
	public abstract Event createEvent();
	
	/**
	 * Calculates the probability for an event to occur
	 * @param player
	 * @return
	 */
	public abstract double getProbability(EntityPlayer player);
	
	public abstract boolean isEnabled();
	
	public final boolean startEvent(Event event, EntityPlayer player) {
		AdventureMode.logger.info("Starting event [" + event.getClass().getSimpleName() + "] at player [" + player.getName() + "]");
		event.init(player);
		return event.start();
	}
	
	protected abstract void onEventStarted(Event event, EntityPlayer player);
	
	public abstract String getType();
	
	public abstract void reset();
}
