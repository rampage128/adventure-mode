package de.jlab.minecraft.mods.adventuremode;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import de.jlab.minecraft.mods.adventuremode.client.renderer.entity.RenderSpiderBoss;
import de.jlab.minecraft.mods.adventuremode.commands.EventCommand;
import de.jlab.minecraft.mods.adventuremode.entity.monster.EntitySpiderBoss;
import de.jlab.minecraft.mods.adventuremode.event.EventHandlerConfigStore;
import de.jlab.minecraft.mods.adventuremode.event.invasion.InvasionEventConfigStore;
import de.jlab.minecraft.mods.adventuremode.item.ItemAdventurePickaxe;
import de.jlab.minecraft.mods.adventuremode.item.ItemConfigStore;
import de.jlab.minecraft.mods.adventuremode.item.ItemHandler;
import de.jlab.minecraft.mods.adventuremode.network.ExtendedPacket;
// used in 1.6.2
//import cpw.mods.fml.common.Mod.PreInit;    // used in 1.5.2
//import cpw.mods.fml.common.Mod.Init;       // used in 1.5.2
//import cpw.mods.fml.common.Mod.PostInit;   // used in 1.5.2

@Mod(modid=AdventureMode.MODID, name="Adventure Mode", version="0.3.3")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, 
	channels={ExtendedPacket.CHANNEL}, packetHandler = PacketHandler.class)
public class AdventureMode {
	
	public static final Logger log = Logger.getLogger("AdventureMode");

	public static final String MODID = "AdventureMode";
	
	//public static final String CONFIG_CATEGORY_INVASION = "invasion";
	
    // The instance of your mod that Forge uses.
    @Instance(AdventureMode.MODID)
    public static AdventureMode instance;
    
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="de.jlab.minecraft.mods.adventuremode.client.ClientProxy", serverSide="de.jlab.minecraft.mods.adventuremode.CommonProxy")
    public static CommonProxy proxy;
    
    
	//private static boolean enableInvasions;		
	//private InvasionHandler invasionHandler = new InvasionHandler();
	private de.jlab.minecraft.mods.adventuremode.event.EventHandler eventHandler = new de.jlab.minecraft.mods.adventuremode.event.EventHandler();
	
	private ItemHandler itemHandler = new ItemHandler();
    
    @EventHandler // used in 1.6.2
    //@PreInit    // used in 1.5.2
    public void preInit(FMLPreInitializationEvent event) {
    	// you will be able to find the config file in .minecraft/config/ and it will be named Dummy.cfg
        // here our Configuration has been instantiated, and saved under the name "config"
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        // loading the configuration from its file
        config.load();
        // check invasions
        //Property enableInvasions = config.get(InvasionEventConfigStore.CATEGORY, "invasions_enabled", true);
        //enableInvasions.comment = "Enable or disable monster invasions";
        //this.enableInvasions = enableInvasions.getBoolean(true);
        // always create instance of InvasionHandler to init the config
        //this.invasionHandler.readConfig(config);
    	this.eventHandler.readConfig(config);
    	
    	this.itemHandler.initTools(config);
    	
        // saving the configuration to its file
        config.save();
        
    }
        
    @EventHandler // used in 1.6.2
    //@Init       // used in 1.5.2
    public void load(FMLInitializationEvent event) {
    	//int id = EntityRegistry.findGlobalUniqueEntityId();
    	//EntityRegistry.registerGlobalEntityID(EntitySpiderBoss.class, "SpiderBoss", id, 0xFFFFFF, 0x000000);
    	    	    	
        proxy.registerRenderers();
    }
    
    @EventHandler // used in 1.6.2
    //@PostInit   // used in 1.5.2
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
    
    @EventHandler 	   // used in 1.6.2
    //@ServerStarted   // used in 1.5.2
    public void serverStarting(FMLServerStartingEvent event) {
    	// only register InvasionHandler if invasions are enabled!
    	//if (this.enableInvasions) {
    		//TickRegistry.registerTickHandler(this.invasionHandler, Side.SERVER);
    		TickRegistry.registerTickHandler(this.eventHandler, Side.SERVER);
    	//}
    	
    	//event.registerServerCommand(new InvasionCommand());
    	//event.registerServerCommand(new StopInvasionCommand());
    	

    	//event.registerServerCommand(new StartEventCommand());
    	//event.registerServerCommand(new StopEventCommand());
    	event.registerServerCommand(new EventCommand());
    	
    	GameRegistry.registerPlayerTracker(new PlayerTracker());
    }
    
    @EventHandler 		// used in 1.6.2
    //@ServerStopped   	// used in 1.5.2
    public void serverStopped(FMLServerStoppedEvent event) {
    	//this.invasionHandler.reset();
    	this.eventHandler.reset();
    }
    
    public de.jlab.minecraft.mods.adventuremode.event.EventHandler getEventHandler() {
    	return this.eventHandler;
    }
    
}