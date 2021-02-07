package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks;

import cn.nukkit.block.Block;

public class IronBlockProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.IRON_BLOCK;
	}

	@Override
	public String getBlockName() {
		return "§r§6Железный Регион";
	}

	@Override
	public Integer getRadius() {
		return 2;
	}
}