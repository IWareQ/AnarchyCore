package Anarchy.Module.Commands;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

public class BonusCommand extends Command {

	public BonusCommand() {
		super("bonus", "§r§fБонус");
		this.setPermission("Command.Bonus");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Bonus")) {
				player.sendMessage("§l§6• §r§fБонус доступен от привилегии §6Тартар§7!");
				return false;
			}
			PlayerInventory playerInventory = player.getInventory();
			Integer bonusID = SQLiteUtils.selectInteger("Users.db", "SELECT Bonus FROM USERS WHERE UPPER(Username) = \'" + player.getName().toUpperCase() + "\';");
			if (bonusID == 0) {
				playerInventory.addItem(Item.get(Item.MONSTER_SPAWNER, 0, 1));
				playerInventory.addItem(Item.get(Item.MONSTER_EGG, 11, 2));
				playerInventory.addItem(Item.get(Item.GOLDEN_APPLE_ENCHANTED, 0, 1));
				player.sendMessage("§l§a• §r§fБонус был успешно взят§7!");
				SQLiteUtils.query("Users.db", "UPDATE USERS SET Bonus = \'1\' WHERE UPPER(Username) = \'" + player.getName().toUpperCase() + "\';");
			} else if (bonusID == 1) {
				player.sendMessage("§l§6• §r§fБонус можно получить §61 §fраз за вайп§7!");
			}
		}
		return false;
	}
}