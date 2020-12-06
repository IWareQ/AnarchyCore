package Anarchy.Module.Commands.NPC.Utils;

import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.entity.mob.EntityPiglinBrute;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class PiglinBruteNPC extends EntityPiglin {
	public static final int NETWORK_ID = 127;

	public PiglinBruteNPC(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override()
	public int getNetworkId() {
		return NETWORK_ID;
	}
}