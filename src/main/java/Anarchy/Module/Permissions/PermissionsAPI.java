package Anarchy.Module.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.Utils.GroupAllow;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.permission.PermissionAttachment;

public class PermissionsAPI {
	public static ArrayList<String> GROUPS = new ArrayList<>();
	public static Map<Integer, GroupAllow> GROUP_ALLOWS = new HashMap<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerGroups();
		registerGroupsAllows();
	}

	private static void registerGroups() {
		GROUPS.add("§7Игрок§r"); // 0
		GROUPS.add("§eГерой§r"); // 1
		GROUPS.add("§3Страж§r"); // 2
		GROUPS.add("§7Лорд§r"); // 3
		GROUPS.add("§6Принц§r"); // 4
		GROUPS.add("§eКороль§r"); // 5
		GROUPS.add("§cYou§fTube§r"); // 6
		GROUPS.add("§bТитан§r"); // 7
		GROUPS.add("§bБог§r"); // 8
		GROUPS.add("§bТарТар§r"); // 9
		GROUPS.add("§bПомощник§r"); // 10
		GROUPS.add("§1Модератор§r"); // 11
		GROUPS.add("§4Администратор§r"); // 12
		GROUPS.add("§6Основатель§r"); // 13
	}

	private static void registerGroupsAllows() {
		GROUP_ALLOWS.put(0, new GroupAllow(2)); // 0
		GROUP_ALLOWS.put(1, new GroupAllow(2)); // 1
		GROUP_ALLOWS.put(2, new GroupAllow(3)); // 2
		GROUP_ALLOWS.put(3, new GroupAllow(3)); // 3
		GROUP_ALLOWS.put(4, new GroupAllow(4)); // 4
		GROUP_ALLOWS.put(5, new GroupAllow(4)); // 5
		GROUP_ALLOWS.put(6, new GroupAllow(4)); // 6
		GROUP_ALLOWS.put(7, new GroupAllow(4)); // 7
		GROUP_ALLOWS.put(8, new GroupAllow(5)); // 8
		GROUP_ALLOWS.put(9, new GroupAllow(6)); // 9
		GROUP_ALLOWS.put(10, new GroupAllow(5)); // 10
		GROUP_ALLOWS.put(11, new GroupAllow(5)); // 11
		GROUP_ALLOWS.put(13, new GroupAllow(5)); // 12
		GROUP_ALLOWS.put(13, new GroupAllow(5)); // 13
	}

	public static boolean isGroup(int groupID) {
		return groupID >= 0 && groupID < PermissionsAPI.GROUPS.size();
	}

	public static int getGroup(Player player) {
		return PlayerSessionManager.getPlayerSession(player).getInteger("Permission");
	}

	public static GroupAllow getGroupAllows(int permissionGroup) {
		return GROUP_ALLOWS.get(permissionGroup);
	}

	public static int getGroup(String playerName) {
		Player player = Server.getInstance().getPlayerExact(playerName);
		if (player != null) {
			return PlayerSessionManager.getPlayerSession(player).getInteger("Permission");
		} else {
			return SQLiteUtils.selectInteger("Users.db", "SELECT `Permission` FROM `USERS` WHERE UPPER (`Username`) = \'" + playerName.toUpperCase() + "\';");
		}
	}

	public static void setGroup(String playerName, int groupID) {
		Player player = Server.getInstance().getPlayerExact(playerName);
		if (player != null) {
			PlayerSessionManager.getPlayerSession(playerName).setInteger("Permission", groupID);
			updatePermissions(player);
		} else {
			SQLiteUtils.query("Users.db", "UPDATE `USERS` SET `Permission` = " + groupID + " WHERE UPPER(`Username`) = \'" + playerName.toUpperCase() + "\';");
		}
	}

	public static void setGroup(Player player, int groupID) {
		PlayerSessionManager.getPlayerSession(player.getName()).setInteger("Permission", groupID);
		updatePermissions(player);
	}

	public static void updateTag(Player player) {
		String playerName = player.getName();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player.getName());
		String device = String.valueOf(player.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4", "FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		String nameTag = GROUPS.get(playerSession.getInteger("Permission")) + " §f" + playerName + "\n§7" + device;
		player.setNameTag(nameTag);
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment permissionAttachment = player.addAttachment(AnarchyMain.plugin, player.getName());
		permissionAttachment.clearPermissions();
		Map<String, Boolean> permissionAllows = new HashMap<>();
		int permissionGroup = getGroup(player);
		switch (permissionGroup) {
		case 0: // Игрок
			break;

		case 1: // Герой
			permissionAllows.put("Command.Food", true);
			break;

		case 2: // Страж
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			break;

		case 3: // Лорд
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			break;

		case 4: // Принц
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			break;

		case 5: // Король
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			break;

		case 6: // YouTube
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Coordinate", true);
			break;

		case 7: // Титан
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			break;

		case 8: // Бог
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			break;

		case 9: // ТарТар
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			break;

		case 10: // Помощник
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("bansystem.command.checkmute", true);
			permissionAllows.put("bansystem.command.mute", true);
			permissionAllows.put("bansystem.command.mutelog", true);
			break;

		case 11: // Модератор
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("bansystem.command.checkmute", true);
			permissionAllows.put("bansystem.command.mute", true);
			permissionAllows.put("bansystem.command.mutelog", true);
			permissionAllows.put("bansystem.command.checkban", true);
			permissionAllows.put("bansystem.command.ban", true);
			permissionAllows.put("bansystem.command.banlog", true);
			permissionAllows.put("Command.Spectate", true);
			break;

		case 12: // Администратор
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Near", true);
			break;

		case 13: // Основатель
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Near", true);

		}
		for (Map.Entry<String, Boolean> entry : permissionAllows.entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}