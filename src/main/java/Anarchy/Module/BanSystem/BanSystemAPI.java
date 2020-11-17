package Anarchy.Module.BanSystem;

import java.io.File;
import java.util.Map;
import Anarchy.AnarchyMain;
import Anarchy.Module.BanSystem.Utils.BanUtils;
import Anarchy.Module.BanSystem.Utils.MuteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;

public class BanSystemAPI {

	public static void banPlayer(String player, String reason, String banner, int seconds) {
		File dataFile = new File(AnarchyMain.folder + "/bans.yml");
		Config bans = new Config(dataFile, Config.YAML);
		long endBan = System.currentTimeMillis() / 1000L + seconds;
		if (seconds == -1) endBan = -1L;
		bans.set("Ban." + player + ".Reason", reason);
		bans.set("Ban." + player + ".Banner", banner);
		bans.set("Ban." + player + ".Time", endBan);
		bans.save();
		bans.reload();
		Player target = Server.getInstance().getPlayerExact(player);
		if (target != null) {
			target.close("", "§l§fУвы§7, §fно Вас §6временно §fзаблокировали§7!\n§fВас заблокировал§7: §6" + banner + "\n§fПричина блокировки§7: §6" + reason + "\n§fРазбан через§7: §6" + ((endBan - System.currentTimeMillis() / 1000L) / 86400) + " §fд§7. §6" + ((endBan - System.currentTimeMillis() / 1000L) / 3600 % 24) + " §fч§7. §6" + ((endBan - System.currentTimeMillis() / 1000L) / 60 % 60) + " §fмин§7.");
		}
	}

	public static void mutePlayer(String player, String reason, String banner, int seconds) {
		File dataFile = new File(AnarchyMain.folder + "/mutes.yml");
		Config mutes = new Config(dataFile, Config.YAML);
		long endMute = System.currentTimeMillis() / 1000L + seconds;
		if (seconds == -1) endMute = -1L;
		mutes.set("Mute." + player + ".Reason", reason);
		mutes.set("Mute." + player + ".Banner", banner);
		mutes.set("Mute." + player + ".Time", endMute);
		mutes.save();
		mutes.reload();
		Player target = Server.getInstance().getPlayerExact(player);
		if (target != null) {
			target.sendMessage("§l§c• §r§fТебя замутили§7! §fАдминистратор §6" + banner + " §fзакрыл тебе доступ к чату на §6" + ((endMute - System.currentTimeMillis() / 1000L) / 60 % 60) + " §fмин§7. §6" + ((endMute - System.currentTimeMillis() / 1000L) % 60) + " §fсек§7. §fпо причине §6" + reason + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
		}
	}

	public static boolean playerIsBanned(String playerName) {
		File dataFile = new File(AnarchyMain.folder + "/bans.yml");
		Config bans = new Config(dataFile, Config.YAML);
		return bans.exists("Ban." + playerName);
	}

	public static void unBanPlayer(String playerName) {
		File dataFile = new File(AnarchyMain.folder + "/bans.yml");
		Config bans = new Config(dataFile, Config.YAML);
		Map<String, Object> map = bans.getSection("Ban").getAllMap();
		map.remove(playerName);
		bans.set("Ban", map);
		bans.save();
		bans.reload();
	}

	public static void unMutePlayer(String playerName) {
		File dataFile = new File(AnarchyMain.folder + "/mutes.yml");
		Config mutes = new Config(dataFile, Config.YAML);
		Map<String, Object> map = mutes.getSection("Mute").getAllMap();
		map.remove(playerName);
		mutes.set("Mute", map);
		mutes.save();
		mutes.reload();
	}

	public static BanUtils getBan(String player) {
		File dataFile = new File(AnarchyMain.folder + "/bans.yml");
		Config bans = new Config(dataFile, Config.YAML);
		String reason = bans.getString("Ban." + player + ".Reason");
		String banner = bans.getString("Ban." + player + ".Banner");
		long time = bans.getLong("Ban." + player + ".Time");
		return new BanUtils(player, reason, banner, time);
	}

	public static MuteUtils getMute(String player) {
		File dataFile = new File(AnarchyMain.folder + "/mutes.yml");
		Config mutes = new Config(dataFile, Config.YAML);
		String reason = mutes.getString("Mute." + player + ".Reason");
		String banner = mutes.getString("Mute." + player + ".Banner");
		long time = mutes.getLong("Mute." + player + ".Time");
		return new MuteUtils(player, reason, banner, time);
	}

	public static boolean playerIsMuted(String playerName) {
		File dataFile = new File(AnarchyMain.folder + "/mutes.yml");
		Config mutes = new Config(dataFile, Config.YAML);
		return mutes.exists("Mute." + playerName);
	}
}