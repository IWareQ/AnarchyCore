package Anarchy.Module.Commands.Storage.Utils;

import java.io.File;
import java.util.Map;

import FakeInventoryAPI.Inventory.DefaultDoubleChest;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

public class StorageChest extends DefaultDoubleChest {
	private static File dataFile;

	public StorageChest(String title, File file) {
		super(title);
		dataFile = file;
	}

	@Override()
	public void onClose(Player player) {
		dataFile.delete();
		if (this.getContents().isEmpty()) {
			return;
		}
		Config config = new Config(dataFile, Config.YAML);
		for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
			Item item = entry.getValue();
			config.set(item.getNamedTag().getString("UUID"), new Object[] {item.getId(), item.getDamage(), item.getCount()});
		}
		config.save();
		super.onClose(player);
	}
}