package ru.jl1mbo.AnarchyCore.Entity.Bosses;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityHusk;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.potion.Effect;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;
import nukkitcoders.mobplugin.utils.Utils;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CustomManager.Utils.CustomItemID;

public class HuskBoss extends WalkingMonster implements EntityAgeable, EntitySmite, Boss {
	private static final int NETWORK_ID = EntityHusk.NETWORK_ID;

	public HuskBoss(FullChunk chunk, CompoundTag nbt) {
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
		return 0.6;
	}

	@Nonnull()
	@Override()
	public String getName() {
		return "Husk Boss";
	}

	@Override()
	protected void initEntity() {
		super.initEntity();
		this.setNameTag("§lФараон");
		this.setNameTagVisible(true);
		this.setNameTagAlwaysVisible(true);
		this.setScale((float) 1.2);
		this.setDamage(new float[] {10, 10, 10, 10});
		this.setMaxHealth(1000);
	}

	@Override()
	public void attackEntity(Entity player) {
		if (player instanceof Player) {
			if (this.attackDelay > 23 && player.distanceSquared(this) <= 5) {
				this.attackDelay = 0;
				if (Utils.rand(0, 100) < 20) {
					this.move(Utils.rand(-30, 30), 0, Utils.rand(-30, 30));
					player.getLevel().addSound(player, Sound.MOB_SHULKER_TELEPORT);
				}
				player.attack(new EntityDamageByEntityEvent(this, player, DamageCause.ENTITY_ATTACK, getDamage()));
				EntityEventPacket pk = new EntityEventPacket();
				pk.eid = this.getId();
				pk.event = 4;
				Server.broadcastPacket(this.getViewers().values(), pk);
				player.addEffect(Effect.getEffect(Effect.HUNGER).setDuration(10 * 20).setVisible(false).setAmplifier(2));
			}
		}
	}

	@Override()
	public Item[] getDrops() {
		List<Item> drops = new ArrayList<>();
		drops.add(Item.get(Item.ROTTEN_FLESH, 0, Utils.rand(0, 16)));
		drops.add(Item.get(CustomItemID.SUN_WAND).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7."));
		drops.add(Item.get(Item.GOLD_BLOCK, 0, Utils.rand(0, 2)));
		drops.add(Item.get(Item.GOLDEN_APPLE, 0, Utils.rand(0, 2)));
		return drops.toArray(new Item[0]);
	}

	@Override()
	public int getKillExperience() {
		return Utils.rand(200, 350);
	}
}