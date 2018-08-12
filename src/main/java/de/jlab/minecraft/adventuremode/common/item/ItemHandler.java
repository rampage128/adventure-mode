package de.jlab.minecraft.mods.adventuremode.item;

import java.util.ArrayList;
import java.util.Iterator;

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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;

public class ItemHandler {

	private ItemConfigStore itemConfigStore;
	
	public static Item adventurePickaxeWood;
	public static Item adventurePickaxeStone;
	public static Item adventurePickaxeIron;
	public static Item adventurePickaxeGold;
	public static Item adventurePickaxeDiamond;
	
	public static Item adventureAxeWood;
	public static Item adventureAxeStone;
	public static Item adventureAxeIron;
	public static Item adventureAxeGold;
	public static Item adventureAxeDiamond;
	
	public static Item adventureHoeWood;
	public static Item adventureHoeStone;
	public static Item adventureHoeIron;
	public static Item adventureHoeGold;
	public static Item adventureHoeDiamond;
	
	public static Item adventureShovelWood;
	public static Item adventureShovelStone;
	public static Item adventureShovelIron;
	public static Item adventureShovelGold;
	public static Item adventureShovelDiamond;
	
	public static Item adventureSledgeWood;
	public static Item adventureSledgeStone;
	public static Item adventureSledgeIron;
	public static Item adventureSledgeGold;
	public static Item adventureSledgeDiamond;
	
