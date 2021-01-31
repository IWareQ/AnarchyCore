package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ThreadCache;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.EvokerBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.HuskBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.SpiderBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.WitherSkeletonBoss;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class ClearTask extends Task {
    public static int seconds = 480;

    @Override()
    public void onRun(int i) {
        if (seconds != 0) {
            seconds--;
            switch (seconds) {
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
            if (seconds <= 5) {
                Server.getInstance().getOnlinePlayers().values().forEach(players -> players.sendTip("§f§lОчистка через §6" + seconds + " §fсек§7!"));
            }
        } else {
            Server.getInstance().getLevels().values().forEach(level -> {
                level.doChunkGarbageCollection();
                level.unloadChunks(true);
                for (Entity entity : level.getEntities()) {
                    if (!(entity instanceof Player) && !(entity instanceof Wither) && !(entity.getLevel().equals(WorldSystemAPI.getSpawn()))
                            && !(entity instanceof WitherSkeletonBoss) && !(entity instanceof HuskBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof SpiderBoss)) {
                        level.removeEntity(entity);
                    }
                }
            });
            ThreadCache.clean();
            System.gc();
            Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка успешно завершена§7!");
            seconds = 480;
        }
    }
}