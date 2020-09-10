package Anarchy.Module.Auction.Utils.Inventory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Map;

import Anarchy.Manager.FakeChests.Inventory.DoubleDefaultChest;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.utils.Config;

public class TakeChest extends DoubleDefaultChest {
	private static File dataFile;
	
	public TakeChest(String title, File file) {
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
			try {
				Item item = entry.getValue();
				config.set(item.getNamedTag().getString("UUID"), item.hasCompoundTag() ? new Object[]{item.getId(), item.getDamage(), item.getCount(), NBTIO.write(item.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} : new Object[]{item.getId(), item.getDamage(), item.getCount()});
			} catch (IOException e) {
				Server.getInstance().getLogger().alert("Error onClose - " + e);
			}
		}
		config.save();
		super.onClose(player);
	}
}