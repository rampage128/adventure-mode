package de.jlab.minecraft.mods.adventuremode.event.invasion;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.event.PositionalEvent;
import de.jlab.minecraft.mods.adventuremode.utils.ChanceCalculator;
import de.jlab.minecraft.mods.adventuremode.utils.EntitySpawnHelper;
import de.jlab.minecraft.mods.adventuremode.utils.RandomRange;

public class InvasionEvent extends PositionalEvent {

	private long startTime      = 0;
	private int timeleft 		= 0;	
	private int limittime 		= 0;
	private int limitkills 		= 0;
	private int monsterCount 	= 0;
	
	private int kills 			= 0;
	
	private ArrayList<EntityLiving> monsterList = new ArrayList<EntityLiving>();
	private ChanceCalculator monsterChanceCalculator = new ChanceCalculator();
	
	private EntitySpawnHelper spawnHelper = new EntitySpawnHelper();
	
	private RandomRange randomRange = new RandomRange();
	
	public InvasionEvent(EventGenerator generator) {
		super(generator);
	}

	@Override
	public void onInit(EntityPlayer player) {
		this.limittime = randomRange.weightedRange(
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_LIMITTIMEMIN), 
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_LIMITTIMEMAX), 
				70, 30) * 20;
		
		this.limitkills = randomRange.weightedRange(
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_LIMITKILLMIN), 
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_LIMITKILLMAX), 
				30, 70);
		
		this.monsterCount = randomRange.weightedRange(
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_MONSTERSMIN), 
				(Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_MONSTERSMAX), 
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
		if (this.getPlayer().worldObj.isDaytime() && !this.getPlayer().worldObj.isRaining() && !this.getPlayer().worldObj.isThundering()) {
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
			if (this.monsterList.size() < this.monsterCount) {
				this.spawnMonster();
			}
		}
		
		// update monsters and killcounter
		for (Iterator<EntityLiving> iterator = this.monsterList.iterator(); iterator.hasNext(); ) {
			EntityLiving monster = iterator.next();
			if (monster.isDead) {
				iterator.remove();
				this.kills++;
			}
		}		
		
		this.timeleft = (int)(this.limittime - (getWorld().getTotalWorldTime() - this.startTime));
	}
	
	/**
	 * Spawns a random monster
	 */
	private void spawnMonster() {				
		// create entity by chance
		String[] monsterTypes 	= (String[])this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_MONSTERTYPES);
		double[] monsterChances = (double[])this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_MONSTERCHANCES);
		Entity entity = this.spawnHelper.getRandomEntity(monsterTypes, monsterChances, EntityList.createEntityByName(monsterTypes[0], this.getPlayer().worldObj), this.getPlayer().worldObj);		
		
		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
		
		// return if entity is not a monster
		if (entityliving == null) {
			return;
		}

		// compute spawn position
		int maxSpawnRadius = (Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_SPAWNRADIUSMAX);
		this.spawnHelper.setRandomCircleSpawnPosition(entity, this.getPosition(), (Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_SPAWNRADIUSMIN), maxSpawnRadius);
		
		int l = this.getPlayer().worldObj.getBlockLightValue(entity.chunkCoordX, entity.chunkCoordY, entity.chunkCoordZ);
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
		if ((Boolean)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_DESPAWNAFTER)) {
			for (Iterator<EntityLiving> iterator = this.monsterList.iterator(); iterator.hasNext(); ) {
				EntityLiving monster = iterator.next();
				monster.spawnExplosionParticle(); // TODO EFFECT WONT PLAY!
				monster.worldObj.removeEntity(monster);
				if (monster.isDead) {
					iterator.remove();
				}
			}
		}
		
		//Minecraft.getMinecraft().theWorld.provider.setSkyRenderer(null);
	}

	@Override
	public boolean affectsPlayer(EntityPlayer player) {		
		int radius = (Integer)this.getConfigStore().getProperty(InvasionEventConfigStore.CATEGORY, InvasionEventConfigStore.PROPERTY_SPAWNRADIUSMAX);
		Vec3 playerpos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		
		return player.worldObj.provider.dimensionId == this.getDimensionId() && (playerpos.distanceTo(this.getPosition()) < radius * 2);
	}
	
	@Override
	public String getLabelText() {
		return "Survive the hordes of " + Math.max(0, this.limitkills - this.kills) + " monsters for " + (int)Math.ceil(this.timeleft / 20f / 60f) + " minutes!";
	}

	  //////////////////
	 /// NETWORKING ///
	//////////////////
	
	@Override
	public void readFromPacket(ByteArrayDataInput in) {
		super.readFromPacket(in);
		this.startTime = in.readLong();
		this.timeleft = in.readInt();
		this.limittime = in.readInt();
		this.limitkills = in.readInt();
		this.monsterCount = in.readInt();
		this.kills = in.readInt();
	}

	@Override
	public void writeToPacket(ByteArrayDataOutput out) {
		super.writeToPacket(out);
		out.writeLong(this.startTime);
		out.writeInt(this.timeleft);
		out.writeInt(this.limittime);
		out.writeInt(this.limitkills);
		out.writeInt(this.monsterCount);
		out.writeInt(this.kills);
	}

}
