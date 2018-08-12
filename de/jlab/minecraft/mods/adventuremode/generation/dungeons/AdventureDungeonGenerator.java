package de.jlab.minecraft.mods.adventuremode.generation.dungeons;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.IWorldGenerator;

public class AdventureDungeonGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		if (world.provider.dimensionId != 0) {
			return;
		}
		
		
	}

}
