package Anarchy.Module.Boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;
import nukkitcoders.mobplugin.utils.Utils;

public class SilverfishBoss extends WalkingMonster implements EntityArthropod {
	public static final int NETWORK_ID = 39;
	
	public SilverfishBoss(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
		this.route = new WalkerRouteFinder(this);
	}
	
	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}
	
	@Override()
	public float getWidth() {
		return 0.4F;
	}
	
	@Override()
	public float getHeight() {
		return 0.3F;
	}
	
	@Override()
	public double getSpeed() {
		return 1.4;
	}
	
	@Override()
	public void initEntity() {
		super.initEntity();
		this.setMaxHealth(50);
		this.setDamage(new float[]{5, 5, 5, 5});
	}
	
	@Override()
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
	public  Item[] getDrops() {
		List<Item> drops = new ArrayList<>();
		Item item =  Item.get(Item.DOUBLE_PLANT, 0, 1).setCustomName("§l§fСокровище").setLore("§l§6• §r§fНажмите ПКМ или ЛКМ по любому блоку");
		item.addEnchantment(Enchantment.get(1));
		drops.add(Item.get(Item.IRON_INGOT, 0, 9));
		drops.add(Item.get(Item.IRON_NUGGET, 0, Utils.rand(10, 30)));
		drops.add(item);
		return drops.toArray(new Item[0]);
	}
	
	@Override()
	public int getKillExperience() {
		return 1500;
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