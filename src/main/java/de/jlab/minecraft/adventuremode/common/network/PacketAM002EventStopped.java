package de.jlab.minecraft.mods.adventuremode.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import cpw.mods.fml.relauncher.Side;

import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.event.Event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;

@Deprecated
public class PacketAM002EventStopped extends PacketAMEvent {

	public PacketAM002EventStopped() {};
	
	public PacketAM002EventStopped(Event event) {
		super(event);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		AdventureMode.instance.getEventHandler().handlePacket(this);
	}

	
}