package de.jlab.minecraft.adventuremode.common.init;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.item.ItemAdventureAxe;
import de.jlab.minecraft.adventuremode.common.item.ItemAdventureHoe;
import de.jlab.minecraft.adventuremode.common.item.ItemAdventurePickaxe;
import de.jlab.minecraft.adventuremode.common.item.ItemAdventureShovel;
import de.jlab.minecraft.adventuremode.common.item.ItemAdventureSledge;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ModItems {

	@ObjectHolder("minecraft")
	public static class VanillaItems {
		public static final Item wooden_axe = null;
		public static final Item stone_axe = null;
		public static final Item iron_axe = null;
		public static final Item golden_axe = null;
		public static final Item diamond_axe = null;
		
		public static final Item wooden_hoe = null;
		public static final Item stone_hoe = null;
		public static final Item iron_hoe = null;
		public static final Item golden_hoe = null;
		public static final Item diamond_hoe = null;
			
		public static final Item wooden_pickaxe = null;
		public static final Item stone_pickaxe = null;
		public static final Item iron_pickaxe = null;
		public static final Item golden_pickaxe = null;
		public static final Item diamond_pickaxe = null;
		
		public static final Item wooden_shovel = null;
		public static final Item stone_shovel = null;
		public static final Item iron_shovel = null;
		public static final Item golden_shovel = null;
		public static final Item diamond_shovel = null;
	}
	
	@ObjectHolder(AdventureMode.MODID)
	public static class AdventureItems {
		public static final Item wooden_sledgehammer = null;
		public static final Item stone_sledgehammer = null;
		public static final Item iron_sledgehammer = null;
		public static final Item golden_sledgehammer = null;
		public static final Item diamond_sledgehammer = null;
	}
		
	@Mod.EventBusSubscriber(modid=AdventureMode.MODID)
	public static class RegistrationHandler {
	    @SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			event.getRegistry().registerAll(
				nameItem(new ItemAdventureAxe(ToolMaterial.WOOD), "minecraft:wooden_axe"),
				nameItem(new ItemAdventureAxe(ToolMaterial.STONE), "minecraft:stone_axe"),
				nameItem(new ItemAdventureAxe(ToolMaterial.IRON), "minecraft:iron_axe"),
				nameItem(new ItemAdventureAxe(ToolMaterial.GOLD), "minecraft:golden_axe"),
				nameItem(new ItemAdventureAxe(ToolMaterial.DIAMOND), "minecraft:diamond_axe"),
				
				nameItem(new ItemAdventureHoe(ToolMaterial.WOOD), "minecraft:wooden_hoe"),
				nameItem(new ItemAdventureHoe(ToolMaterial.STONE), "minecraft:stone_hoe"),
				nameItem(new ItemAdventureHoe(ToolMaterial.IRON), "minecraft:iron_hoe"),
				nameItem(new ItemAdventureHoe(ToolMaterial.GOLD), "minecraft:golden_hoe"),
				nameItem(new ItemAdventureHoe(ToolMaterial.DIAMOND), "minecraft:diamond_hoe"),
				
				nameItem(new ItemAdventurePickaxe(ToolMaterial.WOOD), "minecraft:wooden_pickaxe"),
				nameItem(new ItemAdventurePickaxe(ToolMaterial.STONE), "minecraft:stone_pickaxe"),
				nameItem(new ItemAdventurePickaxe(ToolMaterial.IRON), "minecraft:iron_pickaxe"),
				nameItem(new ItemAdventurePickaxe(ToolMaterial.GOLD), "minecraft:golden_pickaxe"),
				nameItem(new ItemAdventurePickaxe(ToolMaterial.DIAMOND), "minecraft:diamond_pickaxe"),
				
				nameItem(new ItemAdventureShovel(ToolMaterial.WOOD), "minecraft:wooden_shovel"),
				nameItem(new ItemAdventureShovel(ToolMaterial.STONE), "minecraft:stone_shovel"),
				nameItem(new ItemAdventureShovel(ToolMaterial.IRON), "minecraft:iron_shovel"),
				nameItem(new ItemAdventureShovel(ToolMaterial.GOLD), "minecraft:golden_shovel"),
				nameItem(new ItemAdventureShovel(ToolMaterial.DIAMOND), "minecraft:diamond_shovel"),
				
				nameItem(new ItemAdventureSledge(ToolMaterial.WOOD), "wooden_sledgehammer"), 
				nameItem(new ItemAdventureSledge(ToolMaterial.STONE), "stone_sledgehammer"), 
				nameItem(new ItemAdventureSledge(ToolMaterial.IRON), "iron_sledgehammer"), 
				nameItem(new ItemAdventureSledge(ToolMaterial.GOLD), "golden_sledgehammer"), 
				nameItem(new ItemAdventureSledge(ToolMaterial.DIAMOND), "diamond_sledgehammer")
			);
		}
	    
	    @SubscribeEvent
	    @SideOnly(Side.CLIENT)
		public static void registerModels(ModelRegistryEvent event) {
			registerModel(AdventureItems.wooden_sledgehammer);
			registerModel(AdventureItems.stone_sledgehammer);
			registerModel(AdventureItems.iron_sledgehammer);
			registerModel(AdventureItems.golden_sledgehammer);
			registerModel(AdventureItems.diamond_sledgehammer);
		}
	    
	    private static Item nameItem(Item item, String name) {
	    	item.setRegistryName(name);
	    	item.setUnlocalizedName(name);
	    	return item;
	    }
	    
	    private static void registerModel(Item item) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}
