package ru.iwareq.anarchycore.module.BlockProtection.Blocks.Protect;

import cn.nukkit.block.Block;
import ru.iwareq.anarchycore.module.BlockProtection.Blocks.DefaultBlockProtection;

public class IronBlockProtection extends DefaultBlockProtection {

	@Override
	public Integer getBlockId() {
		return Block.IRON_BLOCK;
	}

	@Override
	public String getBlockName() {
		return "§6Железный Регион";
	}

	@Override
	public Integer getRadius() {
		return 2;
	}

	@Override
	public String getBlockImage() {
		return "textures/ui/iron_block";
	}
}