package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class TestCommand extends Command {
	
	public TestCommand() {
		super("test", "§l§fТестовая команда");
		setPermission("Command.Test");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Test")) {
			return false;
		}
		Position position = player.getPosition();
		Entity entity = Entity.createEntity("SilverfishBoss", position);
		entity.setScale((float)2.0);
		entity.spawnToAll();
		player.sendMessage("§l§a| §r§fЧто-то произошло");
		return false;
	}
}