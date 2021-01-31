package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Commands.GroupCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener.PlayerChatListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Utils.GroupAllow;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class PermissionAPI {
	private static final Map<Integer, GroupAllow> GROUP_ALLOWS = new HashMap<>();
	public static ArrayList<String> GROUPS = new ArrayList<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerGroups();
		registerGroupsAllows();
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerChatListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new GroupCommand());
	}

	private static void registerGroups() {
		GROUPS.add("§7Игрок §f");
		GROUPS.add("§eГерой §f");
		GROUPS.add("§3Страж §f");
		GROUPS.add("§7Лорд §f");
		GROUPS.add("§6Принц §f");
		GROUPS.add("§eКороль §f");
		GROUPS.add("§cYou§fTube §f");
		GROUPS.add("§2Титан §f");
		GROUPS.add("§cБог §f");
		GROUPS.add("§eТартар §f");
		GROUPS.add("§bПомощник §f");
		GROUPS.add("§aМодератор §f");
		GROUPS.add("§4Админ §f");
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
	}

	public static boolean isGroup(int groupID) {
		return groupID < 0 || groupID >= PermissionAPI.GROUPS.size();
	}

	public static GroupAllow getGroupAllows(int permissionGroup) {
		return GROUP_ALLOWS.get(permissionGroup);
	}

	public static int getGroup(String playerName) {
		return ConfigUtils.getPermissionConfig().getInt(playerName.toLowerCase(), 0);
	}

	public static void setGroup(String playerName, int groupID) {
		Config config = ConfigUtils.getPermissionConfig();
		config.set(playerName.toLowerCase(), groupID);
		config.save();
		config.reload();
	}

	public static void updateTag(Player player) {
		String device = String.valueOf(player.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4",
						"FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
		String nameTag = GROUPS.get(getGroup(player.getName())) + player.getName() + "\n§7" + device;
		player.setNameTag(nameTag);
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment permissionAttachment = player.addAttachment(Main.getInstance(), player.getName());
		permissionAttachment.clearPermissions();
		Map<String, Boolean> permissionAllows = new HashMap<>();
		int permissionGroup = getGroup(player.getName());
		switch (permissionGroup) {
		case 0:
			break;
		case 1:
			permissionAllows.put("Command.CraftingTable", true);
			break;
		case 2:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			break;
		case 3:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			break;
		case 4:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			break;
		case 5:
		case 6:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			break;
		case 7:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.Near", true);
			break;
		case 8:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Near.100", true);
			break;
		case 9:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Near.150", true);
			permissionAllows.put("Command.Bonus", true);
			break;
		case 10:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			break;
		case 11:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.Inventory", true);
			permissionAllows.put("Command.ClearChat", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			permissionAllows.put("Command.Ban", true);
			break;
		case 12:
			permissionAllows.put("Command.CraftingTable", true);
			permissionAllows.put("Command.Food", true);
			permissionAllows.put("Command.Heal", true);
			permissionAllows.put("Command.ClearInventory", true);
			permissionAllows.put("Command.Day", true);
			permissionAllows.put("Command.Night", true);
			permissionAllows.put("Command.Repair", true);
			permissionAllows.put("Repair.All", true);
			permissionAllows.put("Command.EnderChest", true);
			permissionAllows.put("Command.NightVision", true);
			permissionAllows.put("Command.Near", true);
			permissionAllows.put("Near.150", true);
			permissionAllows.put("Command.Bonus", true);
			permissionAllows.put("Command.Spectate", true);
			permissionAllows.put("Command.Inventory", true);
			permissionAllows.put("AdminChat", true);
			permissionAllows.put("Command.Mute", true);
			permissionAllows.put("Command.UnMute", true);
			permissionAllows.put("Command.Ban", true);
			permissionAllows.put("Command.Kick", true);
			permissionAllows.put("Command.UnBan", true);
			break;
		}
		for (Map.Entry<String, Boolean> entry : permissionAllows.entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}