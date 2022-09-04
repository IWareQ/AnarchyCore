package ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect;

import cn.nukkit.block.Block;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.DefaultBlockProtection;

public class EmeraldBlockProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.EMERALD_BLOCK;
	}

	@Override
	public String getBlockName() {
		return "§6Изумрудный Регион";
	}

	@Override
	public Integer getRadius() {
		return 10;
	}

	@Override
	public String getBlockImage() {
		return "textures/ui/emerald_block";
	}
}