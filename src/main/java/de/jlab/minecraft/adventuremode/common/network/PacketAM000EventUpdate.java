package de.jlab.minecraft.mods.adventuremode.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.network.ExtendedPacket.ProtocolException;

public class PacketAM000EventUpdate extends PacketAMEvent {

	public PacketAM000EventUpdate() {};
	
	public PacketAM000EventUpdate(Event... events) {
		super(events);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		AdventureMode.instance.getEventHandler().handlePacket(this);
	}

}