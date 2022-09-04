package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

import java.util.concurrent.CompletableFuture;

public class BorderBuildCommand extends Command {

	private static final Integer[] BORDER = new Integer[]{-2000, 2000};

	public BorderBuildCommand() {
		super("borderbuild", "§rПостроить границу мира");
		this.setPermission("Command.BorderBuild");
		this.commandParameters.clear();
		this.commandParameters.put("borderbuild", new CommandParameter[]{CommandParameter.newEnum("world", new CommandEnum("worlds", "map", "nether", "the_end"))});
	}

	private static void generateBorder(Level level) {
		if (level == null) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			for (int x = BORDER[0]; x <= BORDER[1]; x++) {
				if (!level.isChunkLoaded(x, BORDER[0])) {
					level.loadChunk(x, BORDER[0], false);
				}
			}
			for (int x = BORDER[0]; x <= BORDER[1]; x++) {
				if (level.getBlock(new Vector3(x, 0, BORDER[0])).getId() != BlockID.OBSIDIAN) {
					level.setBlockAt(x, 0, BORDER[0], BlockID.OBSIDIAN);
				}
			}

			for (int x = BORDER[0]; x <= BORDER[1]; x++) {
				if (!level.isChunkLoaded(x, BORDER[1])) {
					level.loadChunk(x, BORDER[1], false);
				}
			}
			for (int x = BORDER[0]; x <= BORDER[1]; x++) {
				if (level.getBlock(new Vector3(x, 0, BORDER[1])).getId() != BlockID.OBSIDIAN) {
					level.setBlockAt(x, 0, BORDER[1], BlockID.OBSIDIAN);
				}
			}


			for (int z = BORDER[0]; z <= BORDER[1]; z++) {
				level.loadChunk(BORDER[0], z, false);
			}
			for (int z = BORDER[0]; z <= BORDER[1]; z++) {
				if (level.getBlock(new Vector3(BORDER[0], 0, z)).getId() != BlockID.OBSIDIAN) {
					level.setBlockAt(BORDER[0], 0, z, BlockID.OBSIDIAN);
				}
			}

			for (int z = BORDER[0]; z <= BORDER[1]; z++) {
				level.loadChunk(BORDER[1], z, false);
			}
			for (int z = BORDER[0]; z <= BORDER[1]; z++) {
				if (level.getBlock(new Vector3(BORDER[1], 0, z)).getId() != BlockID.OBSIDIAN) {
					level.setBlockAt(BORDER[1], 0, z, BlockID.OBSIDIAN);
				}
			}
		});
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
		Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

			@Override
			public void onRun(int currentTick) {
				generateBorder(Server.getInstance().getLevelByName(levelName));
			}
		}, 20);
		return false;
	}
}