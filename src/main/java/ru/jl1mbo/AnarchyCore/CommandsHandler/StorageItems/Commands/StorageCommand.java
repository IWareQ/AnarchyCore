package ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.StorageItemsAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class StorageCommand extends Command implements Listener {

	public StorageCommand() {
		super("storage", "§rХранилище купленных Предметов");
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
				StorageItemsAPI.addItem(Utils.implode(args, 1), item);
				sender.sendMessage("§l§6• §rИгрок §6" + Utils.implode(args, 1) + " §fполучил §6" + item.getName() + " §fв колличестве §6" + split[2] + " §fшт§7.");
			}
		}
		return false;
	}
}