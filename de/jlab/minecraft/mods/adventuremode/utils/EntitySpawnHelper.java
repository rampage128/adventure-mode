package de.jlab.minecraft.mods.adventuremode.utils;

import de.jlab.minecraft.mods.adventuremode.entity.ai.EntityAIOnARampage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySpawnHelper {

	private RandomRange randomRange = new RandomRange();
	private ChanceCalculator chanceCalculator = new ChanceCalculator();
	
	public Entity getRandomEntity(String[] entityNames, double[] entityChances, Entity defaultEntity, World world) {
		Entity entity = null;
		
		for (int i = 0; i < entityNames.length; i++) {
			String name 	= entityNames[i];
			double chance 	= entityChances[i]; 
			if (chanceCalculator.calculateChance(chance)) {
				entity = EntityList.createEntityByName(name, world);
				break;
			}
		}
		
		// get default entity if no other could be spawned
		if (entity == null && defaultEntity != null) {
			return defaultEntity;
		}
		
		return entity;
	}
	
	public void setRandomCircleSpawnPosition(Entity entity, Vec3 center, int minRadius, int maxRadius) {
	    // create random angle between 0 to 360 degrees
		int radius = minRadius;
		if (minRadius != maxRadius) {
			radius = randomRange.range(minRadius, maxRadius);
		}
	    int ang = randomRange.range(0, 360);
	    double sx = center.xCoord + radius * Math.sin(Math.toRadians(ang));
	    double sz = center.zCoord + radius * Math.cos(Math.toRadians(ang));
	    double sy = entity.worldObj.getTopSolidOrLiquidBlock((int)sx, (int)sz) + 1;
	    entity.setLocationAndAngles(sx, sy, sz, entity.worldObj.rand.nextFloat() * 360.0F, 0.0F);
	}
	
	public void setSpawnPosition(Entity entity, Vec3 position, int spawnRadius) {
		double sx = (double)position.xCoord + (entity.worldObj.rand.nextDouble() - entity.worldObj.rand.nextDouble()) * (double)spawnRadius;
        double sz = (double)position.zCoord + (entity.worldObj.rand.nextDouble() - entity.worldObj.rand.nextDouble()) * (double)spawnRadius;
        double sy = entity.worldObj.getTopSolidOrLiquidBlock((int)sx, (int)sz) + 1;
        entity.setLocationAndAngles(sx, sy, sz, entity.worldObj.rand.nextFloat() * 360.0F, 0.0F);
	}
	
	public void spawnEntityLiving(EntityLiving entityliving) {
		entityliving.spawnExplosionParticle();
		entityliving.worldObj.spawnEntityInWorld(entityliving);
		entityliving.func_110161_a((EntityLivingData)null);
	}
	
	public void autoAggro(EntityLiving entityliving, int radius) {
		EntityAIOnARampage rampageAI = new EntityAIOnARampage((EntityCreature)entityliving, EntityPlayer.class, radius);
		entityliving.targetTasks.addTask(1, rampageAI);
		// if only our AI is active, we assume that entity uses old AI system, hence we have to activate AI behaviour ourselves
		if (entityliving.targetTasks.taskEntries.size() == 1 && rampageAI.shouldExecute()) {
			rampageAI.startExecuting();
		}
		//entityliving.targetTasks.addTask(1, new EntityAINearestAttackableTarget((EntityCreature)entityliving, EntityPlayer.class, 0, false, false));
		//if (entityliving instanceof EntityPigZombie) {
			//entityliving.attackEntityFrom(DamageSource.causePlayerDamage(closestPlayer), 0f); // FIXME MOBS STICK IN FLOOR AFTER CALLING THIS?!
		//}
	}
	
}
