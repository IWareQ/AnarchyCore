package ru.jl1mbo.AnarchyCore.Entity.NPC;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.Boss;

public class CowNPC extends Entity implements Boss {

	private static final int NETWORK_ID = 11;

	public CowNPC(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override
	public float getWidth() {
		return 0.45f;
	}

	@Override
	public float getHeight() {
		return 1.4f;
	}

	@Override
	public void initEntity() {
		super.initEntity();
		this.setMaxHealth(200);
	}
}