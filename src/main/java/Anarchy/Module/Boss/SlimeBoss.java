package Anarchy.Module.Boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.monster.JumpingMonster;
import nukkitcoders.mobplugin.utils.Utils;

public class SlimeBoss extends JumpingMonster {
	public static final int NETWORK_ID = 37;
	public static final int SIZE_SMALL = 5;
	public static final int SIZE_MEDIUM = 6;
	public static final int SIZE_BIG = 7;
	protected int size = SIZE_BIG;

	public SlimeBoss(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override()
	public float getWidth() {
		return 0.51F + size * 0.51F;
	}

	@Override()
	public float getHeight() {
		return 0.51F + size * 0.51F;
	}

	@Override()
	public float getLength() {
		return 0.51F + size * 0.51F;
	}

	@Override()
	protected void initEntity() {
		super.initEntity();
		if (this.namedTag.contains("Size")) {
			this.size = this.namedTag.getInt("Size");
		} else {
			this.size = Utils.rand(5, 7);
		}
		this.setScale(0.51F + size * 0.51F);
		if (size == SIZE_BIG) {
			this.setMaxHealth(700);
		} else if (size == SIZE_MEDIUM) {
			this.setMaxHealth(500);
		} else if (size == SIZE_SMALL) {
			this.setMaxHealth(300);
		}
		if (size == SIZE_BIG) {
			this.setDamage(new float[] {5, 5, 5, 5});
		} else if (size == SIZE_MEDIUM) {
			this.setDamage(new float[] {5, 5, 5, 5});
		} else {
			this.setDamage(new float[] {5, 5, 5, 5});
		}
	}

	@Override()
	public void saveNBT() {
		super.saveNBT();
		this.namedTag.putInt("Size", this.size);
	}

	public void attackEntity(Entity player) {
		if (this.attackDelay > 23 && this.distanceSquared(player) < 1) {
			this.attackDelay = 0;
			HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
			damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
			if (player instanceof Player) {
				HashMap<Integer, Float> armorValues = new ArmorPoints();
				float points = 0;
				for (Item i : ((Player)player).getInventory().getArmorContents()) {
					points += armorValues.getOrDefault(i.getId(), 0.0F);
				}
				damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float)(damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0F) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0F) * points * 0.04)));
			}
			player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
		}
	}

	@Override()
	public Item[] getDrops() {
		if (this.size == SIZE_BIG) {
			CreatureSpawnEvent ev = new CreatureSpawnEvent(NETWORK_ID, this, this.namedTag, CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);
			level.getServer().getPluginManager().callEvent(ev);
			if (ev.isCancelled()) {
				return new Item[0];
			}
			SlimeBoss entity = (SlimeBoss)Entity.createEntity("SlimeBoss", this);
			if (entity != null) {
				entity.size = SIZE_MEDIUM;
				entity.setScale(0.51F + entity.size * 0.51F);
				entity.spawnToAll();
			}
			return new Item[0];
		} else if (this.size == SIZE_MEDIUM) {
			CreatureSpawnEvent ev = new CreatureSpawnEvent(NETWORK_ID, this, this.namedTag, CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);
			level.getServer().getPluginManager().callEvent(ev);
			if (ev.isCancelled()) {
				return new Item[0];
			}
			SlimeBoss entity = (SlimeBoss)Entity.createEntity("SlimeBoss", this);
			if (entity != null) {
				entity.size = SIZE_SMALL;
				entity.setScale(0.51F + entity.size * 0.51F);
				entity.spawnToAll();
			}
			return new Item[0];
		} else {
			List<Item> drops = new ArrayList<>();
			drops.add(Item.get(Item.DIAMOND, 0, 6));
			drops.add(Item.get(Item.EMERALD, 0, 2));
			return drops.toArray(new Item[0]);
		}
	}

	@Override()
	public int getKillExperience() {
		if (this.size == SIZE_BIG) return 4;
		if (this.size == SIZE_MEDIUM) return 2;
		if (this.size == SIZE_SMALL) return 1;
		return 0;
	}

	@Override()
	public boolean entityBaseTick(int tickDiff) {
		if (getServer().getDifficulty() == 0) {
			this.close();
			return true;
		}
		return super.entityBaseTick(tickDiff);
	}
}