package ru.iwareq.anarchycore.entity.Bosses;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityWitherSkeleton;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemShovelWood;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.potion.Effect;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WitherSkeletonBoss extends WalkingMonster implements Boss {

	private static final int NETWORK_ID = EntityWitherSkeleton.NETWORK_ID;
	private static int superAttack = 0;

	public WitherSkeletonBoss(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override()
	public void initEntity() {
		super.initEntity();
		superAttack = 0;
		this.fireProof = true;
		this.setNameTag("§l§fХранитель Кладбища");
		this.setNameTagVisible(true);
		this.setNameTagAlwaysVisible(true);
		this.setScale((float) 1.2);
		this.setDamage(new float[]{7, 7, 7, 7});
		this.setMaxHealth(1000);
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
	public String getName() {
		return "Wither Skeleton Boss";
	}

	@Override()
	public float getHeight() {
		return 1.99F;
	}

	public void attackEntity(Entity player) {
		if (player instanceof Player) {
			if (this.attackDelay > 23 && player.distanceSquared(this) <= 5) {
				this.attackDelay = 0;
				if (this.getHealth() < this.getMaxHealth() / 2F && superAttack == 0) {
					superAttack = 1;
					for (int i = 0; i <= 5; i++) {
						strikeLighting(this.getPosition().add(Utils.rand(-2, 2), 0, Utils.rand(-2, 2)));
						Entity witherSkeleton = Entity.createEntity("HelperWitherSkeleton", this.getPosition().add(Utils.rand(-3, 3), 1, Utils.rand(-3, 3)));
						if (witherSkeleton != null) {
							witherSkeleton.spawnToAll();
						}
					}
					this.addEffect(Effect.getEffect(Effect.INVISIBILITY).setDuration(30 * 20).setVisible(false).setAmplifier(3));
					player.addEffect(Effect.getEffect(Effect.BLINDNESS).setDuration(30 * 20).setVisible(false).setAmplifier(3));
				}
				HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
				damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
				HashMap<Integer, Float> armorValues = new ArmorPoints();
				float points = 0;
				for (Item i : ((Player) player).getInventory().getArmorContents()) {
					points += armorValues.getOrDefault(i.getId(), 0.0F);
				}
				damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0F) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0F) * points * 0.04)));
				if (player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage))) {
					player.addEffect(Effect.getEffect(Effect.WITHER).setDuration(200));
				}
			}
		}
	}

	@Override()
	public void spawnTo(Player player) {
		super.spawnTo(player);
		MobEquipmentPacket pk = new MobEquipmentPacket();
		pk.eid = this.getId();
		pk.item = new ItemShovelWood();
		pk.hotbarSlot = 0;
		player.dataPacket(pk);
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
	public Item[] getDrops() {
		List<Item> drops = new ArrayList<>();
		// drops.add(Item.get(CustomItemID.REMAINS_GUARDIAN).setLore("\n§fЭтот предмет §7- §6Артефакт§7, §fиспользуй его\nдля обмена с §6Обменщиком Артефактов§7,\n§fкоторый находится на спавне§7."));
		drops.add(Item.get(Item.BONE, 0, 16));
		drops.add(Item.get(Item.DIAMOND, 0, Utils.rand(1, 9)));
		drops.add(Item.get(Item.EMERALD, 0, Utils.rand(1, 3)));
		return drops.toArray(new Item[0]);
	}

	@Override()
	public int getKillExperience() {
		return Utils.rand(200, 400);
	}

	private void strikeLighting(Position pos) {
		FullChunk chunk = pos.getLevel().getChunk((int) pos.getX() >> 4, (int) pos.getZ() >> 4);
		CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", pos.getX())).add(new DoubleTag("", pos.getY())).add(new DoubleTag("", pos.getZ()))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0)));
		EntityLightning lightning = new EntityLightning(chunk, nbt);
		lightning.spawnToAll();
	}
}