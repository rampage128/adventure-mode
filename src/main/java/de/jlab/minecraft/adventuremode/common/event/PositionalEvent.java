package de.jlab.minecraft.adventuremode.common.event;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

/**
 * Base class for static events with fixed position like invasions etc
 * @author rampage
 *
 */
public abstract class PositionalEvent extends Event {

	private World world = null;
	private DimensionType dimensionType = DimensionType.OVERWORLD;
	private BlockPos position = new BlockPos(0, 0, 0);
	
	public PositionalEvent(EventGenerator generator) {
		super(generator);
	}
	
	@Override
	public void onInit(EntityPlayer player) {
		this.world 			= player.getEntityWorld();
		this.dimensionType  = player.getEntityWorld().provider.getDimensionType();
		this.position 		= new BlockPos(player.getPosition());
	}

	protected final DimensionType getDimensionType() {
		return this.dimensionType;
	}
	
	protected final void setPosition(BlockPos pos) {
		this.position = new BlockPos(pos);
	}
	
	@Override
	public final BlockPos getPosition() {
		return this.position;
	}
	
	protected World getWorld() {
		return this.world;
	}
	
	@Override
	public abstract boolean isActive();

	@Override
	public abstract void update();

	@Override
	public boolean affectsPlayer(EntityPlayer player) {
		return player.getEntityWorld().provider.getDimensionType() == this.getDimensionType() && 
				Math.sqrt(player.getPosition().distanceSq(this.getPosition())) < getRadius();
	}
	
	public abstract int getRadius();
	
	@Override
	public void readFromPacket(ByteBuf in) {
		this.dimensionType = DimensionType.getById(in.readInt());
		this.setPosition(new BlockPos(in.readInt(), in.readInt(), in.readInt()));
	}
	
	@Override
	public void writeToPacket(ByteBuf out) {
		out.writeInt(this.dimensionType.getId());
		out.writeInt(this.getPosition().getX());
		out.writeInt(this.getPosition().getY());
		out.writeInt(this.getPosition().getZ());
	}
	
}
