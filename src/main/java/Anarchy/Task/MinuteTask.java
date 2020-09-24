package Anarchy.Task;

import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class MinuteTask extends Task {
	private static int TIMER_BROADCAST = 0;
	private static int TIMER_SAVEDATA = 0;
	public static int TIMER_RESTART = 60;
	public static int SECONDS_RESTART = 60;
	
	@Override()
	public void onRun(int i) {
		AuctionAPI.updateAuction();
		if (TIMER_RESTART == 10) {
			Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §310 §fминут!");
		} else if (TIMER_RESTART == 1) {
			Server.getInstance().getScheduler().scheduleRepeatingTask(new RestartTask(), 20);
			AllSessionsManager.saveAllSessions();
			this.cancel();
			return;
		}
		if (TIMER_BROADCAST == 0) {
			TIMER_BROADCAST = 3;
			Server.getInstance().broadcastMessage(Broadcast.getBroadcast());
		}
		if (TIMER_SAVEDATA == 0) {
			TIMER_SAVEDATA = 5;
			AllSessionsManager.saveAllSessions();
		}
		--TIMER_BROADCAST;
		--TIMER_SAVEDATA;
		--TIMER_RESTART;
	}
}