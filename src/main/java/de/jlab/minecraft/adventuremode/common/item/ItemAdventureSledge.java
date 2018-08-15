package de.jlab.minecraft.adventuremode.common.item;

import de.jlab.minecraft.adventuremode.utils.Easing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAdventureSledge extends ItemPickaxe {
	
	public ItemAdventureSledge(Item.ToolMaterial material) {
		super(material);
		this.setMaxDamage(material.getMaxUses() / 6);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float result = super.getDestroySpeed(stack, state);
                
        return getDamage(stack) < 1 ? result : result * Easing.circular((float)getDamage(stack), (float)getMaxDamage(stack)+1);
    }
	
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (!worldIn.isRemote) {
	    	for (int x = -1; x <= 1; x++) {
	    		for (int y = -1; y <= 1; y++) {
	    			for (int z = -1; z <= 1; z++) {   				
	    				BlockPos adjacentPos = pos.add(x, y, z);
	    				IBlockState adjacentState = worldIn.getBlockState(adjacentPos);
	    				if (state.getMaterial() == Material.ROCK && adjacentState != null && adjacentState.getBlockHardness(worldIn, adjacentPos) > 0.0f && adjacentState.getMaterial() == Material.ROCK && state.getBlockHardness(worldIn, pos) >= adjacentState.getBlockHardness(worldIn, adjacentPos)) {
	    					int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
	    					adjacentState.getBlock().dropBlockAsItem(worldIn, adjacentPos, adjacentState, fortune);
	    					worldIn.setBlockToAir(adjacentPos);
	    				}
	    			}
	    		}
	    	}
		}

    	return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
	
}
