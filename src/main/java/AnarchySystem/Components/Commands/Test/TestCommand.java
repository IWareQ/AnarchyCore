package AnarchySystem.Components.Commands.Test;

import AnarchySystem.Main;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

public class TestCommand extends Command {

	public TestCommand() {
		super("test", "§r§fПолучить бонус");
		this.setPermission("Command.Bonus");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			Entity entity = Entity.createEntity("TestEntity", player.getPosition());
			if (entity != null) {
				player.sendMessage("готово");
				
				entity.spawnToAll();
			} else {
				player.sendMessage("ошибка");
			}
		}
		return false;
	}
}