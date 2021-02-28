package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Blocks;

import cn.nukkit.block.Block;

public class EmeraldOreProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.EMERALD_ORE;
	}

	@Override
	public String getBlockName() {
		return "§6Изумрудный Регион";
	}

	@Override
	public Integer getRadius() {
		return 8;
	}

	@Override
	public String getBlockImage() {
		return "textures/ui/emerald_ore";
	}
}