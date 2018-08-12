package de.jlab.minecraft.adventuremode.common.network;

import java.nio.charset.StandardCharsets;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.event.Event;
import de.jlab.minecraft.adventuremode.common.event.EventGenerator;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EventMessage implements IMessage {

	public static final int TYPE_UPDATE 	= 0;
	public static final int TYPE_STARTED 	= 1;
	public static final int TYPE_ENDED 		= 2;
	
	private int type = 0;
	private Event[] events;
	
	/**
	 * Default Constructor only for packet reception
	 */
	public EventMessage() {}
	
	public EventMessage(int type, Event... events) {
		this.events = events;
		this.type 	= type;
	}
	
	public Event[] getEvents() {
		return this.events;
	}
	
	public int getType() {
		return this.type;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = buf.readInt();
		this.events = new Event[buf.readInt()];
    	for (int i = 0; i < this.events.length; i++) {
			EventGenerator generator = AdventureMode.instance.getEventScheduler().getEventGenerator(buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString());
			Event event = generator.createEvent();
			event.readFromPacket(buf);
			this.events[i] = event;
    	}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.type);
		buf.writeInt(this.events.length);
		for (Event event : this.events) {
			String type = event.getGenerator().getType();
			buf.writeInt(type.length());
			buf.writeCharSequence(type, StandardCharsets.UTF_8);
    		event.writeToPacket(buf);
    	}
	}

}
