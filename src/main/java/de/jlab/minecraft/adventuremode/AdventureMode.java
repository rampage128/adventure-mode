package de.jlab.minecraft.adventuremode;

import org.apache.logging.log4j.Logger;

import de.jlab.minecraft.adventuremode.common.commands.EventCommand;
import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.event.EventScheduler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = AdventureMode.MODID, name = AdventureMode.NAME, version = AdventureMode.VERSION)
//@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class AdventureMode {
	
	public static final String MODID = "adventuremode";
    public static final String NAME = "Adventure Mode";
    public static final String VERSION = "0.3.4";
    
    public static Logger logger;
    
    @Instance(MODID)
    public static AdventureMode instance;
       
    private EventScheduler eventScheduler = new EventScheduler(); 
    
    @SidedProxy(clientSide="de.jlab.minecraft.adventuremode.client.ClientProxy", serverSide="de.jlab.minecraft.adventuremode.server.ServerProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    public EventScheduler getEventScheduler() {
    	return this.eventScheduler;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	ConfigManager.sync(MODID, Config.Type.INSTANCE);
    	this.eventScheduler.updateConfig();
    	proxy.init(event);
    	MinecraftForge.EVENT_BUS.register(AdventureConfig.class);
    }
         
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	event.registerServerCommand(new EventCommand());
    	proxy.serverStarting(event);
    }
        
}
