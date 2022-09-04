package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import ru.iwareq.anarchycore.Main;

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
			Config config = new Config(Main.getInstance().getDataFolder() + "/Other/Bonus.yml", Config.YAML);
			if (!config.exists(player.getName())) {
				config.set(player.getName(), true);
				player.getInventory().addItem(Item.get(Item.MONSTER_SPAWNER));
				player.sendMessage("§l§6• §rБонус успешно взят§7!");
			} else {
				player.sendMessage("§l§6• §rВы уже брали бонус");
			}
		}
		return false;
	}
}