package ru.jl1mbo.AnarchyCore.Modules.Permissions;

import java.util.HashMap;
import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.permission.PermissionAttachment;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DefaultGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.PlayerGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.BogGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.GuardianGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.HeroGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.KingGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.LordGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.PrinceGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.TartarusGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup.TitanGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.SpecialGroup.YouTubeGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.StaffGroup.AdministratorGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.StaffGroup.HelperGroup;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.StaffGroup.ModeratorGroup;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

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
		return GROUPS.get(SQLiteUtils.getString("Users.db", "SELECT `Permission` FROM `Users` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "';"));
	}

	public static void setGroup(String playerName, String groupId) {
		SQLiteUtils.query("Users.db", "UPDATE `Users` SET `Permission` = '" + groupId + "' WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "';");
	}

	public static void updateNamedTag(Player player) {
		player.setNameTag(getPlayerGroup(player.getName()).getGroupName() + " " + player.getName() + "\n§7" + Utils.getDeviceOS(player));
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment permissionAttachment = player.addAttachment(Main.getInstance(), player.getName());
		permissionAttachment.clearPermissions();
		DefaultGroup defaultGroup = getPlayerGroup(player.getName());
		for (Entry<String, Boolean> entry : defaultGroup.getPermissionAttachment().entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}