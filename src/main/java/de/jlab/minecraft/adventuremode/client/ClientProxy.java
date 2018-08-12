package de.jlab.minecraft.mods.adventuremode.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import de.jlab.minecraft.mods.adventuremode.CommonProxy;
import de.jlab.minecraft.mods.adventuremode.client.renderer.entity.RenderSpiderBoss;
import de.jlab.minecraft.mods.adventuremode.entity.monster.EntitySpiderBoss;
import de.jlab.minecraft.mods.adventuremode.event.GuiEventIndicator;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
    	//RenderingRegistry.registerEntityRenderingHandler(EntitySpiderBoss.class, new RenderSpiderBoss());
    	
    	MinecraftForge.EVENT_BUS.register(new GuiEventIndicator(Minecraft.getMinecraft()));
    }
	
}
