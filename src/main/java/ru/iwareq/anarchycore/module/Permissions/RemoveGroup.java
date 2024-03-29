package ru.iwareq.anarchycore.module.Permissions;

import lombok.extern.log4j.Log4j2;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class RemoveGroup {

	public static final Map<String, Long> USERS = new ConcurrentHashMap<>();

	public static void init() {
		USERS.putAll(AuthAPI.getAllTimeGroup());
	}

	public static void save() {
		USERS.forEach(AuthAPI::updateTimeGroup);
	}

	public static void add(String playerName, long seconds) {
		USERS.put(playerName.toLowerCase(), seconds);
	}

	public static long getTime(String playerName) {
		return USERS.getOrDefault(playerName.toLowerCase(), -1L);
	}
}
