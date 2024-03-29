package ru.iwareq.anarchycore.entity.NPC;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityWanderingTrader;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class WanderingTraderNPC extends Entity {

	private static final int NETWORK_ID = EntityWanderingTrader.NETWORK_ID;

	public WanderingTraderNPC(FullChunk chunk, CompoundTag nbt) {
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

	@Override()
	public float getHeight() {
		return 1.95F;
	}

	@Override()
	public void initEntity() {
		super.initEntity();
		this.setNameTag("§r§l§6Собиратель Артефактов\n§r§fНажмите§7, §fчтобы торговать");
		this.setNameTagVisible(true);
		this.setNameTagAlwaysVisible(true);
	}

	@Override()
	public String getName() {
		return "Wandering Trader NPC";
	}
}