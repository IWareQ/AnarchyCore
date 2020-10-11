package Anarchy.Task;

/*import java.util.Arrays;
import java.util.List;
import java.util.Random;*/

//import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
//import cn.nukkit.entity.Entity;
//import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;

public class MinuteTask extends Task {
	private static int TIMER_BROADCAST = 0;
	private static int TIMER_SAVEDATA = 0;
	public static int TIMER_RESTART = 60;
	//private static int TIMER_BOSS = 1;
	public static int SECONDS_RESTART = 60;

	@Override()
	public void onRun(int currentTick) {
		AuctionAPI.updateAuction();
		if (TIMER_RESTART == 10) {
			Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fминут!");
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
		/*if (TIMER_BOSS == 0) {
			TIMER_BOSS = 30;
			Random rand = new Random();
			List<String> givenList = Arrays.asList("SilverfishBoss", "WitchBoss", "SlimeBoss", "EvokerBoss", "RavagerBoss");
			for (int i = 0; i < 1; i++) {
				int randomIndex = rand.nextInt(givenList.size());
				String randomElement = givenList.get(randomIndex);
				if (randomElement.equals("SilverfishBoss")) {
					Entity entity = Entity.createEntity("SilverfishBoss", FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
					entity.setScale((float)4.0);
					entity.setNameTag("Червяк");
					entity.setNameTagAlwaysVisible();
					entity.spawnToAll();
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
				if (randomElement.equals("WitchBoss")) {
					Entity entity = Entity.createEntity("WitchBoss", FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
					entity.setScale((float)2.0);
					entity.setNameTag("Алхимик");
					entity.setNameTagAlwaysVisible();
					entity.spawnToAll();
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
				if (randomElement.equals("SlimeBoss")) {
					Entity entity = Entity.createEntity("SlimeBoss", FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
					entity.setNameTag("Слизьняк");
					entity.setNameTagAlwaysVisible();
					entity.spawnToAll();
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
				if (randomElement.equals("EvokerBoss")) {
					Entity entity = Entity.createEntity("EvokerBoss", FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
					entity.setScale((float)4.0);
					entity.setNameTag("Червяк");
					entity.setNameTagAlwaysVisible();
					entity.spawnToAll();
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
				if (randomElement.equals("RavagerBoss")) {
					Entity entity = Entity.createEntity("RavagerBoss", FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
					entity.setScale((float)1.0);
					entity.setNameTag("Зверь");
					entity.setNameTagAlwaysVisible();
					entity.spawnToAll();
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
			}
		}*/
		if (TIMER_SAVEDATA == 0) {
			TIMER_SAVEDATA = 1;
			AllSessionsManager.saveAllSessions();
		}
		--TIMER_BROADCAST;
		--TIMER_SAVEDATA;
		--TIMER_RESTART;
		//--TIMER_BOSS;
	}
}