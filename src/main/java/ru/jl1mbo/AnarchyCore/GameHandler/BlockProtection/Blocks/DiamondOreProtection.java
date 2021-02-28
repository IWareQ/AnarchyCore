package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks;

import cn.nukkit.block.Block;

public class DiamondOreProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.DIAMOND_ORE;
	}

	@Override
	public String getBlockName() {
		return "§6Алмазный Регион";
	}

	@Override
	public Integer getRadius() {
		return 4;
	}

	@Override
	public String getBlockImage() {
		return "textures/ui/diamond_ore";
	}
}