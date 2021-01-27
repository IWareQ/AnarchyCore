package AnarchySystem.Components.AutoRestartAndClearServer.Task;

import AnarchySystem.Components.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class RestartTask extends Task {
    public static int TIMER = 3600;

    @Override()
    public void onRun(int currentTick) {
        switch (TIMER) {
            case 600:
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fминут!");
                break;
            case 300:
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §65 §fминут!");
                break;
            case 60:
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §61 §fминуту!");
                break;
            case 30:
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §630 §fсекунд!");
                break;
            case 10:
                Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §r§fСервер перезагрузится через §610 §fсекунд!");
                break;
        }
        if (TIMER <= 10) {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                player.sendTitle("§l§fПерезагрузка", "§f§lСервер перезагрузится через §6" + TIMER + " §f" + getSecond(TIMER), 0, 20, 0);
                player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
            }
            if (TIMER == 0) {
                for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                    CombatLoggerAPI.removeCombat(player);
                    player.close("", "§l§6Перезагрузка");
                }
                Server.getInstance().shutdown();
                return;
            }
        }
        TIMER--;
    }

    private String getSecond(int second) {
        int preLastDigit = second % 100 / 10;
        if (preLastDigit == 1) {
            return "секунд";
        }
        if (second % 10 == 1) {
            return "секунду";
        } else if (second % 10 == 2 || second % 10 == 3 || second % 10 == 4) {
            return "секунды";
        }
        return "секунд";
    }
}