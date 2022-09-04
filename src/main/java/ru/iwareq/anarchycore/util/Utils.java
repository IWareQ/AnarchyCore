package ru.iwareq.anarchycore.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	private static final String accessToken = "сюда свой токен";
	private static final String P = "пПnPp";
	private static final String I = "иИiI1u";
	private static final String E = "еЕeEёЁ";
	private static final String D = "дДdD";
	private static final String Z = "зЗ3zZ3";
	private static final String M = "мМmM";
	private static final String U = "уУyYuU";
	private static final String O = "оОoO0";
	private static final String L = "лЛlL";
	private static final String S = "сСcCsS";
	private static final String A = "аАaA@";
	private static final String N = "нНhH";
	private static final String G = "гГgG";
	private static final String CH = "чЧ4";
	private static final String K = "кКkK";
	private static final String C = "цЦcC";
	private static final String R = "рРpPrR";
	private static final String H = "хХxXhH";
	private static final String YI = "йЙy";
	private static final String YA = "яЯ";
	private static final String YU = "юЮ";
	private static final String B = "бБ6bB";
	private static final String T = "тТtT";
	private static final String HS = "ъЪ";
	private static final String SS = "ьЬ";
	private static final String Y = "ыЫ";
	private static final String SH = "ШшHhЩщ";
	private static final String V = "вВvVBb";

	public static void sendMessageToChat(String message) {
		try {
			String url = "https://api.vk.com/method/messages.send?peer_id=2000000002"
					+ "&random_id=" + new Random().nextInt(Integer.MAX_VALUE)
					+ "&access_token=" + accessToken
					+ "&message=" + URLEncoder.encode(message, "UTF-8")
					+ "&v=5.124";
			request(url);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static boolean validText(String texts) {
		String text = texts.replaceAll(" ", "").replaceAll("[0-9]", "").replaceAll(".", "");
		boolean pizda = preg_match(text, "\\b.*([" + P + "][" + I + E + Y + "][" + Z + "][" + D + "]).*\\b*");
		boolean ebal = preg_match(text, "\\b.*([" + E + "][" + B + "](?:[" + A + O + "][" + T + N + K + L + "]+|[" + L + "])).*\\b*");
		boolean blad = preg_match(text, "\\b.*([" + B + "][" + L + "][" + YA + "][" + D + "]).*\\b*");
		boolean soska = preg_match(text, "\\b.*([" + S + "][" + A + O + "][" + S + "](?:[" + U + "]+|[" + K + "]+|[" + I + "])).*\\b*");
		boolean mamka = preg_match(text, "\\b.*([" + M + "][" + A + "](?:[" + T + "]+|[" + M + "][" + K + "])).*\\b*");
		boolean dolbaeb = preg_match(text, "\\b.*([" + D + "][" + A + O + "][" + L + "][" + B + "][" + A + O + "][" + E + "]).*\\b*");
		boolean pidor = preg_match(text, "\\b.*([" + P + "][" + I + Y + "][" + D + "][" + A + O + "][" + R + "]).*\\b*");
		boolean pedick = preg_match(text, "\\b.*([" + P + "][" + E + "][" + D + "][" + I + "][" + K + "]).*\\b*");
		boolean chmo = preg_match(text, "\\b.*([" + CH + "][" + M + "][" + O + "]).*\\b*");
		boolean xyisos = preg_match(text, "\\b.*([" + H + "][" + U + "][" + E + I + "][" + S + "][" + O + I + "]).*\\b*");
		boolean dayn = preg_match(text, "\\b.*([" + D + "][" + A + "][" + U + "][" + N + "]).*\\b*");
		return pizda || ebal || blad || soska || mamka || dolbaeb || pidor || pedick || chmo || dayn || xyisos;
	}

	public static boolean preg_match(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	public static boolean isValidString(String text) {
		return preg_match(text, "\\b.*([a-zA-Zа-яА-Я]).*\\b*");
	}

	public static String convertNbtToHex(CompoundTag namedTag) {
		if (namedTag == null) {
			return "";
		}

		try {
			return DatatypeConverter.printHexBinary(NBTIO.write(namedTag));
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	public static CompoundTag convertHexToNBT(String hex) {
		if (hex.isEmpty()) {
			return new CompoundTag();
		}
		try {
			return NBTIO.read(DatatypeConverter.parseHexBinary(hex));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static void sendMessageAdmins(String message, int type) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (PermissionAPI.getPlayerGroup(player.getName()).isAdmin()) {
				if (type == 1) {
					player.sendMessage(message);
				} else {
					player.sendPopup(message);
				}
			}
		}
	}

	public static String getRemainingTime(long seconds) {
		SimpleDateFormat today = new SimpleDateFormat("dd.MM.yyyy");
		today.format(System.currentTimeMillis() / 1000L);
		SimpleDateFormat future = new SimpleDateFormat("dd.MM.yyyy");
		future.format(System.currentTimeMillis() / 1000L + seconds);
		long time = future.getCalendar().getTimeInMillis() - today.getCalendar().getTimeInMillis();
		int days = (int) (time / 86400L);
		int hours = (int) (time / 3600L % 24L);
		int minutes = (int) (time / 60L % 60L);
		if (minutes < 1 && days == 0 && hours == 0) {
			return time + " сек.";
		} else if (hours == 0 && days == 0) {
			return minutes + " мин. ";
		} else {
			return days == 0 ? hours + " ч. " + minutes + " мин."
			                 : days + " д. " + hours + " ч. " + minutes + " мин.";
		}
	}

	public static String getSecond(int seconds) {
		int preLastDigit = seconds % 100 / 10;
		if (preLastDigit == 1) {
			return seconds + " §fсекунд";
		}
		if (seconds % 10 == 1) {
			return seconds + " §fсекунду";
		} else if (seconds % 10 == 2 || seconds % 10 == 3 || seconds % 10 == 4) {
			return seconds + " §fсекунды";
		}
		return seconds + " §fсекунд";
	}

	public static String getDeviceOS(Player player) {
		return String.valueOf(player.getLoginChainData().getDeviceOS())
				.replace("0", "Неизвестно")
				.replace("1", "Android")
				.replace("2", "iOS")
				.replace("3", "MacOS")
				.replace("4", "FireOS")
				.replace("5", "GearVR")
				.replace("6", "HoloLens")
				.replace("7", "Win 10")
				.replace("8", "Win")
				.replace("9", "Dedicated")
				.replace("10", "PS 4")
				.replace("11", "Switch");
	}

	public static List<String> getPlayersList(String playerName) {
		ArrayList<String> playerList = new ArrayList<>();
		Player target = Server.getInstance().getPlayer(playerName);
		if (target == null) {
			/*List<String> names = MySQLUtils.getStringList("SELECT `Name` FROM `Users`;");
			for (String name : names) {
				if (name.startsWith(playerName)) {
					playerList.add(name);
				}
			}*/
		} else {
			playerList.add(target.getName());
		}
		return playerList;
	}

	private static void request(String url) {
		try {
			URL request = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
			StringBuilder stringBuilder;
			stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				stringBuilder.append(inputLine);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDate() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
	}

	public static String getDate(int unixtime) {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(unixtime);
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