package AnarchySystem.Components.Bosses.Bosses;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.mob.EntitySpider;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import ru.dragonestia.ironlib.IronLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpiderBoss extends WalkingMonster implements EntityArthropod, Boss {
    private static final int NETWORK_ID = EntitySpider.NETWORK_ID;

    public SpiderBoss(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override()
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override()
    public float getWidth() {
        return 1.4F;
    }

    @Override()
    public float getHeight() {
        return 0.9F;
    }

    @Override()
    public double getSpeed() {
        return 1.0;
    }

    public void initEntity() {
        super.initEntity();
        this.setNameTag("§l§fТарантул");
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setScale((float) 1.3);
        this.setMaxHealth(700);
        this.setDamage(new float[]{7, 7, 7, 7});
    }

    @Override()
    protected boolean checkJump(double dx, double dz) {
        if (this.motionY == this.getGravity() * 2) {
            int b = level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z));
            return b == BlockID.WATER || b == BlockID.STILL_WATER;
        } else {
            int b = level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
            if (b == BlockID.WATER || b == BlockID.STILL_WATER) {
                this.motionY = this.getGravity() * 2;
                return true;
            }
        }
        try {
            Block block = this.getLevel().getBlock(new Vector3(NukkitMath.floorDouble(this.x + dx), (int) this.y, NukkitMath.floorDouble(this.z + dz)));
            Block directionBlock = block.getSide(this.getDirection());
            if (!directionBlock.canPassThrough()) {
                this.motionY = this.getGravity() * 3;
                return true;
            }
        } catch (Exception ignore) {

        }
        return false;
    }

    @Override()
    public void attackEntity(Entity player) {
        if (player instanceof Player) {
            if (this.attackDelay > 15 && this.distanceSquared(player) < 5) {
                this.attackDelay = 0;
                HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
                damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
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
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0.0F);
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0F) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0F) * points * 0.04)));
                player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
                Entity caveSpider = Entity.createEntity("HelperCaveSpider", this.getPosition());
                if (caveSpider != null) {
                    caveSpider.spawnToAll();
                }
            }
        }
    }

    @Override()
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        drops.add(Item.get(Item.SPIDER_EYE, 0, Utils.rand(0, 16)));
        drops.add(IronLib.getInstance().getPrefabManager().getPrefab("cobweb_artifact").getItem(1));
        drops.add(Item.get(Item.STRING, 0, Utils.rand(0, 16)));
        drops.add(Item.get(Item.EXPERIENCE_BOTTLE, 0, Utils.rand(0, 10)));
        return drops.toArray(new Item[0]);
    }

    @Override()
    public int getKillExperience() {
        return Utils.rand(100, 300);
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