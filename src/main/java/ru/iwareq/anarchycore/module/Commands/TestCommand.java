package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.AnarchyCore;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.task.ClearTask;

import java.util.concurrent.CompletableFuture;

public class TestCommand extends Command {

	public TestCommand() {
		super("test", "§rТестовая команда");
		this.setPermission("Command.Test");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}

		AnarchyCore.getInstance().getClearTask().setSeconds(30);
		/*Config config = new Config(Main.getInstance().getDataFolder() + "/Users.yml", Config.YAML);
		for (Entry<String, Object> entry : config.getAll().entrySet()) {
			PermissionAPI.setGroup(entry.getKey(), (String)entry.getValue());
		}
		sender.sendMessage("готово");*/
		/*CompletableFuture.runAsync(() -> {
			long start = System.currentTimeMillis() / 1000L;
			for (int x = -2000; x <= 2000; x++) {
				WorldSystemAPI.TheEnd.setBlockAt(x, 0, 0, BlockID.OBSIDIAN);
			}
			long stop = start - System.currentTimeMillis() / 1000L;
			sender.sendMessage("готово за " + stop);
		});*/
		return false;
	}
}