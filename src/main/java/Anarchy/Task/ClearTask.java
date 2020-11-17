package Anarchy.Task;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Module.Boss.EvokerBoss;
import Anarchy.Module.Boss.RavagerBoss;
import Anarchy.Module.Boss.SilverfishBoss;
import Anarchy.Module.Boss.SlimeBoss;
import Anarchy.Module.Boss.WitchBoss;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;

public class ClearTask extends Task {
	public static int TIMER_CLEAR = 600;

	@Override()
	public void onRun(int currentTick) {
		if (TIMER_CLEAR == 300) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §65 §fминут§7!");
		} else if (TIMER_CLEAR == 60) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §61 §fминуту§7!");
		}
		if (TIMER_CLEAR <= 10) {
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTip("§f§lОчистка через §6" + TIMER_CLEAR + " §fсек§7!");
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
			if (TIMER_CLEAR == 0) {
				for (Level level : Server.getInstance().getLevels().values()) {
					level.doChunkGarbageCollection();
					level.unloadChunks(true);
					for (Entity entity : level.getEntities()) {
						if (!(entity instanceof Player) &&  !(entity instanceof EntityMinecartChest) && !(entity instanceof Wither) && !(entity.getLevel().equals(FunctionsAPI.SPAWN)) && !(entity instanceof SilverfishBoss) && !(entity instanceof SlimeBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof RavagerBoss) && !(entity instanceof WitchBoss)) {
							level.removeEntity(entity);
						}
					}
				}
				Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка успешно завершена§7!");
				TIMER_CLEAR = 600;
			}
		}
		TIMER_CLEAR--;
	}
}