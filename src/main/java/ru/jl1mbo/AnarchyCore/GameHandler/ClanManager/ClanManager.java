package ru.jl1mbo.AnarchyCore.GameHandler.ClanManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.Commands.ClanCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.EventsListener.EntityDamageByEntityListener;

public class ClanManager {
	public static String PREFIX = "§l§7(§3Кланы§7) §r";
	public static Config playerConfig;
	public static Config clanConfig;

	public static void registe() {
		Server.getInstance().getPluginManager().registerEvents(new EntityDamageByEntityListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new ClanCommand());
		playerConfig = new Config(Main.getInstance().getDataFolder() + "/ClanSystem/PlayersData.yml", Config.YAML);
		clanConfig = new Config(Main.getInstance().getDataFolder() + "/ClanSystem/ClansData.yml", Config.YAML);
	}

	public static void registerPlayer(Player player) {
		if (!isRegister(player.getName())) {
			playerConfig.set(player.getName() + ".Requests", new ArrayList<String>());
			playerConfig.set(player.getName() + ".Role", "Участник");
			playerConfig.set(player.getName() + ".Clan", "null");
			playerConfig.save();
			playerConfig.reload();
		}
	}

	public static boolean isRegister(String playerName) {
		return playerConfig.exists(playerName);
	}

	public static boolean clanNameExists(String clanName) {
		for (Entry<String, Object> entry : clanConfig.getAll().entrySet()) {
			if (entry.getKey().toLowerCase().equals(clanName.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public static void leaveClan(String playerName, String clanName) {
		List<String> members = getClanMembers(clanName);
		members.remove(playerName);
		clanConfig.set(clanName + ".Members", members);
		clanConfig.save();
		clanConfig.reload();

		playerConfig.set(playerName + ".Role", "Участник");
		playerConfig.set(playerName + ".Clan", "null");
		playerConfig.save();
		playerConfig.reload();
	}

	public static void createClan(Player player, String clanName) {
		ArrayList<String> members = new ArrayList<>();
		members.add(player.getName());
		clanConfig.set(clanName + ".Requests", new ArrayList<String>());
		clanConfig.set(clanName + ".Members", members);
		clanConfig.save();
		clanConfig.reload();

		playerConfig.set(player.getName() + ".Clan", clanName);
		playerConfig.set(player.getName() + ".Role", "Глава");
		playerConfig.save();
		playerConfig.reload();
	}

	public static void changeRolePlayer(String playerName, String role) {
		playerConfig.set(playerName + ".Role", role);
		playerConfig.save();
		playerConfig.reload();
	}

	public static void sendRequestsClan(Player player, String clanName) {
		List<String> clanRequests = getClanRequests(clanName);
		clanRequests.add(player.getName());
		clanConfig.set(clanName + ".Requests", clanRequests);
		clanConfig.save();
		clanConfig.reload();

		List<String> playerRequests = getPlayerRequests(player.getName());
		playerRequests.add(clanName);
		playerConfig.set(player.getName() + ".Requests", playerRequests);
		playerConfig.save();
		playerConfig.reload();
	}

	public static void acceptRequestClan(Player player, String clanName) {
		List<String> clanRequests = getClanRequests(clanName);
		List<String> members = getClanMembers(clanName);
		members.add(player.getName());
		clanRequests.remove(player.getName());
		clanConfig.set(clanName + ".Members", members);
		clanConfig.set(clanName + ".Requests", clanRequests);
		clanConfig.save();
		clanConfig.reload();

		List<String> playerRequests = getPlayerRequests(player.getName());
		playerRequests.remove(clanName);
		playerConfig.set(player.getName() + ".Requests", playerRequests);
		playerConfig.set(player.getName() + ".Clan", clanName);
		playerConfig.save();
		playerConfig.reload();
	}

	public static void removeRequestClan(String playerName, String clanName) {
		List<String> playerRequests = getPlayerRequests(playerName);
		playerRequests.remove(clanName);
		playerConfig.set(playerName + ".Requests", playerRequests);
		playerConfig.save();
		playerConfig.reload();

		List<String> clanRequests = getClanRequests(clanName);
		clanRequests.remove(playerName);
		clanConfig.set(clanName + ".Requests", clanRequests);
		clanConfig.save();
		clanConfig.reload();
	}

	public static void deleteClan(String clanName) {
		getClanMembers(clanName).forEach(players -> {
			playerConfig.set(players + ".Clan", "null");
			playerConfig.set(players + ".Role", "Участник");
			playerConfig.save();
			playerConfig.reload();
		});
		clanConfig.remove(clanName);
		clanConfig.save();
		clanConfig.reload();
	}

	public static List<String> getPlayerRequests(String playerName) {
		return playerConfig.getStringList(playerName + ".Requests");
	}

	public static String getPlayerRole(String playerName) {
		return playerConfig.getString(playerName + ".Role");
	}

	public static String getPlayerClan(String playerName) {
		return playerConfig.getString(playerName + ".Clan");
	}

	public static List<String> getClanRequests(String clanName) {
		return clanConfig.getStringList(clanName + ".Requests");
	}

	public static List<String> getClanMembers(String clanName) {
		return clanConfig.getStringList(clanName + ".Members");
	}

	public static boolean playerIsInClan(String playerName) {
		return !playerConfig.get(playerName + ".Clan").equals("null");
	}
}