package AnarchySystem.Manager.Scoreboard;

import AnarchySystem.Manager.Scoreboard.Network.Scoreboard;
import cn.nukkit.Player;

public class ScoreboardAPI {

    public static Scoreboard createScoreboard() {
        return new Scoreboard();
    }

    public static void setScoreboard(Player player, Scoreboard scoreboard) {
        scoreboard.showFor(player);
    }

    public static void removeScorebaord(Player player, Scoreboard scoreboard) {
        scoreboard.hideFor(player);
    }
}