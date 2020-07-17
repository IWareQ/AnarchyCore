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
	public static String PREFIX = "§7(§6Привилегии§7) §r";
	
	public static void register() {
		registerGroups();
		registerGroupsAllows();
	}
	
	private static void registerGroups() {
		GROUPS.add("§7Игрок§r"); // 0
		GROUPS.add("§6Вип§r"); // 1
		GROUPS.add("§aПремиум§r"); // 2
		GROUPS.add("§dEnigma§r"); // 3
		GROUPS.add("§cHydra§r"); // 4
		GROUPS.add("§eMerlin§r"); // 5
		GROUPS.add("§1Модератор§r"); // 6
		GROUPS.add("§4Администратор§r"); // 7
		GROUPS.add("§eSupport§r"); // 8
	}
	
	private static void registerGroupsAllows() {
		GROUP_ALLOWS.put(0, new GroupAllow(2, 0)); // 0
		GROUP_ALLOWS.put(1, new GroupAllow(3, 0)); // 1
		GROUP_ALLOWS.put(2, new GroupAllow(3, 0)); // 2
		GROUP_ALLOWS.put(3, new GroupAllow(3, 1)); // 3
		GROUP_ALLOWS.put(4, new GroupAllow(4, 1)); // 4
		GROUP_ALLOWS.put(5, new GroupAllow(5, 2)); // 5
		GROUP_ALLOWS.put(6, new GroupAllow(9, 9)); // 6
		GROUP_ALLOWS.put(7, new GroupAllow(9, 9)); // 7
		GROUP_ALLOWS.put(8, new GroupAllow(9, 9)); // 8
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
		String tag = GROUPS.get(playerSession.getInteger("Permission")) + " §f" + playerName;
		player.setNameTag(tag);
		player.setDisplayName(tag);
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
			permissionAllows.put("Command.Repair", true);
			break;
			
			case 3: 
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			break;
			
			case 4: 
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			break;
			
			case 5: 
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			break;
			
			case 6: 
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Access.Admin", true);
			permissionAllows.put("Command.A", true);
			permissionAllows.put("Command.Gamemode", true);
			break;
			
			case 7: 
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.SeeMoney", true);
			permissionAllows.put("Access.Admin", true);
			permissionAllows.put("Command.A", true);
			permissionAllows.put("Command.Gamemode", true);
			break;
			
			case 8:
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.SeeMoney", true);
			permissionAllows.put("Access.Admin", true);
			permissionAllows.put("Command.A", true);
			permissionAllows.put("Command.Gamemode", true);
		}
		for (Map.Entry<String, Boolean> entry : permissionAllows.entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}