package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class TestCommand extends Command {

	public TestCommand() {
		super("test", "§l§fТестовая команда");
		this.setPermission("Command.Test");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Test")) {
			return false;
		}
		if (args.length == 0) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6test §31§7/§32§7/§33§7/§34");
			return true;
		}
		switch (args[0]) {
		case "1": {
			Position position = player.getPosition();
			Entity entity = Entity.createEntity("SilverfishBoss", position);
			entity.setScale((float)5.0);
			entity.spawnToAll();
			player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: " + entity.getPosition());
		}
		break;

		case "2": {
			Position position = player.getPosition();
			Entity entity = Entity.createEntity("RavagerBoss", position);
			entity.setScale((float)1.0);
			entity.spawnToAll();
			player.sendMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: " + entity.getPosition());
		}
		break;

		case "3": {
			player.sendMessage("Скоро");
		}
		break;

		case "4": {
			player.sendMessage("Скоро");
		}
		break;

		}
		return false;
	}
}