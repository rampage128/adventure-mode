package de.jlab.minecraft.adventuremode.utils;

import java.util.HashMap;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.world.chunk.Chunk;

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
	
	public BlockPos getRandomCircleSpawnPosition(World world, BlockPos center, int minRadius, int maxRadius, int desiredHeadroom) {	
	    // create random angle between 0 to 360 degrees
		int radius = minRadius;
		if (minRadius != maxRadius) {
			radius = randomRange.range(minRadius, maxRadius);
		}
	    int ang = randomRange.range(0, 360);
	    
	    // Set position & orientation	    
	    double rx = radius * Math.sin(Math.toRadians(ang));
	    double rz = radius * Math.cos(Math.toRadians(ang));
	    BlockPos spawnPos = center.add(rx, 0, rz);
	    
	    return this.getVerticalSpawnPosition(spawnPos, world, desiredHeadroom);
	}
	
	public void spawnEntityLiving(EntityLiving entityliving, BlockPos position) {
		entityliving.moveToBlockPosAndAngles(position, entityliving.getEntityWorld().rand.nextFloat() * 360.0F, 0.0F);
		entityliving.spawnExplosionParticle();
		entityliving.getEntityWorld().spawnEntity(entityliving);
		entityliving.onInitialSpawn(entityliving.getEntityWorld().getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
		entityliving.playLivingSound();
	}
	
	private BlockPos getVerticalSpawnPosition(BlockPos source, World world, int desiredHeadroom) {	
		Chunk chunk = world.getChunkFromBlockCoords(source);
		
		if (!chunk.isLoaded()) {
			return null;
		}
		
		boolean hasHitGround = false;
		int headroom = 0;
		BlockPos spawnPos = null;
				
		for (BlockPos pos = source; pos.getY() < world.getHeight(); pos = hasHitGround ? pos.up() : pos.down()) {
			IBlockState state = chunk.getBlockState(pos);
			if (state.getMaterial().isSolid() || state.getMaterial().isLiquid()) {
				spawnPos = pos;
				headroom = 0;
				if (state.getMaterial() != Material.LEAVES) {
					hasHitGround = true;
				}
			}
			else {
				headroom++;
				if (hasHitGround && headroom >= desiredHeadroom) {
					return spawnPos.up();
				}
			}
		}
				
		return null;
	}
	
	public void autoAggro(EntityLiving entityliving, int radius) {
		entityliving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(radius);
		EntityPlayer player = entityliving.getEntityWorld().getClosestPlayer(entityliving.posX, entityliving.posY, entityliving.posZ, radius, EntitySelectors.IS_ALIVE);
		if (player != null) {
			entityliving.setAttackTarget(player);
		}
	}
	
}
