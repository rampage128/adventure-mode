package de.jlab.minecraft.adventuremode.common.event;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public abstract class Event {

	private EventGenerator generator;
	private EntityPlayer player;
	
	public Event(EventGenerator generator) {
		this.generator = generator;
	}
	
	public EventGenerator getGenerator() {
		return this.generator;
	}
	
	protected EntityPlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * Initializes the event and calls onInit
	 * @param player
	 */
	public final void init(EntityPlayer player) {
		this.player = player;
		this.onInit(player);
	}
	
	/**
	 * Initializes the event and creates all necessary objects
	 * @param player
	 */
	public abstract void onInit(EntityPlayer player);
	
	/**
	 * Return the position of this event
	 * @return
	 */
	public abstract BlockPos getPosition();

	/**
	 * Return if this event is Active
	 * @return true if active, false if inactive
	 */
	public abstract boolean isActive();
	
	/**
	 * Start the event
	 */
	public abstract boolean start();
	
	/**
	 * update this event
	 */
	public abstract void update();
	
	/**
	 * End the event
	 */
	public abstract void end();
	
	/**
	 * Check if Event affects Player
	 * @param player EntityPlayer to check for
	 */
	public abstract boolean affectsPlayer(EntityPlayer player);
	
	/**
	 * Get a label text for this event
	 */
	public abstract String getLabelText();
	
	/**
	 * Read this event from a network packet
	 */
	public abstract void readFromPacket(ByteBuf in);
	
	/**
	 * Write this event to a network packet 
	 */
	public abstract void writeToPacket(ByteBuf out);
	
}