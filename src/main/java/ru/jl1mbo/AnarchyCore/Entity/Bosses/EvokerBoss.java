package ru.jl1mbo.AnarchyCore.Entity.Bosses;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityEvoker;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.entities.projectile.EntityBlueWitherSkull;
import nukkitcoders.mobplugin.entities.projectile.EntityWitherSkull;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;
import nukkitcoders.mobplugin.utils.Utils;
import ru.dragonestia.ironlib.IronLib;

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
        this.setNameTag("§l§fДревний колдун");
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setScale((float) 1.2);
        this.setDamage(new float[]{10, 10, 10, 10});
        this.setMaxHealth(1000);
    }

    @Override()
    public void attackEntity(Entity player) {
        if (player instanceof Player) {
            if (this.attackDelay > 5 && this.distanceSquared(player) <= 30) {
                this.attackDelay = 0;
                double f = 1;
                double yaw = this.yaw + Utils.rand(-12.0, 12.0);
                double pitch = this.pitch + Utils.rand(-7.0, 7.0);
                Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight() + 1, this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
                if (this.getLevel().getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != Block.AIR) {
                    return;
                }
                Entity k;
                ProjectileLaunchEvent launch;
                EntityWitherSkull skull;
                if (Utils.rand(0, 200) > 180 || Utils.rand(0, 200) < 20) {
                    f = 0.8;
                    k = Entity.createEntity("BlueWitherSkull", pos, this);
                    skull = (EntityBlueWitherSkull) k;
                    if (skull != null) {
                        ((EntityBlueWitherSkull) skull).setExplode(false);
                    }
                } else {
                    k = Entity.createEntity("WitherSkull", pos, this);
                    skull = (EntityWitherSkull) k;
                }
                if (skull != null) {
                    skull.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));
                }
                launch = new ProjectileLaunchEvent(skull);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    if (skull != null) {
                        skull.close();
                    }
                } else {
                    if (skull != null) {
                        skull.spawnToAll();
                    }
                    this.level.addSound(this, Sound.MOB_WITHER_SHOOT);
                }
            }
        }
    }

    @Override()
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        drops.add(IronLib.getInstance().getPrefabManager().getPrefab("ice_frosted_artifact").getItem(1));
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
        return 20;
    }
}