	public void initTools(Configuration config) {
		
		itemConfigStore = new ItemConfigStore(config);
				
    	ItemStack plankStack 	= new ItemStack(Block.planks);
    	ItemStack cobbleStack 	= new ItemStack(Block.cobblestone);
    	ItemStack ironStack 	= new ItemStack(Item.ingotIron);
    	ItemStack goldStack 	= new ItemStack(Item.ingotGold);
    	ItemStack diamondStack 	= new ItemStack(Item.diamond);
    	ItemStack stickStack 	= new ItemStack(Item.stick);
		
		if ((Boolean)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_REPLACETOOLS)) {
			// INIT PICKAXE
			this.adventurePickaxeWood = this.replaceTool(
					Item.pickaxeWood, 
					new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventurePickaxeWood"), 
					new Object[] { "xxx", " y ", " y ", 'x', plankStack, 'y', stickStack });
			this.adventurePickaxeStone = this.replaceTool(
					Item.pickaxeStone, 
					new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXESTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventurePickaxeStone"), 
					new Object[] { "xxx", " y ", " y ", 'x', cobbleStack, 'y', stickStack });			
			this.adventurePickaxeIron = this.replaceTool(
					Item.pickaxeIron, 
					new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEIRON), EnumToolMaterial.STONE).setUnlocalizedName("AdventurePickaxeIron"), 
					new Object[] { "xxx", " y ", " y ", 'x', ironStack, 'y', stickStack });
			this.adventurePickaxeGold = this.replaceTool(
					Item.pickaxeGold, 
					new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventurePickaxeGold"), 
					new Object[] { "xxx", " y ", " y ", 'x', goldStack, 'y', stickStack });
			this.adventurePickaxeDiamond = this.replaceTool(
					Item.pickaxeDiamond, 
					new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventurePickaxeDiamond"), 
					new Object[] { "xxx", " y ", " y ", 'x', diamondStack, 'y', stickStack });
			
			// INIT AXE
			this.adventureAxeWood = this.replaceTool(
					Item.axeWood, 
					new ItemAdventureAxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_AXEWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventureAxeWood"), 
					new Object[] { "xx ", "xy ", " y ", 'x', plankStack, 'y', stickStack });
			this.adventureAxeStone = this.replaceTool(
					Item.axeStone, 
					new ItemAdventureAxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_AXESTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventureAxeStone"), 
					new Object[] { "xx ", "xy ", " y ", 'x', cobbleStack, 'y', stickStack });			
			this.adventureAxeIron = this.replaceTool(
					Item.axeIron, 
					new ItemAdventureAxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_AXEIRON), EnumToolMaterial.STONE).setUnlocalizedName("AdventureAxeIron"), 
					new Object[] { "xx ", "xy ", " y ", 'x', ironStack, 'y', stickStack });
			this.adventureAxeGold = this.replaceTool(
					Item.axeGold, 
					new ItemAdventureAxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_AXEGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventureAxeGold"), 
					new Object[] { "xx ", "xy ", " y ", 'x', goldStack, 'y', stickStack });
			this.adventureAxeDiamond = this.replaceTool(
					Item.axeDiamond, 
					new ItemAdventureAxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_AXEDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventureAxeDiamond"), 
					new Object[] { "xx ", "xy ", " y ", 'x', diamondStack, 'y', stickStack });

			// INIT HOE
			this.adventureHoeWood = this.replaceTool(
					Item.hoeWood, 
					new ItemAdventureHoe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_HOEWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventureHoeWood"), 
					new Object[] { "xx ", " y ", " y ", 'x', plankStack, 'y', stickStack });
			this.adventureHoeStone = this.replaceTool(
					Item.hoeStone, 
					new ItemAdventureHoe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_HOESTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventureHoeStone"), 
					new Object[] { "xx ", " y ", " y ", 'x', cobbleStack, 'y', stickStack });			
			this.adventureHoeIron = this.replaceTool(
					Item.hoeIron, 
					new ItemAdventureHoe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_HOEIRON), EnumToolMaterial.STONE).setUnlocalizedName("AdventureHoeIron"), 
					new Object[] { "xx ", " y ", " y ", 'x', ironStack, 'y', stickStack });
			this.adventureHoeGold = this.replaceTool(
					Item.hoeGold, 
					new ItemAdventureHoe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_HOEGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventureHoeGold"), 
					new Object[] { "xx ", " y ", " y ", 'x', goldStack, 'y', stickStack });
			this.adventureHoeDiamond = this.replaceTool(
					Item.hoeDiamond, 
					new ItemAdventureHoe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_HOEDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventureHoeDiamond"), 
					new Object[] { "xx ", " y ", " y ", 'x', diamondStack, 'y', stickStack });
			
			// INIT SHOVEL
			this.adventureShovelWood = this.replaceTool(
					Item.shovelWood, 
					new ItemAdventureShovel((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SHOVELWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventureShovelWood"), 
					new Object[] { " x ", " y ", " y ", 'x', plankStack, 'y', stickStack });
			this.adventureShovelStone = this.replaceTool(
					Item.shovelStone, 
					new ItemAdventureShovel((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SHOVELSTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventureShovelStone"), 
					new Object[] { " x ", " y ", " y ", 'x', cobbleStack, 'y', stickStack });			
			this.adventureShovelIron = this.replaceTool(
					Item.shovelIron, 
					new ItemAdventureShovel((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SHOVELIRON), EnumToolMaterial.STONE).setUnlocalizedName("AdventureShovelIron"), 
					new Object[] { " x ", " y ", " y ", 'x', ironStack, 'y', stickStack });
			this.adventureShovelGold = this.replaceTool(
					Item.shovelGold, 
					new ItemAdventureShovel((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SHOVELGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventureShovelGold"), 
					new Object[] { " x ", " y ", " y ", 'x', goldStack, 'y', stickStack });
			this.adventureShovelDiamond = this.replaceTool(
					Item.shovelDiamond, 
					new ItemAdventureShovel((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SHOVELDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventureShovelDiamond"), 
					new Object[] { " x ", " y ", " y ", 'x', diamondStack, 'y', stickStack });
			
			// INIT SLEDGE
			this.adventureSledgeWood = this.createTool(
					new ItemAdventureSledge((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SLEDGEWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventureSledgeWood"), 
					new Object[] { "xxx", "xyx", " y ", 'x', plankStack, 'y', stickStack });
			this.adventureSledgeStone = this.createTool(
					new ItemAdventureSledge((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SLEDGESTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventureSledgeStone"), 
					new Object[] { "xxx", "xyx", " y ", 'x', cobbleStack, 'y', stickStack });			
			this.adventureSledgeIron = this.createTool(
					new ItemAdventureSledge((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SLEDGEIRON), EnumToolMaterial.STONE).setUnlocalizedName("AdventureSledgeIron"), 
					new Object[] { "xxx", "xyx", " y ", 'x', ironStack, 'y', stickStack });
			this.adventureSledgeGold = this.createTool(
					new ItemAdventureSledge((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SLEDGEGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventureSledgeGold"), 
					new Object[] { "xxx", "xyx", " y ", 'x', goldStack, 'y', stickStack });
			this.adventureSledgeDiamond = this.createTool(
					new ItemAdventureSledge((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_SLEDGEDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventureSledgeDiamond"), 
					new Object[] { "xxx", "xyx", " y ", 'x', diamondStack, 'y', stickStack });
						
			/* OLD
	        adventurePickaxeWood 	= new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEWOOD), EnumToolMaterial.WOOD).setUnlocalizedName("AdventurePickaxeWood");
	        ItemStack pickaxeWood 		= new ItemStack(adventurePickaxeWood);
	    	ItemConfigStore.removeRecipe(new ItemStack(Item.pickaxeWood));
	    	GameRegistry.addRecipe(pickaxeWood, "xxx", " y ", " y ",
					'x', plankStack, 'y', stickStack);
	    	
			adventurePickaxeStone 	= new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXESTONE), EnumToolMaterial.STONE).setUnlocalizedName("AdventurePickaxeStone");
			ItemStack pickaxeStone = new ItemStack(adventurePickaxeStone);
	    	ItemConfigStore.removeRecipe(new ItemStack(Item.pickaxeStone));
	    	GameRegistry.addRecipe(pickaxeStone, "xxx", " y ", " y ",
					'x', cobbleStack, 'y', stickStack);
	    	
			adventurePickaxeIron 	= new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEIRON), EnumToolMaterial.IRON).setUnlocalizedName("AdventurePickaxeIron");
			ItemStack pickaxeIron = new ItemStack(adventurePickaxeIron);
	    	ItemConfigStore.removeRecipe(new ItemStack(Item.pickaxeIron));
	    	GameRegistry.addRecipe(pickaxeIron, "xxx", " y ", " y ",
					'x', ironStack, 'y', stickStack);
	    	
			adventurePickaxeGold 	= new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEGOLD), EnumToolMaterial.GOLD).setUnlocalizedName("AdventurePickaxeGold");
			ItemStack pickaxeGold = new ItemStack(adventurePickaxeGold);
	    	ItemConfigStore.removeRecipe(new ItemStack(Item.pickaxeGold));
	    	GameRegistry.addRecipe(pickaxeGold, "xxx", " y ", " y ",
					'x', goldStack, 'y', stickStack);
	    	
			adventurePickaxeDiamond = new ItemAdventurePickaxe((Integer)itemConfigStore.getProperty(ItemConfigStore.PROPERTY_ID_PICKAXEDIAMOND), EnumToolMaterial.EMERALD).setUnlocalizedName("AdventurePickaxeDiamond");
			ItemStack pickaxeDiamond = new ItemStack(adventurePickaxeDiamond);
			ItemConfigStore.removeRecipe(new ItemStack(Item.pickaxeDiamond));
	    	GameRegistry.addRecipe(pickaxeDiamond, "xxx", " y ", " y ",
					'x', diamondStack, 'y', stickStack);
			*/
		}
	}
	
	private Item createTool(Item newTool, Object[] recipe) {
		GameRegistry.addRecipe(new ItemStack(newTool), recipe);
    	return newTool;
	}
	
	private Item replaceTool(Item vanillaTool, Item newTool, Object[] recipe) {
		removeRecipe(new ItemStack(vanillaTool));
    	GameRegistry.addRecipe(new ItemStack(newTool), recipe);    	
    	return newTool;
	}
	
	public static float easeCircular(float damage, float maxDamage) {
		damage /= maxDamage;
		return (float)(Math.sqrt(1 - damage*damage));
	}
	
	/*
	public static void replaceRecipe(ItemStack sourceItem, IRecipe recipe) {
		ArrayList recipes = (ArrayList)CraftingManager.getInstance().getRecipeList();

	    for (int i = 0; i < recipes.size(); i++) {
	    	IRecipe tmpRecipe = (IRecipe) recipes.get(i);
	    	if (ItemStack.areItemStacksEqual(tmpRecipe.getRecipeOutput(), sourceItem)) {
	    		if (tmpRecipe instanceof ShapedRecipes) {
	    			AdventureMode.log.warning(AdventureMode.MODID + " replacing recipe: " + tmpRecipe + ", " + tmpRecipe.getRecipeOutput() + " -> " + newItem);
	    			ShapedOreRecipe replacement = new ShapedOreRecipe(tmpRecipe, new HashMap<ItemStack, String>());
	    			recipes.set(i, replacement);
	    		}
	    	}
	    }
	}
	*/
	
	public static void removeRecipe(ItemStack resultItem) {
	    ItemStack recipeResult = null;
	    ArrayList recipes = (ArrayList)CraftingManager.getInstance().getRecipeList();

	    for (Iterator recipeIterator = recipes.iterator(); recipeIterator.hasNext(); ) {
	        IRecipe tmpRecipe = (IRecipe) recipeIterator.next();
	        if (tmpRecipe instanceof ShapedRecipes) {
	            ShapedRecipes recipe = (ShapedRecipes)tmpRecipe;
	            recipeResult = recipe.getRecipeOutput();
	        }

	        if (tmpRecipe instanceof ShapelessRecipes) {
	            ShapelessRecipes recipe = (ShapelessRecipes)tmpRecipe;
	            recipeResult = recipe.getRecipeOutput();
	        }
	        
	        if (tmpRecipe instanceof ShapedOreRecipe) {
	        	ShapedOreRecipe recipe = (ShapedOreRecipe)tmpRecipe;
	            recipeResult = recipe.getRecipeOutput();
	        }

	        if (ItemStack.areItemStacksEqual(resultItem, recipeResult)) {
	            AdventureMode.log.warning(AdventureMode.MODID + " removed recipe: " + tmpRecipe + " -> " + recipeResult);
	            recipeIterator.remove();
	        }
	    }
	}
	
}
