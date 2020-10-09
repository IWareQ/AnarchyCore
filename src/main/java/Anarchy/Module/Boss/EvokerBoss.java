package Anarchy.Module.Boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;
import nukkitcoders.mobplugin.utils.Utils;

public class EvokerBoss extends WalkingMonster {
	public static final int NETWORK_ID = 104;

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
		this.setDamage(new float[] {10, 10, 10, 10});
		this.setMaxHealth(1000);
	}

	@Override()
	public void attackEntity(Entity player) {
		if (this.attackDelay > 2 && player.distanceSquared(this) <= 3) {
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
		List<Item> drops = new ArrayList<>();
		Item treasure = Item.get(Item.DOUBLE_PLANT, 0, 1).setCustomName("§r§l§f๑ Сокровище ๑").setLore("§l§6• §r§fНажмите ПКМ или ЛКМ по любому блоку§7,\n§fчтобы активировать §6Сокровище");
		treasure.addEnchantment(Enchantment.get(30));
		drops.add(Item.get(Item.EMERALD, 0, Utils.rand(1, 16)));
		drops.add(Item.get(Item.TOTEM, 0, 1));
		drops.add(treasure);
		return drops.toArray(new Item[0]);
	}

	@Override()
	public int getKillExperience() {
		return 10;
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
		return 20;
	}
}