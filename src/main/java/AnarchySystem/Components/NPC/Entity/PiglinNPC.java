package AnarchySystem.Components.NPC.Entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;

import javax.annotation.Nonnull;

public class PiglinNPC extends Entity implements Boss {
    private static final int NETWORK_ID = EntityPiglin.NETWORK_ID;

    public PiglinNPC(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override()
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override()
    protected void initEntity() {
        super.initEntity();
        this.setNameTag("§r§l§6Барыга");
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override()
    public float getWidth() {
        return 0.6F;
    }

    @Nonnull()
    @Override()
    public String getName() {
        return "Piglin NPC";
    }

    @Override()
    public float getHeight() {
        return 1.95F;
    }
}