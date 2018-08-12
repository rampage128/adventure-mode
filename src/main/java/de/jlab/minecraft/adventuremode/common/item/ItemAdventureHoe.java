package de.jlab.minecraft.adventuremode.common.item;


import de.jlab.minecraft.adventuremode.utils.ChanceCalculator;
import de.jlab.minecraft.adventuremode.utils.Easing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAdventureHoe extends ItemHoe {

	private ChanceCalculator cc = new ChanceCalculator();
	
	public ItemAdventureHoe(Item.ToolMaterial material) {
		super(material);
		this.setMaxDamage(material.getMaxUses() * 5);
	}

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	ItemStack itemstack = player.getHeldItem(hand);
    		
		EnumActionResult originalResult = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		
		// if chance is given fail the tiling of the block (turn it into dirt!)
		if (getDamage(itemstack) > 0 && !cc.calculateChance(Easing.circular((float)getDamage(itemstack), (float)getMaxDamage(itemstack)+1) * 100f)) {
			if (!worldIn.isRemote) {
	            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 11);
			}
			return EnumActionResult.FAIL;
		}
		
		return originalResult;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float result = super.getDestroySpeed(stack, state);
                
        return getDamage(stack) < 1 ? result : result * Easing.circular((float)getDamage(stack), (float)getMaxDamage(stack)+1);
    }
		
}
