package de.jlab.minecraft.mods.adventuremode.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

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
		return this.entity.worldObj.getTotalWorldTime() - this.dyingTime >= this.respawnTime;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public int ticksLeft() {
		return (int)(this.respawnTime - (this.entity.worldObj.getTotalWorldTime() - this.dyingTime));
	}
	
	public void resetTime() {
		this.dyingTime = entity.worldObj.getTotalWorldTime();
	}
	
	public Entity getNewEntity() {
		return EntityList.createEntityByName(EntityList.getEntityString(this.entity), this.entity.worldObj);
	}
	
}
