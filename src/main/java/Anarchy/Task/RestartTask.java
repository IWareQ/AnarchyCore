package Anarchy.Task;

//import AuctionAPI.Auction.AuctionAPI;
import CombatLoggerAPI.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class RestartTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		if (MinuteTask.SECONDS_RESTART == 60) {
			Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §61 §fминуту!");
		} else if (MinuteTask.SECONDS_RESTART == 30) {
			Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §630 §fсекунд!");
		} else if (MinuteTask.SECONDS_RESTART == 10) {
			Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fсекунд!");
		}
		if (MinuteTask.SECONDS_RESTART <= 10) {
			if (MinuteTask.SECONDS_RESTART == 0) {
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					CombatLoggerAPI.removeCombat(player);
					//AuctionAPI.saveAuction();
					player.close("", "§l§6Перезагрузка");
				}
				Server.getInstance().shutdown();
				return;
			}
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTitle("§l§fПерезагрузка", "§f§lСервер перезагрузится через §6" + MinuteTask.SECONDS_RESTART + " §f" + getSecond(MinuteTask.SECONDS_RESTART), 0, 20, 0);
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
		}
		MinuteTask.SECONDS_RESTART--;
	}

	private String getSecond(int second) {
		int preLastDigit = second % 100 / 10;
		if (preLastDigit == 1) {
			return "секунд";
		}
		switch (second % 10) {
		case 1:
			return "секунду";

		case 2:

		case 3:

		case 4:
			return "секунды";

		default:
			return "секунд";

		}
	}
}