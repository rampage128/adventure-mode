package de.jlab.minecraft.adventuremode.common.event.invasion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.event.EventGenerator;
import de.jlab.minecraft.adventuremode.common.event.PositionalEvent;
import de.jlab.minecraft.adventuremode.utils.EntitySpawnHelper;
import de.jlab.minecraft.adventuremode.utils.RandomRange;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;

public class InvasionEvent extends PositionalEvent {

	private long startTime      = 0;
	private int timeleft 		= 0;	
	private int limittime 		= 0;
	private int limitkills 		= 0;
	private int monsterCount 	= 0;
	
	private int kills 			= 0;
	
	private ArrayList<EntityLiving> monsterList = new ArrayList<EntityLiving>();
	
	private EntitySpawnHelper spawnHelper = new EntitySpawnHelper();
	
	private RandomRange randomRange = new RandomRange();
	
	public InvasionEvent(EventGenerator generator) {
		super(generator);
	}

	@Override
	public void onInit(EntityPlayer player) {
		this.limittime = randomRange.weightedRange(
				AdventureConfig.events.invasion.timeLimitMin, 
				AdventureConfig.events.invasion.timeLimitMax, 
				70, 30) * 20;
		
		this.limitkills = randomRange.weightedRange(
				AdventureConfig.events.invasion.killLimitMin, 
				AdventureConfig.events.invasion.killLimitMax,  
				30, 70);
		
		this.monsterCount = randomRange.weightedRange(
				AdventureConfig.events.invasion.monsterLimitMin, 
				AdventureConfig.events.invasion.monsterLimitMax, 
				30, 70);
		
		super.onInit(player);
	}

	@Override
	public boolean isActive() {
		// is inactive if time is up
		if (this.timeleft <= 0) {
			return false;
		}
		
		// is inactive if killlimit is reached
		if (this.kills > this.limitkills) {
			return false;
		}
		
		// is inactive if conditions vanish (night time, rain, thunderstorm)
		if (this.getWorld().isDaytime() && !this.getWorld().isRaining() && !this.getWorld().isThundering()) {
			return false;
		}
		
		return true;
	}

	@Override
	public void start() {
		this.startTime 	= this.getWorld().getTotalWorldTime();
		this.timeleft 	= this.limittime;
		//Minecraft.getMinecraft().theWorld.provider.setSkyRenderer(new InvasionSkyRenderer());
	}
	
	@Override
	public void update() {		
		// spawn some monsters
		for (int i = 0; i < 20; i++) {
			if (this.monsterList.size() < this.monsterCount && this.monsterList.size() < this.limitkills - this.kills) {
				this.spawnMonster();
			}
		}
		
		// update monsters and killcounter
		for (Iterator<EntityLiving> iterator = this.monsterList.iterator(); iterator.hasNext(); ) {
			EntityLiving monster = iterator.next();
			if (monster.isDead) {
				iterator.remove();
				if (monster.getLastDamageSource() != null) { //  && monster.getLastDamageSource().getTrueSource() instanceof EntityPlayer
					this.kills++;
				}
			}
		}		
		
		this.timeleft = (int)(this.limittime - (getWorld().getTotalWorldTime() - this.startTime));
	}
	
	/**
	 * Spawns a random monster
	 */
	private void spawnMonster() {						
		// create entity by chance
		HashMap<String, Double> monsterTypes = AdventureConfig.events.invasion.monsterTypes;
		Entity entity = this.spawnHelper.getRandomEntity(monsterTypes, "minecraft:zombie", this.getWorld());		
		
		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
		
		// return if entity is not a monster
		if (entityliving == null) {
			return;
		}

		// compute spawn position
		int maxSpawnRadius = AdventureConfig.events.invasion.spawnRadiusMax;
		int minSpawnRadius = AdventureConfig.events.invasion.spawnRadiusMin;
		this.spawnHelper.setRandomCircleSpawnPosition(entity, this.getPosition(), minSpawnRadius, maxSpawnRadius);
		
		System.out.println("Spawning monster " + (entityliving == null ? "Unknown" : entityliving.getName()));
		
		int l = this.getWorld().getLightFor(EnumSkyBlock.BLOCK, entity.getPosition().down());
		// abort spawning if conditions are not met and light level is above 10 (thunderstorm)
		if (!entityliving.getCanSpawnHere() || l > 10) {
			return;
		}
				
		// spawn monster
		this.spawnHelper.spawnEntityLiving(entityliving);
		
		// set aggro to nearest player
		this.spawnHelper.autoAggro(entityliving, maxSpawnRadius);
		
		// add monster to monsterlist
		this.monsterList.add(entityliving);
	}

	@Override
	public void end() {
		// despawn all monsters if desired
		if (AdventureConfig.events.invasion.despawnOnEnd) {
			for (Iterator<EntityLiving> iterator = this.monsterList.iterator(); iterator.hasNext(); ) {
				EntityLiving monster = iterator.next();
				monster.spawnExplosionParticle(); // TODO EFFECT WONT PLAY!
				monster.getEntityWorld().removeEntity(monster);
				if (monster.isDead) {
					iterator.remove();
				}
			}
		}
		
		//Minecraft.getMinecraft().theWorld.provider.setSkyRenderer(null);
	}

	@Override
	public int getRadius() {		
		return AdventureConfig.events.invasion.spawnRadiusMax * 2;
	}
	
	@Override
	public String getLabelText() {
		return "Survive the hordes of " + Math.max(0, this.limitkills - this.kills) + " monsters for " + (int)Math.ceil(this.timeleft / 20f / 60f) + " minutes!";
	}

	  //////////////////
	 /// NETWORKING ///
	//////////////////
	
	@Override
	public void readFromPacket(ByteBuf in) {
		super.readFromPacket(in);
		this.startTime = in.readLong();
		this.timeleft = in.readInt();
		this.limittime = in.readInt();
		this.limitkills = in.readInt();
		this.monsterCount = in.readInt();
		this.kills = in.readInt();
	}

	@Override
	public void writeToPacket(ByteBuf out) {
		super.writeToPacket(out);
		out.writeLong(this.startTime);
		out.writeInt(this.timeleft);
		out.writeInt(this.limittime);
		out.writeInt(this.limitkills);
		out.writeInt(this.monsterCount);
		out.writeInt(this.kills);
	}

}
