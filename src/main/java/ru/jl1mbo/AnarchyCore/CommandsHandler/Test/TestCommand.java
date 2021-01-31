package ru.jl1mbo.AnarchyCore.CommandsHandler.Test;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;

public class TestCommand extends Command {

	public TestCommand() {
		super("test", "§r§fПолучить бонус");
		this.setPermission("Command.Test");
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