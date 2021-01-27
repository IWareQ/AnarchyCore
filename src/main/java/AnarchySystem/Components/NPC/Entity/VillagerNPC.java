package AnarchySystem.Components.NPC.Entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;

import javax.annotation.Nonnull;

public class VillagerNPC extends Entity implements Boss {
    private static final int NETWORK_ID = EntityVillager.NETWORK_ID;

    public VillagerNPC(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override()
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override()
    public float getWidth() {
        return 0.6F;
    }

    @Nonnull()
    @Override()
    public String getName() {
        return "Villager NPC";
    }

    @Override()
    public float getHeight() {
        return 1.95F;
    }

    @Override()
    public void initEntity() {
        super.initEntity();
        this.setNameTag("§r§l§6Аукционер\n§r§fНажмите§7, §fчтобы открыть Аукцион");
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setScale((float) 1.3);
    }
}