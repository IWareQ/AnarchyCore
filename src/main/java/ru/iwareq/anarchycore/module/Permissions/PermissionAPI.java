package ru.iwareq.anarchycore.module.Permissions;

import cn.nukkit.Player;
import cn.nukkit.permission.PermissionAttachment;
import ru.iwareq.anarchycore.Main;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Auth.AuthDB;
import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.BogGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.GuardianGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.HeroGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.KingGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.LordGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.PrinceGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.TartarusGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup.TitanGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.PlayerGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.SpecialGroup.YouTubeGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup.AdministratorGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup.HelperGroup;
import ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup.ModeratorGroup;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;

public class PermissionAPI {

	public static final HashMap<String, DefaultGroup> GROUPS = new HashMap<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerGroup(new PlayerGroup());
		registerGroup(new BogGroup());
		registerGroup(new GuardianGroup());
		registerGroup(new HeroGroup());
		registerGroup(new KingGroup());
		registerGroup(new LordGroup());
		registerGroup(new PrinceGroup());
		registerGroup(new TartarusGroup());
		registerGroup(new TitanGroup());
		registerGroup(new YouTubeGroup());
		registerGroup(new HelperGroup());
		registerGroup(new ModeratorGroup());
		registerGroup(new AdministratorGroup());
	}

	private static void registerGroup(DefaultGroup group) {
		GROUPS.put(group.getGroupId(), group);
	}

	public static boolean isGroup(String groupId) {
		return GROUPS.containsKey(groupId);
	}

	public static DefaultGroup getGroup(String groupId) {
		return GROUPS.get(groupId);
	}

	public static DefaultGroup getPlayerGroup(String playerName) {
		return GROUPS.get(AuthAPI.getGroup(playerName));
	}

	public static void setGroup(String playerName, String groupId) {
		AuthAPI.setGroup(playerName, groupId);
	}

	public static void updateNamedTag(Player player) {
		player.setNameTag(getPlayerGroup(player.getName()).getGroupName() + " " + player.getName() + "\n§7" + Utils.getDeviceOS(player));
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment attachment = player.addAttachment(Main.getInstance(), player.getName());
		attachment.clearPermissions();
		DefaultGroup defaultGroup = getPlayerGroup(player.getName());
		defaultGroup.getPermissionAttachment().forEach(attachment::setPermission);
	}
}