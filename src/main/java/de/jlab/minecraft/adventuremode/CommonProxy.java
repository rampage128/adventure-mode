package de.jlab.minecraft.adventuremode;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public abstract class CommonProxy {
    public abstract void init(FMLInitializationEvent event);
    
    public abstract void serverStarting(FMLServerStartingEvent event);
}
