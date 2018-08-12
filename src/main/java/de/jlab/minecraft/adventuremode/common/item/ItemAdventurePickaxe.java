package de.jlab.minecraft.adventuremode.common.item;

import de.jlab.minecraft.adventuremode.utils.Easing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemAdventurePickaxe extends ItemPickaxe {

	public ItemAdventurePickaxe(Item.ToolMaterial material) {
		super(material);
		this.setMaxDamage(material.getMaxUses() * 3);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float result = super.getDestroySpeed(stack, state);
                
        return getDamage(stack) < 1 ? result : result * Easing.circular((float)getDamage(stack), (float)getMaxDamage(stack)+1);
    }
		
}
