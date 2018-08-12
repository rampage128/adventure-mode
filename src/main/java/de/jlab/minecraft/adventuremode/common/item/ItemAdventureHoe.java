package de.jlab.minecraft.mods.adventuremode.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.utils.ChanceCalculator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAdventureHoe extends ItemHoe {

	private ChanceCalculator cc = new ChanceCalculator();
	
	public ItemAdventureHoe(int i, EnumToolMaterial enumToolMaterial) {
		super(i, enumToolMaterial);
		this.setMaxDamage(enumToolMaterial.getMaxUses() * 5);
	}

	/**
	 * Emulate worn out effect
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		// get dirt blockid
		int i1 = Block.dirt.blockID;		
		boolean tiled = super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
		
		// if chance is given fail the tiling of the block (turn it into dirt!)
		if (getDamage(par1ItemStack) > 0 && !cc.calculateChance(ItemHandler.easeCircular((float)getDamage(par1ItemStack), (float)getMaxDamage(par1ItemStack)+1) * 100f)) {
			par3World.setBlock(par4, par5, par6, i1);
			return false;
		}
		
		return tiled;
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
