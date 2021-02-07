package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks;

import cn.nukkit.block.Block;

public class DiamondOreProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.DIAMOND_BLOCK;
	}

	@Override
	public String getBlockName() {
		return "§r§6Алмазный Регион";
	}

	@Override
	public Integer getRadius() {
		return 4;
	}
}