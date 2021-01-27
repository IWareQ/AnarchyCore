package AnarchySystem.Components.AdminSystem.BanSystem;

import AnarchySystem.Main;
import AnarchySystem.Components.AdminSystem.BanSystem.Utils.BanUtils;
import AnarchySystem.Components.AdminSystem.BanSystem.Utils.MuteUtils;
import AnarchySystem.Utils.ConfigUtils;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;

public class BanSystemAPI {
	public static String PREFIX = "§l§7(§3Система§7) §r";

	public static boolean IsBanned(String playerName) {
		return ConfigUtils.getBanConfig().exists(playerName.toLowerCase());
	}

	public static boolean IsMuted(String playerName) {
		return ConfigUtils.getMuteConfig().exists(playerName.toLowerCase());
	}

	public static void sendMessageAdmin(Player player, String message, int type) {
		if (player.hasPermission("AdminChat")) {
			switch (type) {
			case 1:
				player.sendMessage(message);
				break;
			case 2:
				player.sendPopup(message);
				break;
			case 3:
				player.sendTitle("", message);
				break;
			}
		}
	}

	public static void addBan(String playerName, String reason, String bannerName, int seconds) {
		long endBan = System.currentTimeMillis() / 1000L + seconds;
		Utils.sendMessageToChat("🔒Блокировка аккаунта\n\nИгрок: " + playerName + "\nАдминистратор: " + bannerName + "\nПричина: " + reason + "\nПериод: " +
								getRemainingTime(endBan).replaceAll("§[0-9]", "").replaceAll("§[a-zA-Z]", ""), 2000000001);
		Config config = ConfigUtils.getBanConfig();
		config.set(playerName.toLowerCase() + ".Reason", reason);
		config.set(playerName.toLowerCase() + ".Banner", bannerName);
		config.set(playerName.toLowerCase() + ".EndBan", endBan);
		config.save();
		config.reload();
		Player target = Server.getInstance().getPlayerExact(playerName.toLowerCase());
		if (target != null) {
			target.close("", "§l§fУвы§7, §fно Вас §6временно §fзаблокировали§7!\n§fПричина§7: §6" + reason + "\n§fИстекает через§7: §6" +
						 getRemainingTime(endBan));
		}
	}

	public static void addMute(String playerName, String reason, String bannerName, int seconds) {
		Config config = ConfigUtils.getMuteConfig();
		long endMute = System.currentTimeMillis() / 1000L + seconds;
		config.set(playerName.toLowerCase() + ".Reason", reason);
		config.set(playerName.toLowerCase() + ".Banner", bannerName);
		config.set(playerName.toLowerCase() + ".EndMute", endMute);
		config.save();
		config.reload();
		Player target = Server.getInstance().getPlayerExact(playerName.toLowerCase());
		if (target != null) {
			target.sendMessage("§l§c• §r§fТебя замутили§7! §fАдминистратор закрыл тебе доступ к чату на §6" + getRemainingTime(
								   endMute) + " §fпо причине §6" + reason + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
		}
	}

	public static void removeBan(String playerName) {
		Config config = ConfigUtils.getBanConfig();
		config.remove(playerName.toLowerCase());
		config.save();
		config.reload();
	}

	public static void removeMute(String playerName) {
		Config config = ConfigUtils.getMuteConfig();
		config.remove(playerName.toLowerCase());
		config.save();
		config.reload();
	}

	public static BanUtils getBanUtils(String playerName) {
		Config config = ConfigUtils.getBanConfig();
		String reason = config.getString(playerName.toLowerCase() + ".Reason");
		String bannerName = config.getString(playerName.toLowerCase() + ".Banner");
		long time = config.getLong(playerName.toLowerCase() + ".EndBan");
		return new BanUtils(playerName.toLowerCase(), reason, bannerName, time);
	}

	public static MuteUtils getMuteUtils(String playerName) {
		Config config = ConfigUtils.getMuteConfig();
		String reason = config.getString(playerName.toLowerCase() + ".Reason");
		String bannerName = config.getString(playerName.toLowerCase() + ".Banner");
		long endMute = config.getLong(playerName.toLowerCase() + ".EndMute");
		return new MuteUtils(playerName.toLowerCase(), reason, bannerName, endMute);
	}

	public static String getRemainingTime(long seconds) {
		long time = System.currentTimeMillis() / 1000L;
		int days = (int)(seconds - time) / 86400;
		int hours = (int)(seconds - time) / 3600 % 24;
		int minutes = (int)(seconds - time) / 60 % 60;
		if (days < 1 && hours < 1) {
			return minutes + " §fмин§7.";
		} else if (days < 1) {
			return hours + " §fч§7. §6" + minutes + " §fмин§7.";
		} else {
			return days + " §fд§7. §6" + hours + " §fч§7. §6" + minutes + " §fмин§7.";
		}
	}
}