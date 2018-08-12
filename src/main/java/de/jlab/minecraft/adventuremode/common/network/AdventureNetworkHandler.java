package de.jlab.minecraft.adventuremode.common.network;

import de.jlab.minecraft.adventuremode.AdventureMode;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class AdventureNetworkHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AdventureMode.MODID);

}
