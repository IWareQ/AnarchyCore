package Anarchy.Manager.Sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Anarchy.Manager.Sessions.Session.PlayerSession;
import ScoreboardPlugin.Network.Scoreboard;
import cn.nukkit.Player;

public class PlayerSessionManager {
	public static ArrayList<String> SCOREBOARD = new ArrayList<>();
	public static Map<String, Scoreboard> SCOREBOARDS = new HashMap<>();
	public static Map<String, PlayerSession> PLAYER_SESSION = new HashMap<>();
	
	public static PlayerSession getPlayerSession(Player player) {
		return PLAYER_SESSION.get(player.getName().toUpperCase());
	}
	
	public static PlayerSession getPlayerSession(String playerName) {
		return PLAYER_SESSION.get(playerName.toUpperCase());
	}
	
	public static boolean hasPlayerSession(Player player) {
		return PLAYER_SESSION.containsKey(player.getName().toUpperCase());
	}
	
	public static boolean hasPlayerSession(String playerName) {
		return PLAYER_SESSION.containsKey(playerName.toUpperCase());
	}
	
	public static void startPlayerSession(Player player) {
		PLAYER_SESSION.put(player.getName().toUpperCase(), new PlayerSession(player.getName()));
	}
	
	public static void stopPlayerSession(Player player) {
		String playerName = player.getName().toUpperCase();
		PLAYER_SESSION.get(playerName).saveSession();
		PLAYER_SESSION.remove(playerName);
	}
	
	public static void stopPlayerSession(String playerName) {
		playerName = playerName.toUpperCase();
		PLAYER_SESSION.get(playerName).saveSession();
		PLAYER_SESSION.remove(playerName);
	}
}