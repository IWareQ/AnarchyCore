package ru.jl1mbo.AnarchyCore.Modules.Commands;

import java.util.concurrent.CompletableFuture;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;

public class BorderBuildCommand extends Command {
	private static Integer[] BORDER = new Integer[] {-2000, 2000};

	public BorderBuildCommand() {
		super("borderbuild", "§rПостроить границу мира");
		this.setPermission("Command.BorderBuild");
		this.commandParameters.clear();
		this.commandParameters.put("borderbuild", new CommandParameter[] {CommandParameter.newEnum("world", new CommandEnum("worlds", "map", "nether", "the_end"))});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage("§l§6• §rВведите §6название §fмира§7!");
			return false;
		}
		String levelName = args[0];
		long start = System.currentTimeMillis() / 1000L;
		generateBorder(Server.getInstance().getLevelByName(levelName));
		sender.sendMessage("§l§6• §rГраница в мире §6 " + levelName + " §fпостроенна за §6" + (double)(start - System.currentTimeMillis() / 1000L) + " §fсекунд§7!");
		return false;
	}

	private static void generateBorder(Level level) {
		if (level == null) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			for (int x = BORDER[0]; x <= BORDER[1]; x++) {
				for (int z = BORDER[0]; z <= BORDER[1]; z++) {
					level.setBlockAt(x, 100, BORDER[0], BlockID.BORDER_BLOCK);
					level.setBlockAt(x, 100, BORDER[1], BlockID.BORDER_BLOCK);
					level.setBlockAt(BORDER[0], 100, z, BlockID.BORDER_BLOCK);
					level.setBlockAt(BORDER[1], 100, z, BlockID.BORDER_BLOCK);
				}
			}
		});
	}
}