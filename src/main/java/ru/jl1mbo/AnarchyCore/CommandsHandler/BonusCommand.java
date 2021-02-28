package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;

public class BonusCommand extends Command {

	public BonusCommand() {
		super("bonus", "§rПолучить бонус");
		this.setPermission("Command.Bonus");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			Config config = new Config(Main.getInstance().getDataFolder() + "/BonusCommand/users.yml", Config.YAML);
			if (config.exists(player.getName().toLowerCase())) {
				player.sendMessage("§l§6• §rБонус можно получить §61 §fраз в вайп§7!");
				return false;
			}
			PlayerInventory playerInventory = player.getInventory();
			Item[] item = new Item[] {Item.get(Item.MOB_SPAWNER, 0, 1), Item.get(Item.SPAWN_EGG, 13, 1)};
			for (Item items : item) {
				if (playerInventory.canAddItem(items)) {
					playerInventory.addItem(items);
					player.sendMessage("§l§a• §rВы успешно §6взяли §fбонус§7");
					config.set(player.getName().toLowerCase(), true);
					config.save();
				} else {
					player.sendMessage("§l§6• §rВаш инвентарь переполнен§7!");
				}
			}
		}
		return false;
	}
}