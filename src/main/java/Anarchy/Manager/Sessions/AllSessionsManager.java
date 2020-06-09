package Anarchy.Manager.Sessions;

import java.util.Map;

import Anarchy.Manager.Sessions.Session.PlayerSession;

public class AllSessionsManager {
	public static void saveAllSessions() {
		for (Map.Entry<String, PlayerSession> entry: PlayerSessionManager.PLAYER_SESSION.entrySet()) {
			entry.getValue().saveSession();
		}
	}
}