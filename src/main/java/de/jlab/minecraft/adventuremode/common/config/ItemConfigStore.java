package de.jlab.minecraft.mods.adventuremode.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;

public class ItemConfigStore extends ConfigStore {

	public static final String PROPERTY_REPLACETOOLS = "replace_tools";
	
	public static final String PROPERTY_ID_PREFIX = "itemid_";
	
	public static final String PROPERTY_ID_PICKAXEWOOD 		= PROPERTY_ID_PREFIX + "pickaxe_wood";
	public static final String PROPERTY_ID_PICKAXESTONE 	= PROPERTY_ID_PREFIX + "pickaxe_stone";
	public static final String PROPERTY_ID_PICKAXEIRON 		= PROPERTY_ID_PREFIX + "pickaxe_iron";
	public static final String PROPERTY_ID_PICKAXEGOLD 		= PROPERTY_ID_PREFIX + "pickaxe_gold";
	public static final String PROPERTY_ID_PICKAXEDIAMOND 	= PROPERTY_ID_PREFIX + "pickaxe_diamond";
	
	public static final String PROPERTY_ID_HOEWOOD 		= PROPERTY_ID_PREFIX + "hoe_wood";
	public static final String PROPERTY_ID_HOESTONE 	= PROPERTY_ID_PREFIX + "hoe_stone";
	public static final String PROPERTY_ID_HOEIRON 		= PROPERTY_ID_PREFIX + "hoe_iron";
	public static final String PROPERTY_ID_HOEGOLD 		= PROPERTY_ID_PREFIX + "hoe_gold";
	public static final String PROPERTY_ID_HOEDIAMOND 	= PROPERTY_ID_PREFIX + "hoe_diamond";
	
	public static final String PROPERTY_ID_AXEWOOD 		= PROPERTY_ID_PREFIX + "axe_wood";
	public static final String PROPERTY_ID_AXESTONE 	= PROPERTY_ID_PREFIX + "axe_stone";
	public static final String PROPERTY_ID_AXEIRON 		= PROPERTY_ID_PREFIX + "axe_iron";
	public static final String PROPERTY_ID_AXEGOLD 		= PROPERTY_ID_PREFIX + "axe_gold";
	public static final String PROPERTY_ID_AXEDIAMOND 	= PROPERTY_ID_PREFIX + "axe_diamond";
	
	public static final String PROPERTY_ID_SHOVELWOOD 		= PROPERTY_ID_PREFIX + "shovel_wood";
	public static final String PROPERTY_ID_SHOVELSTONE 		= PROPERTY_ID_PREFIX + "shovel_stone";
	public static final String PROPERTY_ID_SHOVELIRON 		= PROPERTY_ID_PREFIX + "shovel_iron";
	public static final String PROPERTY_ID_SHOVELGOLD 		= PROPERTY_ID_PREFIX + "shovel_gold";
	public static final String PROPERTY_ID_SHOVELDIAMOND 	= PROPERTY_ID_PREFIX + "shovel_diamond";
	
	public static final String PROPERTY_ID_SLEDGEWOOD 		= PROPERTY_ID_PREFIX + "sledge_wood";
	public static final String PROPERTY_ID_SLEDGESTONE 		= PROPERTY_ID_PREFIX + "sledge_stone";
	public static final String PROPERTY_ID_SLEDGEIRON 		= PROPERTY_ID_PREFIX + "sledge_iron";
	public static final String PROPERTY_ID_SLEDGEGOLD 		= PROPERTY_ID_PREFIX + "sledge_gold";
	public static final String PROPERTY_ID_SLEDGEDIAMOND 	= PROPERTY_ID_PREFIX + "sledge_diamond";
		
	public ItemConfigStore(Configuration config) {
		super(config);
	}

	@Override
	public void readConfig() {
		this.readProperty(getDefaultCategory(), PROPERTY_REPLACETOOLS, true, "Determines if vanilla tools will be replaced with adventure tools (boolean)");
		
    	this.readItemId(PROPERTY_ID_PICKAXEWOOD, 14, "Item id of the adventure wood pickaxe");
    	this.readItemId(PROPERTY_ID_PICKAXESTONE, 18, "Item id of the adventure stone pickaxe");
    	this.readItemId(PROPERTY_ID_PICKAXEIRON, 1, "Item id of the adventure iron pickaxe");
    	this.readItemId(PROPERTY_ID_PICKAXEGOLD, 29, "Item id of the adventure gold pickaxe");
    	this.readItemId(PROPERTY_ID_PICKAXEDIAMOND, 22, "Item id of the adventure diamond pickaxe");
    	
    	this.readItemId(PROPERTY_ID_HOEWOOD, 34, "Item id of the adventure wood hoe");
    	this.readItemId(PROPERTY_ID_HOESTONE, 35, "Item id of the adventure stone hoe");
    	this.readItemId(PROPERTY_ID_HOEIRON, 36, "Item id of the adventure iron hoe");
    	this.readItemId(PROPERTY_ID_HOEGOLD, 38, "Item id of the adventure gold hoe");
    	this.readItemId(PROPERTY_ID_HOEDIAMOND, 37, "Item id of the adventure diamond hoe");
    	
    	this.readItemId(PROPERTY_ID_AXEWOOD, 15, "Item id of the adventure wood axe");
    	this.readItemId(PROPERTY_ID_AXESTONE, 19, "Item id of the adventure stone axe");
    	this.readItemId(PROPERTY_ID_AXEIRON, 2, "Item id of the adventure iron axe");
    	this.readItemId(PROPERTY_ID_AXEGOLD, 30, "Item id of the adventure gold axe");
    	this.readItemId(PROPERTY_ID_AXEDIAMOND, 23, "Item id of the adventure diamond axe");
    	
    	this.readItemId(PROPERTY_ID_SHOVELWOOD, 13, "Item id of the adventure wood shovel");
    	this.readItemId(PROPERTY_ID_SHOVELSTONE, 17, "Item id of the adventure stone shovel");
    	this.readItemId(PROPERTY_ID_SHOVELIRON, 0, "Item id of the adventure iron shovel");
    	this.readItemId(PROPERTY_ID_SHOVELGOLD, 28, "Item id of the adventure gold shovel");
    	this.readItemId(PROPERTY_ID_SHOVELDIAMOND, 21, "Item id of the adventure diamond shovel");
    	
    	this.readItemId(PROPERTY_ID_SLEDGEWOOD, 34, "Item id of the adventure wood sledge");
    	this.readItemId(PROPERTY_ID_SLEDGESTONE, 35, "Item id of the adventure stone sledge");
    	this.readItemId(PROPERTY_ID_SLEDGEIRON, 36, "Item id of the adventure iron sledge");
    	this.readItemId(PROPERTY_ID_SLEDGEGOLD, 38, "Item id of the adventure gold sledge");
    	this.readItemId(PROPERTY_ID_SLEDGEDIAMOND, 37, "Item id of the adventure diamond sledge");
	}

	@Override
	public String getDefaultCategory() {
		return Configuration.CATEGORY_ITEM;
	}

}
