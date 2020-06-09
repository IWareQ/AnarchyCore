package Anarchy.Module.Commands.Donate.Utils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import Anarchy.Manager.FakeChests.Inventory.DoubleDefaultChest;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

public class DonateChest extends DoubleDefaultChest {
	private static File dataFile;

	public DonateChest(String title, File file) {
		super(title);
		dataFile = file;
	}

	@Override
	public void onClose(Player who) {
		super.onClose(who);
		dataFile.delete();
		if (this.getContents().isEmpty()) {
			dataFile.delete();
			return;
		}

		Config config = new Config(dataFile, Config.YAML);
		LinkedHashMap objectMap = new LinkedHashMap();
		for (Map.Entry<Integer, Item> entry: this.getContents().entrySet()) {
			Item item = entry.getValue();
			CompoundTag compoundTag = item.getNamedTag();
			objectMap.put(compoundTag.getString("UUID"), new Object[] {
				compoundTag.getString("DATE"),
				item.getId(),
				item.getDamage(),
				item.getCount()
			});
		}
		config.setAll(objectMap);
		config.save();
	}
}