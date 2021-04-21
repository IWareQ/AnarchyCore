package ru.jl1mbo.AnarchyCore.Manager.Scoreboard;

import cn.nukkit.Player;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.Scoreboard;

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