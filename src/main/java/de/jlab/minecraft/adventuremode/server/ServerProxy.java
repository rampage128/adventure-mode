package de.jlab.minecraft.adventuremode.server;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ServerProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {}

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	MinecraftForge.EVENT_BUS.register(AdventureMode.instance.getEventScheduler());
    }
    
}
