package de.jlab.minecraft.adventuremode.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

/*
 * Keeps track of an entity respawn
 */
public class EntityRespawn {

	private Entity entity;
	private long dyingTime 	= 0;
	private int respawnTime = 0;
	
	/**
	 * Create entity respawn
	 * @param entity The Entity to respawn
	 * @param respawnTime The time in seconds until respawn
	 */
	public EntityRespawn(Entity entity, int respawnTime) {
		this.entity 		= entity;
		this.respawnTime 	= respawnTime * 30;
		this.resetTime();
	}
	
	public boolean updateCheck() {
		return this.entity.getEntityWorld().getTotalWorldTime() - this.dyingTime >= this.respawnTime;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public int ticksLeft() {
		return (int)(this.respawnTime - (this.entity.getEntityWorld().getTotalWorldTime() - this.dyingTime));
	}
	
	public void resetTime() {
		this.dyingTime = entity.getEntityWorld().getTotalWorldTime();
	}
	
	public Entity getNewEntity() {
		return EntityList.createEntityByIDFromName(new ResourceLocation(this.entity.getName()), this.entity.getEntityWorld());
	}
	
}
