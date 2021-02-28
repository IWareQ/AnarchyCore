package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Commands.GroupCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener.PlayerChatListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.PlayerGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.BogGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.GuardianGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.HeroGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.KingGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.LordGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.PrinceGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.TartarusGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup.TitanGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.SpecialGroup.YouTubeGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.StaffGroup.AdministratorGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.StaffGroup.HelperGroup;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.StaffGroup.ModeratorGroup;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class PermissionAPI {
	private static Config config;
	private static final HashMap<String, DefaultGroup> GROUPS = new HashMap<>();
	public static String PREFIX = "§7(§3Привилегии§7) §r";

	public static void register() {
		registerAllGroups();
		config = new Config(Main.getInstance().getDataFolder() + "/Permission/Users.yml", Config.YAML);
		registerGroup(new PlayerGroup());
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerChatListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new GroupCommand());
	}

	private static void registerAllGroups() {
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
		if (GROUPS.get(groupId) != null) {
			return true;
		}
		return false;
	}

	public static HashMap<String, DefaultGroup> getAllGroups() {
		return GROUPS;
	}

	public static String getGroup(String playerName) {
		return config.getString(playerName.toLowerCase(), "player");
	}

	public static void setGroup(String playerName, String groupId) {
		config.set(playerName.toLowerCase(), groupId);
		config.save();
		config.reload();
	}

	public static void updateNamedTag(Player player) {
		player.setNameTag(GROUPS.get(getGroup(player.getName())).getGroupName() + " " + player.getName() + "\n§7" + Utils.getDeviceOS(player));
	}

	public static void updatePermissions(Player player) {
		PermissionAttachment permissionAttachment = player.addAttachment(Main.getInstance(), player.getName());
		permissionAttachment.clearPermissions();
		DefaultGroup defaultGroup = GROUPS.get(getGroup(player.getName()));
		for (HashMap.Entry<String, Boolean> entry : defaultGroup.getPermissionAttachment().entrySet()) {
			permissionAttachment.setPermission(entry.getKey(), entry.getValue());
		}
	}
}