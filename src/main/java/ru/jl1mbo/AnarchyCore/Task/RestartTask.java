package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RestartTask extends Task {
    public static int seconds = 3600;

    @Override()
    public void onRun(int i) {
        if (seconds != 0) {
            seconds--;
            if (seconds == 600) {
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fминут!");
            } else if (seconds == 300) {
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §65 §fминут!");
            } else if (seconds == 60) {
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §61 §fминуту!");
            } else if (seconds == 30) {
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §630 §fсекунд!");
            } else if (seconds == 10) {
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fсекунд!");
            }
            if (seconds <= 5) {
                Server.getInstance().getOnlinePlayers().values().forEach(players -> players.sendTitle("§l§fПерезагрузка", "§f§lСервер перезагрузится через §6" + seconds + Utils.getSecond(seconds), 0, 20, 0));
            }
        } else {
            for (Player players : Server.getInstance().getOnlinePlayers().values()) {
                CombatLoggerAPI.removeCombat(players);
                players.close("", "§l§6Перезагрузка");
            }
            Server.getInstance().shutdown();
        }
    }
}