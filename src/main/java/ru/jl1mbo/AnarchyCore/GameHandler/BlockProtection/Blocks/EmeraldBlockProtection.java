package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks;

import cn.nukkit.block.Block;

public class EmeraldBlockProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.EMERALD_BLOCK;
	}

	@Override
	public String getBlockName() {
		return "§r§6Изумрудный Регион";
	}

	@Override
	public Integer getRadius() {
		return 10;
	}
}