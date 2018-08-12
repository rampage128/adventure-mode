package de.jlab.minecraft.adventuremode.utils;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySpawnHelper {

	private RandomRange randomRange = new RandomRange();
	private ChanceCalculator chanceCalculator = new ChanceCalculator();
	
	public Entity getRandomEntity(HashMap<String, Double> entities, String defaultEntityName, World world) {
		Entity entity = null;
				
		for (String name : entities.keySet()) {
			double chance 	= entities.get(name); 
			if (chanceCalculator.calculateChance(chance)) {
				entity = EntityList.createEntityByIDFromName(new ResourceLocation(name), world);
				break;
			}
		}
		
		// get default entity if no other could be spawned
		if (entity == null && defaultEntityName != null) {
			return EntityList.createEntityByIDFromName(new ResourceLocation(defaultEntityName), world);
		}
		
		return entity;
	}
	
	public void setRandomCircleSpawnPosition(Entity entity, BlockPos center, int minRadius, int maxRadius) {
		World world = entity.getEntityWorld();
		
	    // create random angle between 0 to 360 degrees
		int radius = minRadius;
		if (minRadius != maxRadius) {
			radius = randomRange.range(minRadius, maxRadius);
		}
	    int ang = randomRange.range(0, 360);
	    
	    // Set position & orientation	    
	    double sx = center.getX() + radius * Math.sin(Math.toRadians(ang));
	    double sz = center.getZ() + radius * Math.cos(Math.toRadians(ang));
	    double sy = world.getTopSolidOrLiquidBlock(new BlockPos((int)sx, 0, (int)sz)).getY() + 1;
	    entity.setLocationAndAngles(sx, sy, sz, world.rand.nextFloat() * 360.0F, 0.0F);
	}
	
	public void setSpawnPosition(Entity entity, BlockPos position, int spawnRadius) {
		World world = entity.getEntityWorld();
		
		double sx = (double)position.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)spawnRadius;
        double sz = (double)position.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)spawnRadius;
        double sy = world.getTopSolidOrLiquidBlock(new BlockPos((int)sx, 0, (int)sz)).getY() + 1;
        entity.setLocationAndAngles(sx, sy, sz, world.rand.nextFloat() * 360.0F, 0.0F);
	}
	
	public void spawnEntityLiving(EntityLiving entityliving) {
		entityliving.spawnExplosionParticle();
		entityliving.getEntityWorld().spawnEntity(entityliving);
		entityliving.onInitialSpawn(entityliving.getEntityWorld().getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
		entityliving.playLivingSound();
	}
	
	public void autoAggro(EntityLiving entityliving, int radius) {
		entityliving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(radius);
		EntityPlayer player = entityliving.getEntityWorld().getClosestPlayer(entityliving.posX, entityliving.posY, entityliving.posZ, radius, EntitySelectors.IS_ALIVE);
		if (player != null) {
			entityliving.setAttackTarget(player);
		}
	}
	
}
