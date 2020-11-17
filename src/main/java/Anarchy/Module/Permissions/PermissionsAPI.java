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
	private static Map<Integer, GroupAllow> GROUP_ALLOWS = new HashMap<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerGroups();
		registerGroupsAllows();
	}

	private static void registerGroups() {
		GROUPS.add("§7Игрок§r");
		GROUPS.add("§eГерой§r");
		GROUPS.add("§3Страж§r");
		GROUPS.add("§7Лорд§r");
		GROUPS.add("§6Принц§r");
		GROUPS.add("§eКороль§r");
		GROUPS.add("§cYou§fTube§r");
		GROUPS.add("§bТитан§r");
		GROUPS.add("§bБог§r");
		GROUPS.add("§bТартар§r");
		GROUPS.add("§bПомощник§r");
		GROUPS.add("§1Модератор§r");
		GROUPS.add("§4Администратор§r");
		GROUPS.add("§6Основатель§r");
	}

	private static void registerGroupsAllows() {
		GROUP_ALLOWS.put(0, new GroupAllow(2));
		GROUP_ALLOWS.put(1, new GroupAllow(2));
		GROUP_ALLOWS.put(2, new GroupAllow(3));
		GROUP_ALLOWS.put(3, new GroupAllow(3));
		GROUP_ALLOWS.put(4, new GroupAllow(4));
		GROUP_ALLOWS.put(5, new GroupAllow(4));
		GROUP_ALLOWS.put(6, new GroupAllow(4));
		GROUP_ALLOWS.put(7, new GroupAllow(5));
		GROUP_ALLOWS.put(8, new GroupAllow(6));
		GROUP_ALLOWS.put(9, new GroupAllow(8));
		GROUP_ALLOWS.put(10, new GroupAllow(5));
		GROUP_ALLOWS.put(11, new GroupAllow(6));
		GROUP_ALLOWS.put(12, new GroupAllow(7));
		GROUP_ALLOWS.put(13, new GroupAllow(10));
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
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			return PlayerSessionManager.getPlayerSession(playerName).getInteger("Permission");
		} else {
			return SQLiteUtils.selectInteger("Users.db", "SELECT Permission FROM USERS WHERE UPPER (Username) = \'" + playerName.toUpperCase() + "\';");
		}
	}

	public static void setGroup(String playerName, int groupID) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			PlayerSessionManager.getPlayerSession(playerName).setInteger("Permission", groupID);
			updatePermissions(Server.getInstance().getPlayerExact(playerName));
		} else {
			SQLiteUtils.query("Users.db", "UPDATE USERS SET Permission = \'" + groupID + "\' WHERE UPPER(Username) = \'" + playerName.toUpperCase() + "\';");
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
		case 0: 
			break;

		case 1:
			permissionAllows.put("Command.Food", true);
			break;

		case 2:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			break;

		case 3:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			break;

		case 4:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			break;

		case 5:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			break;

		case 6:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.Coordinate", true);
			break;

		case 7:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			break;

		case 8:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.CraftingTable", true);
			break;

		case 9:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Bonus", true);
			break;

		case 10:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			break;

		case 11:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.Inventory", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			permissionAllows.put("Command.Ban", true);
			permissionAllows.put("Command.UnBan", true);
			permissionAllows.put("Command.Kick", true);
			break;

		case 12:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.Inventory", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			permissionAllows.put("Command.Ban", true);
			permissionAllows.put("Command.UnBan", true);
			permissionAllows.put("Command.Kick", true);
			break;

		case 13:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.DonateShop", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.Inventory", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			permissionAllows.put("Command.Ban", true);
			permissionAllows.put("Command.UnBan", true);

		}
		for (Map.Entry<String, Boolean> entry : permissionAllows.entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}