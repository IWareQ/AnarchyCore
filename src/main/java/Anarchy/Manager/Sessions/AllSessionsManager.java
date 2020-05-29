package Anarchy.Manager.Sessions;

import Anarchy.Manager.Sessions.Session.PlayerSession;

import java.util.Map;

public class AllSessionsManager {
	public static void saveAllSessions() {
		for (Map.Entry<String, PlayerSession> entry: PlayerSessionManager.PLAYER_SESSION.entrySet()) {
			entry.getValue().saveSession();
		}
	}
}