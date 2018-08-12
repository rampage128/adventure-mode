package de.jlab.minecraft.mods.adventuremode.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.PacketHandler;
import de.jlab.minecraft.mods.adventuremode.event.Event;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.network.ExtendedPacket.ProtocolException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;

public abstract class PacketAMEvent extends ExtendedPacket {

	private Event[] events;
	
	/**
	 * Default Constructor only for packet reception
	 */
	public PacketAMEvent() {}
	
	public PacketAMEvent(Event... events) {
		this.events = events;
	}
	
    public void write(ByteArrayDataOutput out) {
    	out.writeInt(this.events.length);
    	for (Event event : this.events) {
    		out.writeUTF(event.getGenerator().getType());
    		/*
    		try {
    			Packet.writeString(event.getGenerator().getType(), out);
    		} catch (Exception e) {}
    		*/
    		event.writeToPacket(out);
    	}
    }
    
    public void read(ByteArrayDataInput in) throws ProtocolException {
    	this.events = new Event[in.readInt()];
    	for (int i = 0; i < this.events.length; i++) {
    		try {
    			EventGenerator generator = AdventureMode.instance.getEventHandler().getEventGenerator(in.readUTF());
    			/*
				EventGenerator generator = AdventureMode.instance.getEventHandler().getEventGenerator(Packet.readString(in, Short.MAX_VALUE));
				*/ 
				Event event = generator.createEvent();
				event.readFromPacket(in);
				this.events[i] = event;
    		} catch (Exception e) {
    			throw new ProtocolException(e.getMessage());
    		}
    	}
    }
    
	public Event[] getEvents() {
		return this.events;
	}
	
    public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;

}
