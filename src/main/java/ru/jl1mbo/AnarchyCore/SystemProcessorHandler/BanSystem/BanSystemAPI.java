package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem;

import java.util.Arrays;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.BanCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.MuteCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.UnBanCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Commands.UnMuteCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener.EventsListenerBanSystem;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils.BanUtils;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils.MuteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class BanSystemAPI {
	public static Config banConfig;
	public static Config muteConfig;
	public static String PREFIX = "§l§7(§3Система§7) §r";

	public static void register() {
		banConfig = new Config(Main.getInstance().getDataFolder() + "/BanSystem/Bans.yml", Config.YAML);
		muteConfig = new Config(Main.getInstance().getDataFolder() + "/BanSystem/Mutes.yml", Config.YAML);
		EventsListenerBanSystem.register();
		Server.getInstance().getCommandMap().registerAll("", Arrays.asList(new BanCommand(), new UnBanCommand(), new MuteCommand(), new UnMuteCommand()));
	}

	public static boolean IsBanned(String playerName) {
		return banConfig.exists(playerName.toLowerCase());
	}

	public static boolean IsMuted(String playerName) {
		return muteConfig.exists(playerName.toLowerCase());
	}

	public static void addBan(String playerName, String reason, String bannerName, int seconds) {
		long endBan = System.currentTimeMillis() / 1000L + seconds;
		if (!bannerName.equalsIgnoreCase("AntiCheat")) {
			Utils.sendMessageToChat("🔒Блокировка аккаунта\n\nИгрок: " + playerName + "\nАдминистратор: " + bannerName + "\nПричина: " + reason + "\nПериод: " +
									Utils.getRemainingTime(endBan).replaceAll("§[0-9]", "").replaceAll("§[a-zA-Z]", ""));
		}
		banConfig.set(playerName.toLowerCase() + ".Reason", reason);
		banConfig.set(playerName.toLowerCase() + ".Banner", bannerName);
		banConfig.set(playerName.toLowerCase() + ".EndBan", endBan);
		banConfig.save();
		banConfig.reload();
		Player target = Server.getInstance().getPlayerExact(playerName);
		if (target != null) {
			target.sendTitle("§l§6Аккаунт", "§l§6заблокирован", 0, 60, 0);
			target.setImmobile(true);
		}
	}

	public static void addMute(String playerName, String reason, String bannerName, int seconds) {
		long endMute = System.currentTimeMillis() / 1000L + seconds;
		Utils.sendMessageToChat("🔒Блокировка чата\n\nИгрок: " + playerName + "\nАдминистратор: " + bannerName + "\nПричина: " + reason + "\nПериод: " +
								Utils.getRemainingTime(endMute).replaceAll("§[0-9]", "").replaceAll("§[a-zA-Z]", ""));
		muteConfig.set(playerName.toLowerCase() + ".Reason", reason);
		muteConfig.set(playerName.toLowerCase() + ".Banner", bannerName);
		muteConfig.set(playerName.toLowerCase() + ".EndMute", endMute);
		muteConfig.save();
		muteConfig.reload();
		Player target = Server.getInstance().getPlayerExact(playerName);
		if (target != null) {
			target.sendMessage("§l§6| §rТебя замутили§7! §fАдминистратор закрыл тебе доступ к чату на §6" + Utils.getRemainingTime(
								   endMute) + " §fпо причине §6" + reason + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
		}
	}

	public static void removeBan(String playerName) {
		banConfig.remove(playerName.toLowerCase());
		banConfig.save();
		banConfig.reload();
	}

	public static void removeMute(String playerName) {
		muteConfig.remove(playerName.toLowerCase());
		muteConfig.save();
		muteConfig.reload();
	}

	public static BanUtils getBanUtils(String playerName) {
		String reason = banConfig.getString(playerName.toLowerCase() + ".Reason");
		String bannerName = banConfig.getString(playerName.toLowerCase() + ".Banner");
		long time = banConfig.getLong(playerName.toLowerCase() + ".EndBan");
		return new BanUtils(playerName.toLowerCase(), reason, bannerName, time);
	}

	public static MuteUtils getMuteUtils(String playerName) {
		String reason = muteConfig.getString(playerName.toLowerCase() + ".Reason");
		String bannerName = muteConfig.getString(playerName.toLowerCase() + ".Banner");
		long endMute = muteConfig.getLong(playerName.toLowerCase() + ".EndMute");
		return new MuteUtils(playerName.toLowerCase(), reason, bannerName, endMute);
	}
}