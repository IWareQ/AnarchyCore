package AnarchySystem.Components.StorageItems.Commands;

import AnarchySystem.Components.StorageItems.StorageItemsAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.item.Item;

public class StorageCommand extends Command implements Listener {

	public StorageCommand() {
		super("storage", "§r§fХранилище купленных Предметов");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length == 0) {
				StorageItemsAPI.showStorageItemsChest(player, true);
			}
		} else {
			if (args.length >= 2) {
				String[] split = args[0].split(":");
				Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				StorageItemsAPI.addItemsToStorageItems(Utils.implode(args, 1), item);
				sender.sendMessage("§l§6• §r§fИгрок §6" + Utils.implode(args, 1) + " §fполучил §6" + item.getName() + " §fв колличестве §6" + split[2] + " §fшт§7!");
			}
		}
		return false;
	}
}