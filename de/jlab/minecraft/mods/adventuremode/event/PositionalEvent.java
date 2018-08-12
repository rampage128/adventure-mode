package de.jlab.minecraft.mods.adventuremode.event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.jlab.minecraft.mods.adventuremode.AdventureMode;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

/**
 * Base class for static events with fixed position like invasions etc
 * @author rampage
 *
 */
public abstract class PositionalEvent extends Event {

	private World world;
	private int dimensionId;
	private Vec3 position = Vec3.createVectorHelper(0, 0, 0);
	
	public PositionalEvent(EventGenerator generator) {
		super(generator);
	}
	
	@Override
	public void onInit(EntityPlayer player) {
		this.world 			= player.worldObj;
		this.dimensionId 	= this.world.provider.dimensionId;
		this.position 		= Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
	}

	public final void setPosition(double x, double y, double z) {
		position.xCoord = x;
		position.yCoord = y;
		position.zCoord = z;
	}
	
	@Override
	public final Vec3 getPosition() {
		return this.position;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public int getDimensionId() {
		return this.dimensionId;
	}

	@Override
	public abstract boolean isActive();

	@Override
	public abstract void update();

	@Override
	public void readFromPacket(ByteArrayDataInput in) {	
		this.dimensionId = in.readInt();
		this.setPosition(in.readInt(), in.readInt(), in.readInt());
	}
	
	@Override
	public void writeToPacket(ByteArrayDataOutput out) {
		out.writeInt(this.dimensionId);
		out.writeInt((int)this.getPosition().xCoord);
		out.writeInt((int)this.getPosition().yCoord);
		out.writeInt((int)this.getPosition().zCoord);
	}
	
}
