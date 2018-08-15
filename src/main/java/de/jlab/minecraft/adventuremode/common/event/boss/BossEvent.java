package de.jlab.minecraft.adventuremode.common.event.boss;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jlab.minecraft.adventuremode.common.config.AdventureConfig;
import de.jlab.minecraft.adventuremode.common.event.EventGenerator;
import de.jlab.minecraft.adventuremode.common.event.PositionalEvent;
import de.jlab.minecraft.adventuremode.utils.EntityRespawn;
import de.jlab.minecraft.adventuremode.utils.EntitySpawnHelper;
import de.jlab.minecraft.adventuremode.utils.RandomRange;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class BossEvent extends PositionalEvent {

	private EntityLiving boss;

	private int addCount = 0;
	private ArrayList<EntityLiving> addList = new ArrayList<EntityLiving>();
	private ArrayList<EntityRespawn> respawnList = new ArrayList<EntityRespawn>();

	private RandomRange randomRange = new RandomRange();
	private EntitySpawnHelper spawnHelper = new EntitySpawnHelper();

	public BossEvent(EventGenerator generator) {
		super(generator);
	}

	@Override
	public void onInit(EntityPlayer player) {
		super.onInit(player);
		this.addCount = randomRange.weightedRange(AdventureConfig.events.boss.minAdds,
				AdventureConfig.events.boss.maxAdds, 30, 70);
	}

	private void spawnboss() {
		// create entity by chance
		HashMap<String, Double> bossTypes = AdventureConfig.events.boss.bossTypes;
		Entity entity = this.spawnHelper.getRandomEntity(bossTypes, "minecraft:witch",
				this.getPlayer().getEntityWorld());

		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;

		// return if entity is not a monster
		if (entityliving == null) {
			return;
		}

		// compute spawn position
		this.spawnHelper.setRandomCircleSpawnPosition(entity, this.getPlayer().getPosition(), 16, 32);
		// this.spawnHelper.setSpawnPosition(entity,
		// Vec3.createVectorHelper(this.getPlayer().posX, this.getPlayer().posY,
		// this.getPlayer().posZ), 8);

		// abort spawning if conditions are not met
		// if (!entityliving.getCanSpawnHere()) {
		// return;
		// }

		// spawn monster
		this.spawnHelper.spawnEntityLiving(entityliving);

		// set aggro to nearest player
		this.spawnHelper.autoAggro(entityliving, 40);

		// set boss
		this.boss = entityliving;

		this.setPosition(this.boss.getPosition());
	}

	private void spawnadds() {
		for (int i = 0; i < this.addCount; i++) {
			EntityLiving add = createAdd();
			if (add != null) {
				this.addList.add(add);
			}
		}
	}

	private EntityLiving createAdd() {
		// create entity by chance
		HashMap<String, Double> addTypes = AdventureConfig.events.boss.addTypes;
		Entity entity = this.spawnHelper.getRandomEntity(addTypes, "minecraft:zombie",
				this.getPlayer().getEntityWorld());

		EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;

		// return if entity is not a monster
		if (entityliving == null) {
			return null;
		}

		return spawnAdd(entityliving);
	}

	private EntityLiving spawnAdd(EntityLiving add) {
		// compute spawn position
		this.spawnHelper.setSpawnPosition(add, this.getPosition(), 3);

		// abort spawning if conditions are not met
		if (!add.getCanSpawnHere()) {
			return null;
		}

		// spawn monster
		this.spawnHelper.spawnEntityLiving(add);

		// set aggro to nearest player
		this.spawnHelper.autoAggro(add, -1);

		return add;
	}

	@Override
	public boolean isActive() {
		// stop event when boss does not exist or is dead
		return this.boss != null && !this.boss.isDead;
	}

	@Override
	public void start() {
		this.spawnboss();
		this.spawnadds();
	}

	@Override
	public void update() {
		this.setPosition(this.boss.getPosition());

		for (Iterator<EntityLiving> addIterator = this.addList.iterator(); addIterator.hasNext();) {
			EntityLiving add = addIterator.next();
			if (add.isDead) {
				EntityRespawn respawn = new EntityRespawn(add, 5);
				this.respawnList.add(respawn);
				addIterator.remove();
			}
		}

		for (Iterator<EntityRespawn> respawnIterator = this.respawnList.iterator(); respawnIterator.hasNext();) {
			EntityRespawn respawn = respawnIterator.next();
			if (respawn.updateCheck()) {
				EntityLiving newEntity = (EntityLiving) respawn.getNewEntity();
				if (spawnAdd(newEntity) != null) {
					this.addList.add(newEntity);
					respawnIterator.remove();
				} else {
					respawn.resetTime();
				}
			}
		}
	}

	@Override
	public void end() {
		for (EntityLiving add : this.addList) {
			if (this.boss != null && this.boss.isDead) {
				add.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
			} else {
				add.getEntityWorld().removeEntity(add);
			}
		}
		if (this.boss != null && !this.boss.isDead) {
			boss.getEntityWorld().removeEntity(boss);
		}
		this.addList.clear();
	}

	@Override
	public int getRadius() {
		return 96;
	}

	@Override
	public String getLabelText() {
		return "Defeat the evil " + this.boss.getName() + "!";
	}

	//////////////////
	/// NETWORKING ///
	//////////////////

	@Override
	public void readFromPacket(ByteBuf in) {
		super.readFromPacket(in);
		this.boss = (EntityLiving) EntityList.createEntityByIDFromName(new ResourceLocation(in.readCharSequence(in.readInt(), StandardCharsets.UTF_8).toString()), this.getWorld());
	}

	@Override
	public void writeToPacket(ByteBuf out) {
		super.writeToPacket(out);
		String entityName = EntityList.getEntityString(this.boss);
		out.writeInt(entityName.length());
		out.writeCharSequence(entityName, StandardCharsets.UTF_8);
	}

}
