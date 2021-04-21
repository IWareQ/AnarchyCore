package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

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
		/*Config config = new Config(Main.getInstance().getDataFolder() + "/Users.yml", Config.YAML);
		for (Entry<String, Object> entry : config.getAll().entrySet()) {
			PermissionAPI.setGroup(entry.getKey(), (String)entry.getValue());
		}
		sender.sendMessage("готово");*/
		CompletableFuture.runAsync(() -> {
			long start = System.currentTimeMillis() / 1000L;
			for (int x = -2000; x <= 2000; x++) {
				WorldSystemAPI.TheEnd.setBlockAt(x, 0, 0, BlockID.BORDER_BLOCK);
			}
			long stop = start - System.currentTimeMillis() / 1000L;
			sender.sendMessage("готово за " + stop);
		});
		return false;
	}
}