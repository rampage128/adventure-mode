package de.jlab.minecraft.mods.adventuremode;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import de.jlab.minecraft.mods.adventuremode.network.ExtendedPacket;
import de.jlab.minecraft.mods.adventuremode.network.ExtendedPacket.ProtocolException;
import de.jlab.minecraft.mods.adventuremode.network.PacketAM001EventStarted;

public class PacketHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			EntityPlayer entityPlayer = (EntityPlayer)player;
			ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
			int packetId = in.readUnsignedByte(); // Assuming your packetId is between 0 (inclusive) and 256 (exclusive). If you need more you need to change this
			ExtendedPacket demoPacket = ExtendedPacket.constructPacket(packetId);
	        demoPacket.read(in);
	        demoPacket.execute(entityPlayer, entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
		} catch (ProtocolException e) {
	        if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Protocol Exception!");
                AdventureMode.log.warning("Player " + ((EntityPlayer)player).username + " caused a Protocol Exception!");
	        }
		} catch (ReflectiveOperationException e) {
	        throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
		}
		/*
	    if (packet.channel.equals(ExtendedPacket.CHANNEL)) {
			handleEvents(packet, player);
        }
        */
	}
	
	private void handleEvents(Packet250CustomPayload packet, Player player) {
		/*
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
                
        try {
            AdventureMode.instance.getEventHandler().setClientAffectingEvents((EntityPlayer)player, Packet.readString(inputStream, 32767));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        */
	}

}
