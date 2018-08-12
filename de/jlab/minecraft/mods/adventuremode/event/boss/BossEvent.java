package de.jlab.minecraft.mods.adventuremode.event.boss;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.event.PositionalEvent;
import de.jlab.minecraft.mods.adventuremode.event.invasion.InvasionEventConfigStore;
import de.jlab.minecraft.mods.adventuremode.utils.EntityRespawn;
import de.jlab.minecraft.mods.adventuremode.utils.EntitySpawnHelper;
import de.jlab.minecraft.mods.adventuremode.utils.RandomRange;

public class BossEvent extends PositionalEvent {

	private EntityLiving boss;
	
	private int addCount = 0;
	private ArrayList<EntityLiving> addList = new ArrayList<EntityLiving>();
	private ArrayList<EntityRespawn> respawnList = new ArrayList<EntityRespawn>();
	
	private RandomRange randomRange = new RandomRange();
	private EntitySpawnHelper spawnHelper = new EntitySpawnHelper();
	
	public BossEvent(EventGenerator generator) {
		super(generator);
	}

	@Override
	public void onInit(EntityPlayer player) {
		super.onInit(player);
		this.addCount = randomRange.weightedRange(
				(Integer)this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_ADDSMIN), 
				(Integer)this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_ADDSMAX), 
				30, 70);
	}

	private void spawnboss() {
		// create entity by chance
		String[] monsterTypes 	= (String[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_BOSSTYPES);
		double[] monsterChances = (double[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_BOSSCHANCES);
		Entity entity = this.spawnHelper.getRandomEntity(monsterTypes, monsterChances, EntityList.createEntityByName(monsterTypes[0], this.getPlayer().worldObj), this.getPlayer().worldObj);		
		
		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
		
		// return if entity is not a monster
		if (entityliving == null) {
			return;
		}

		// compute spawn position
		this.spawnHelper.setRandomCircleSpawnPosition(entity, Vec3.createVectorHelper(this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ), 16, 32);
		//this.spawnHelper.setSpawnPosition(entity, Vec3.createVectorHelper(this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ), 8);
		
		// abort spawning if conditions are not met
		//if (!entityliving.getCanSpawnHere()) {
			//return;
		//}
		
		// spawn monster
		this.spawnHelper.spawnEntityLiving(entityliving);
		
		// set aggro to nearest player
		this.spawnHelper.autoAggro(entityliving, 40);
		
		// set boss
		this.boss = entityliving;
		
		this.setPosition(this.boss.posX, this.boss.posY, this.boss.posZ);
	}
	
	private void spawnadds() {
		for (int i = 0; i < this.addCount; i++) {
			EntityLiving add = createAdd();
			if (add != null) {
				this.addList.add(add);
			}
		}
	}
	
	private EntityLiving createAdd() {		
		// create entity by chance
		String[] monsterTypes 	= (String[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_ADDTYPES);
		double[] monsterChances = (double[])this.getConfigStore().getProperty(BossEventConfigStore.CATEGORY, BossEventConfigStore.PROPERTY_ADDCHANCES);
		Entity entity = this.spawnHelper.getRandomEntity(monsterTypes, monsterChances, EntityList.createEntityByName(monsterTypes[0], this.getPlayer().worldObj), this.getPlayer().worldObj);		
		
		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
		
		// return if entity is not a monster
		if (entityliving == null) {
			return null;
		}
		
		return spawnAdd(entityliving);
	}
	
	private EntityLiving spawnAdd(EntityLiving add) {
		// compute spawn position
		this.spawnHelper.setSpawnPosition(add, this.getPosition(), 3);
		
		// abort spawning if conditions are not met
		if (!add.getCanSpawnHere()) {
			return null;
		}
		
		// spawn monster
		this.spawnHelper.spawnEntityLiving(add);
		
		// set aggro to nearest player
		this.spawnHelper.autoAggro(add, -1);
		
		return add;
	}
	
	@Override
	public boolean isActive() {
		// stop event when boss does not exist or is dead
		return this.boss != null && !this.boss.isDead;
	}

	@Override
	public void start() {
		this.spawnboss();
		this.spawnadds();
	}
	
	@Override
	public void update() {
		this.setPosition(this.boss.posX, this.boss.posY, this.boss.posZ);
		
		for (Iterator<EntityLiving> addIterator = this.addList.iterator(); addIterator.hasNext();) {
			EntityLiving add = addIterator.next();
			if (add.isDead) {
				EntityRespawn respawn = new EntityRespawn(add, 5);
				this.respawnList.add(respawn);
				addIterator.remove();
			}
		}
		
		for (Iterator<EntityRespawn> respawnIterator = this.respawnList.iterator(); respawnIterator.hasNext();) {
			EntityRespawn respawn = respawnIterator.next();
			if (respawn.updateCheck()) {
				EntityLiving newEntity = (EntityLiving)respawn.getNewEntity();
				if (spawnAdd(newEntity) != null) {
					this.addList.add(newEntity);
					respawnIterator.remove();
				} else {
					respawn.resetTime();
				}
			}
		}
	}

	@Override
	public void end() {
		for (EntityLiving add : this.addList) {
			if (this.boss != null && this.boss.isDead) {
				add.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
			} else {
				add.worldObj.removeEntity(add);
			}
		}
		if (this.boss != null && !this.boss.isDead) {
			boss.worldObj.removeEntity(boss);
		}
		this.addList.clear();
	}

	@Override
	public boolean affectsPlayer(EntityPlayer player) {
		int radius = 64;
		Vec3 playerpos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		return (playerpos.distanceTo(this.getPosition()) < radius + radius / 2);
	}

	@Override
	public String getLabelText() {
		return "Defeat the evil " + this.boss.getEntityName() + "!";
	}
	
	  //////////////////
	 /// NETWORKING ///
	//////////////////
	
	@Override
	public void readFromPacket(ByteArrayDataInput in) {
		super.readFromPacket(in);
		this.boss = (EntityLiving)EntityList.createEntityByName(in.readUTF(), this.getWorld());
	}

	@Override
	public void writeToPacket(ByteArrayDataOutput out) {
		super.writeToPacket(out);
		out.writeUTF(EntityList.getEntityString(this.boss));
	}
	
}
