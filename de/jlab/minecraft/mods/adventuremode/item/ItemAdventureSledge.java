package de.jlab.minecraft.mods.adventuremode.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.utils.ChanceCalculator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAdventureSledge extends ItemPickaxe {

	private ChanceCalculator cc = new ChanceCalculator();
	
	public ItemAdventureSledge(int i, EnumToolMaterial enumToolMaterial) {
		super(i, enumToolMaterial);
		this.setMaxDamage(enumToolMaterial.getMaxUses() / 6);
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
	
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
    	Block block = Block.blocksList[par3]; 
    	
    	for (int x = -1; x <= 1; x++) {
    		for (int y = -1; y <= 1; y++) {
    			for (int z = -1; z <= 1; z++) {
    				boolean chance = true; //!cc.calculateChance(ItemHandler.easeCircular((float)getDamage(par1ItemStack), (float)getMaxDamage(par1ItemStack)+1) * 100f);
    				Block adjacentBlock = Block.blocksList[par2World.getBlockId(par4 + x, par5 + y, par6 + z)];
    				if (chance && block.blockMaterial == Material.rock && adjacentBlock != null && adjacentBlock.blockHardness > 0.0f && adjacentBlock.blockMaterial == Material.rock && block.blockHardness >= adjacentBlock.blockHardness) {
						int i1 = par2World.getBlockMetadata(par4 + x, par5 + y, par6 + z);
						block.dropBlockAsItem(par2World, par4 + x, par5 + y, par6 + z, i1, 0);
						par2World.setBlockToAir(par4 + x, par5 + y, par6 + z);
    				}
    			}
    		}
    	}

    	return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLivingBase);
    }
	
}
