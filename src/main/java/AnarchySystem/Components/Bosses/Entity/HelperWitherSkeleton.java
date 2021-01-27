package AnarchySystem.Components.Bosses.Entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityWitherSkeleton;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemShovelNetherite;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.potion.Effect;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperWitherSkeleton extends WalkingMonster implements EntitySmite {
    private static final int NETWORK_ID = EntityWitherSkeleton.NETWORK_ID;

    public HelperWitherSkeleton(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
    }

    @Override()
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Nonnull
    @Override()
    public String getName() {
        return "Helper Wither Skeleton";
    }

    @Override()
    protected void initEntity() {
        super.initEntity();
        this.fireProof = true;
        this.setScale((float) 1.2);
        this.setNameTag("§l§fХранитель Кладбища");
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setMaxHealth(50);
        this.setDamage(new float[]{4, 4, 4, 4});
    }

    @Override()
    public float getWidth() {
        return 0.7F;
    }

    @Override()
    public double getSpeed() {
        return 3.0;
    }

    @Override()
    public float getHeight() {
        return 2.4F;
    }

    @Override()
    public void attackEntity(Entity player) {
        if (player instanceof Player) {
            if (this.attackDelay > 23 && player.distanceSquared(this) <= 10) {
                this.attackDelay = 0;
                HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
                damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
                HashMap<Integer, Float> armorValues = new ArmorPoints();
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0.0F);
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0F) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0F) * points * 0.04)));
                if (player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage))) {
                    player.addEffect(Effect.getEffect(Effect.WITHER).setDuration(10 * 20));
                }
            }
        }
    }

    @Override()
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = new ItemShovelNetherite();
        pk.hotbarSlot = 0;
        player.dataPacket(pk);
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
}