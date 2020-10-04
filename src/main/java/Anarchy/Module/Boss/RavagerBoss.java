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

public class RavagerBoss extends WalkingMonster {
	public static final int NETWORK_ID = 59;

	public RavagerBoss(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override()
	protected void initEntity() {
		super.initEntity();
		this.setMaxHealth(100);
		this.setDamage(new float[] {9, 9, 9, 9});
	}

	@Override()
	public float getHeight() {
		return 1.9F;
	}

	@Override()
	public float getWidth() {
		return 1.2F;
	}

	@Override()
	public int getKillExperience() {
		return 0;
	}

	@Override()
	public void attackEntity(Entity player) {
		if (this.attackDelay > 40 && player.distanceSquared(this) <= 3) {
			this.attackDelay = 0;
			HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
			damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
			if (player instanceof Player) {
				@SuppressWarnings("serial")
				HashMap<Integer, Float> armorValues = new HashMap<Integer, Float>() {
					{
						put(Item.LEATHER_CAP, 1.0F);
						put(Item.LEATHER_TUNIC, 3.0F);
						put(Item.LEATHER_PANTS, 2.0F);
						put(Item.LEATHER_BOOTS, 1.0F);
						put(Item.CHAIN_HELMET, 1.0F);
						put(Item.CHAIN_CHESTPLATE, 5.0F);
						put(Item.CHAIN_LEGGINGS, 4.0F);
						put(Item.CHAIN_BOOTS, 1.0F);
						put(Item.GOLD_HELMET, 1.0F);
						put(Item.GOLD_CHESTPLATE, 5.0F);
						put(Item.GOLD_LEGGINGS, 3.0F);
						put(Item.GOLD_BOOTS, 1.0F);
						put(Item.IRON_HELMET, 2.0F);
						put(Item.IRON_CHESTPLATE, 6.0F);
						put(Item.IRON_LEGGINGS, 5.0F);
						put(Item.IRON_BOOTS, 2.0F);
						put(Item.DIAMOND_HELMET, 3.0F);
						put(Item.DIAMOND_CHESTPLATE, 8.0F);
						put(Item.DIAMOND_LEGGINGS, 6.0F);
						put(Item.DIAMOND_BOOTS, 3.0F);
					}
				};
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
		drops.add(Item.get(Item.EXPERIENCE_BOTTLE, 0, 32));
		drops.add(Item.get(Item.PHANTOM_MEMBRANE, 0, 1));
		drops.add(treasure);
		return drops.toArray(new Item[0]);
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