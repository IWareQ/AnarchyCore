package AnarchySystem.Components.Bosses.Entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.mob.EntityCaveSpider;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperCaveSpider extends WalkingMonster implements EntityArthropod, Boss {
    private static final int NETWORK_ID = EntityCaveSpider.NETWORK_ID;

    public HelperCaveSpider(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override()
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override()
    public float getWidth() {
        return 0.7F;
    }

    @Override()
    public float getHeight() {
        return 0.5F;
    }

    @Override()
    public double getSpeed() {
        return 5.0;
    }

    @Override()
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(5);
        this.setDamage(new float[]{2, 2, 2, 2});
    }

    @Override()
    public void attackEntity(Entity player) {
        if (player instanceof Player) {
            if (this.attackDelay > 23 && this.distanceSquared(player) < 1.32) {
                this.attackDelay = 0;
                HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
                damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
                HashMap<Integer, Float> armorValues = new ArmorPoints();
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0.0F);
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0F) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0F) * points * 0.04)));
                player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
            }
        }
    }

    @Override()
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        return drops.toArray(new Item[0]);
    }

    @Override()
    public int getKillExperience() {
        return 0;
    }

    @Nonnull
    @Override()
    public String getName() {
        return "Helper Cave Spider";
    }
}