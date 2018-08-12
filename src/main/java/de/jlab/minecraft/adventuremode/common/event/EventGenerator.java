package de.jlab.minecraft.mods.adventuremode.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import de.jlab.minecraft.mods.adventuremode.network.PacketAM001EventStarted;
import de.jlab.minecraft.mods.adventuremode.utils.ConfigStore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.Configuration;

public abstract class EventGenerator {

	private ConfigStore configstore;
	
	/**
	 * Creates an event around the given player
	 * @param player
	 * @return
	 */
	public abstract Event createEvent();
	
	/**
	 * Calculates the general chance for an event to occur
	 * @param player
	 * @return
	 */
	public abstract double getChance(EntityPlayer player);
	
	public final void readConfig(Configuration config) {
		this.configstore = this.createConfigStore(config);
		this.configstore.readConfig();
	}
	
	public final ConfigStore getConfigStore() {
		return this.configstore;
	}
	
	public final void startEvent(Event event, EntityPlayer player) {
		FMLRelaunchLog.info("Starting event [" + event.getClass().getSimpleName() + "] at player [" + player.username + "]");
		event.init(player);
		event.start();
	}
	
	protected abstract void onEventStarted(Event event, EntityPlayer player);
	
	protected abstract ConfigStore createConfigStore(Configuration config);
	
	public abstract String getType();
	
	public abstract void reset();
	
}
