package Anarchy.Module.Commands.NPC.Utils;

import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class VillagerNPC extends EntityVillager {
	public static final int NETWORK_ID = 15;

	public VillagerNPC(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}
}