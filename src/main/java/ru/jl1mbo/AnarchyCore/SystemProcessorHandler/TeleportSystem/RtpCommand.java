package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RtpCommand extends Command {
	private static int seconds = 5;

	public RtpCommand() {
		super("rtp", "§r§fТелепортироваться в случайное место");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getLevel().equals(WorldSystemAPI.getMap()) || player.getLevel().equals(WorldSystemAPI.getSpawn())) {
				Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

					@Override()
					public void onRun(int currentTick) {
						if (seconds != 0) {
							seconds--;
							if (seconds <= 5) {
								player.sendPopup("§r§fВы будете телепортированы через §6" + seconds + Utils.getSecond(seconds));
							}
						} else {
							if (!CombatLoggerAPI.inCombat(player)) {
								WorldSystemAPI.findRandomSafePosition(WorldSystemAPI.getMap(), position -> {
									player.teleport(position.setLevel(WorldSystemAPI.getMap()));
									player.sendMessage("§l§6• §r§fВы успешно телепортировались в рандомное место§7!");
								});
							}
							this.cancel();
							seconds = 5;
						}
					}
				}, 20);
			} else {
				player.sendMessage(TpaCommand.PREFIX + "§fС этого измерения запрещено телепортироваться§7!");
			}
		}
		return false;
	}
}