package de.jlab.minecraft.mods.adventuremode.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemAdventureAxe extends ItemAxe {

	public ItemAdventureAxe(int i, EnumToolMaterial enumToolMaterial) {
		super(i, enumToolMaterial);
		this.setMaxDamage(enumToolMaterial.getMaxUses() * 3);
	}

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
	@Override
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
        float result = super.getStrVsBlock(par1ItemStack, par2Block);
                
        return getDamage(par1ItemStack) < 1 ? result : result * ItemHandler.easeCircular((float)getDamage(par1ItemStack), (float)getMaxDamage(par1ItemStack)+1);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(AdventureMode.MODID.toLowerCase() + ":" + getUnlocalizedName().replace("item.", "").toLowerCase());
    }
	
}
