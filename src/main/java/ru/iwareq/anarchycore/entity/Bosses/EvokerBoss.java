package ru.iwareq.anarchycore.entity.Bosses;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityEvoker;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.entities.projectile.EntityWitherSkull;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;
import nukkitcoders.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EvokerBoss extends WalkingMonster implements Boss, EntitySmite {

	private static final int NETWORK_ID = EntityEvoker.NETWORK_ID;

	public EvokerBoss(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
		this.route = new WalkerRouteFinder(this);
	}

	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override()
	public float getWidth() {
		return 0.6F;
	}

	@Override()
	public float getHeight() {
		return 1.95F;
	}

	@Override()
	public double getSpeed() {
		return 1.1;
	}

	@Override()
	protected void initEntity() {
		super.initEntity();
		this.setNameTag("§lДревний колдун");
		this.setNameTagVisible(true);
		this.setNameTagAlwaysVisible(true);
		this.setScale((float) 1.2);
		this.setDamage(new float[]{10, 10, 10, 10});
		this.setMaxHealth(1000);
	}

	@Override()
	public void attackEntity(Entity player) {
		if (player instanceof Player) {
			if (this.attackDelay > 10 && this.distanceSquared(player) <= 800) {
				this.attackDelay = 0;
				double yaw = this.yaw + Utils.rand(-12.0, 12.0);
				double pitch = this.pitch + Utils.rand(-7.0, 7.0);
				Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(), this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
				if (this.getLevel().getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != Block.AIR) {
					return;
				}
				strikeBlackSkull(player.getPosition());
				this.level.addSound(this, Sound.MOB_WITHER_SHOOT);
			}
		}
	}

	private void strikeBlackSkull(Vector3 object) {
		Vector3 pos = this.asBlockVector3().add(0.5, this.getEyeHeight() + 2, 0.5);
		double pitch = this.getMyPitch(pos, object);
		Vector3 motion = new Vector3(-Math.sin(this.yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI), -Math.sin(pitch / 180 * Math.PI), Math.cos(this.yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI));
		CompoundTag nbt = Entity.getDefaultNBT(pos, motion, (float) this.yaw, (float) pitch);
		EntityWitherSkull skull = new EntityWitherSkull(this.getChunk(), nbt, this);
		if (skull instanceof EntityWitherSkull) {
			skull.spawnToAll();
		}
	}

	private double getMyPitch(Vector3 from, Vector3 to) {
		double distance = from.distance(to);
		double height = to.y - from.y;
		if (height > 0) {
			return -Math.toDegrees(Math.asin(height / distance));
		} else if (height < 0) {
			return Math.toDegrees(Math.asin(-height / distance));
		}
		return 0.0;
	}

	@Override()
	public Item[] getDrops() {
		List<Item> drops = new ArrayList<>();
		// drops.add(Item.get(CustomItemID.PIECE_ICE).setLore("\n§r§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7."));
		drops.add(Item.get(Item.EMERALD, 0, Utils.rand(1, 2)));
		return drops.toArray(new Item[0]);
	}

	@Override()
	public int getKillExperience() {
		return Utils.rand(100, 200);
	}

	@Override()
	public boolean entityBaseTick(int tickDiff) {
		if (getServer().getDifficulty() == 0) {
			this.close();
			return true;
		}
		return super.entityBaseTick(tickDiff);
	}

	@Override()
	public int nearbyDistanceMultiplier() {
		return 100;
	}
}