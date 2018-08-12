package de.jlab.minecraft.adventuremode.client;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.CommonProxy;
import de.jlab.minecraft.adventuremode.client.event.DebugEventIndicator;
import de.jlab.minecraft.adventuremode.client.event.GuiEventIndicator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {   	
    	MinecraftForge.EVENT_BUS.register(new GuiEventIndicator(Minecraft.getMinecraft()));
    	MinecraftForge.EVENT_BUS.register(new DebugEventIndicator());
    }

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MinecraftForge.EVENT_BUS.register(AdventureMode.instance.getEventScheduler());
		}
	}
	
}
