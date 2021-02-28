package ru.jl1mbo.AnarchyCore.Utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class Utils {
	private static final String accessToken = "55dd4f4f3ce1e286912bdb68d9d8cdec1f663608abf6b3463399e6255659347f7c069f7b9ef250de4c212";

	public static void sendMessageToChat(String message) {
		try {
			String url = "https://api.vk.com/method/messages.send?peer_id=2000000001&random_id=" + new Random().nextInt(Integer.MAX_VALUE) + "&access_token=" + accessToken + "&message=" + URLEncoder.encode(
							 message, "UTF-8") + "&v=5.124";
			URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageAdmins(String message, int type) {
		if (type == 1) {
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				if (player.hasPermission("AdminChat")) {
					player.sendMessage(message);
				}
			}
		}
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.hasPermission("AdminChat")) {
				player.sendPopup(message);
			}
		}
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

	public static String getSecond(int seconds) {
		int preLastDigit = seconds % 100 / 10;
		if (preLastDigit == 1) {
			return " §fсекунд";
		}
		if (seconds % 10 == 1) {
			return " §fсекунду";
		} else if (seconds % 10 == 2 || seconds % 10 == 3 || seconds % 10 == 4) {
			return " §fсекунды";
		}
		return " §fсекунд";
	}

	public static String getDeviceOS(Player player) {
		return String.valueOf(player.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4",
				"FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
	}

	public static List<String> getPlayerToName(String playerName) {
		ArrayList<String> arrayList = new ArrayList<>();
		Player target = Server.getInstance().getPlayer(playerName);
		if (target == null) {
			for (Entry<String, Object> entry : AuthorizationAPI.config.getAll().entrySet()) {
				if (entry.getKey().startsWith(playerName)) {
					arrayList.add(entry.getKey());
				}
			}
		}
		if (arrayList.isEmpty()) {
			arrayList.add(target.getName());
		}
		return arrayList;
	}

	private static void URL(String url) {
		try {
			URL request = new URL(url);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.openStream()));
			StringBuilder stringBuilder;
			stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) stringBuilder.append(inputLine);
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDate() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
	}

	public static String implode(String[] args, int start) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}
		return stringBuilder.substring(1);
	}

	public static int rand(int min, int max) {
		if (min == max) {
			return max;
		}
		return new Random().nextInt(max + 1 - min) + min;
	}

	public static double rand(double min, double max) {
		if (min == max) {
			return max;
		}
		return min + Math.random() * (max - min);
	}

	public static boolean isDouble(String string) {
		return string.matches("^[0.1-9.0]+$");
	}

	public static String getOnlineString(String playerName) {
		return Server.getInstance().getPlayerExact(playerName) == null ? "§cОффлайн" : "§aОнлайн";
	}

	public static boolean isInteger(String string) {
		return string.matches("^[0-9]+$");
	}
}