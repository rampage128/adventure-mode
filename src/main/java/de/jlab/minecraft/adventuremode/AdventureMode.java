package de.jlab.minecraft.adventuremode;

import org.apache.logging.log4j.Logger;

import de.jlab.minecraft.adventuremode.common.commands.EventCommand;
import de.jlab.minecraft.adventuremode.common.event.EventScheduler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

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
    }
   
    @SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
            this.eventScheduler.updateConfig();
        }
    }
    
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
    	eventScheduler.onPlayerLogin((EntityPlayerMP)event.player);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	logger.info("STARTING SERVER");
    	event.registerServerCommand(new EventCommand());
    	proxy.serverStarting(event);
    }
        
}
