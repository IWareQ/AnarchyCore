package AnarchySystem.Components.AutoRestartAndClearServer.Task;

import AnarchySystem.Components.Bosses.Bosses.EvokerBoss;
import AnarchySystem.Components.Bosses.Bosses.HuskBoss;
import AnarchySystem.Components.Bosses.Bosses.SpiderBoss;
import AnarchySystem.Components.Bosses.Bosses.WitherSkeletonBoss;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ThreadCache;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;

public class ClearTask extends Task {
    public static int TIMER = 480;

    @Override()
    public void onRun(int currentTick) {
        switch (TIMER) {
            case 300:
                Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §65 §fминут§7!");
                break;
            case 60:
                Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §61 §fминуту§7!");
                break;
            case 10:
                Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §610 §fсекунд§7!");
                break;
        }
        if (TIMER <= 5) {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                player.sendTip("§f§lОчистка через §6" + TIMER + " §fсек§7!");
                player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
            }
            if (TIMER == 0) {
                for (Level level : Server.getInstance().getLevels().values()) {
                    level.doChunkGarbageCollection();
                    level.unloadChunks(true);
                    for (Entity entity : level.getEntities()) {
                        if (!(entity instanceof Player) && !(entity instanceof EntityMinecartChest) && !(entity instanceof Wither) && !(entity.getLevel().equals(WorldSystemAPI.getSpawn())) && !(entity instanceof WitherSkeletonBoss) && !(entity instanceof HuskBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof SpiderBoss)) {
                            level.removeEntity(entity);
                        }
                    }
                }
                ThreadCache.clean();
                System.gc();
                Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка успешно завершена§7!");
                TIMER = 480;
            }
        }
        TIMER--;
    }
}