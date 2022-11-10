package ru.iwareq.anarchycore.module.Permissions;

import cn.nukkit.Player;
import cn.nukkit.permission.PermissionAttachment;
import ru.iwareq.anarchycore.AnarchyCore;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.AristocratGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.DukeGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.EmperorGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.HellHoundGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.LordGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.PhoenixGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.WarriorGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.PlayerGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.SpecialGroup.YouTubeGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.SpecialGroup.YouTubePlusGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup.AdminGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup.HelperGroup;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.Titles;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;

public class PermissionAPI {

	public static final HashMap<String, DefaultGroup> GROUPS = new HashMap<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerGroup(new PlayerGroup());
		registerGroup(new WarriorGroup());
		registerGroup(new HellHoundGroup());
		registerGroup(new PhoenixGroup());
		registerGroup(new EmperorGroup());
		registerGroup(new DukeGroup());
		registerGroup(new LordGroup());
		registerGroup(new AristocratGroup());

		registerGroup(new YouTubeGroup());
		registerGroup(new YouTubePlusGroup());

		registerGroup(new HelperGroup());
		registerGroup(new AdminGroup());
	}

	private static void registerGroup(DefaultGroup group) {
		GROUPS.put(group.getGroupId(), group);
	}

	public static boolean isGroup(String groupId) {
		return GROUPS.containsKey(groupId);
	}

	public static long getTimeGroup(String playerName) {
		return AuthAPI.getTimeGroup(playerName);
	}

	public static DefaultGroup getGroup(String groupId) {
		return GROUPS.get(groupId);
	}

	public static DefaultGroup getPlayerGroup(String playerName) {
		return GROUPS.get(AuthAPI.getGroup(playerName));
	}

	public static void setGroup(String playerName, String groupId, long expiredTime) {
		AuthAPI.setGroup(playerName, groupId, expiredTime);

		if (expiredTime != -1) {
			RemoveGroup.add(playerName, expiredTime);
		}
	}

	public static void updateNamedTag(Player player) {
		Titles currentTitle = TitleAPI.getManager(player).getCurrentTitle();
		player.setNameTag((currentTitle == null ? "" : currentTitle.getName()) + getPlayerGroup(player.getName()).getGroupName() + " " + player.getName() + "\n§7" + Utils.getDeviceOS(player));
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment attachment = player.addAttachment(AnarchyCore.getInstance(), player.getName());
		attachment.clearPermissions();
		DefaultGroup defaultGroup = getPlayerGroup(player.getName());
		defaultGroup.getPermissionAttachment().forEach(attachment::setPermission);
	}
}