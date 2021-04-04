package ru.jl1mbo.AnarchyCore.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;

public class Utils {
	private  static String P = "пПnPp";
	private  static String I = "иИiI1u";
	private  static String E = "еЕeEёЁ";
	private  static String D = "дДdD";
	private  static String Z = "зЗ3zZ3";
	private  static String M = "мМmM";
	private  static String U = "уУyYuU";
	private  static String O = "оОoO0";
	private  static String L = "лЛlL";
	private  static String S = "сСcCsS";
	private  static String A = "аАaA@";
	private  static String N = "нНhH";
	private  static String G = "гГgG";
	private  static String CH = "чЧ4";
	private  static String K = "кКkK";
	private  static String C = "цЦcC";
	private  static String R = "рРpPrR";
	private  static String H = "хХxXhH";
	private  static String YI = "йЙy";
	private  static String YA = "яЯ";
	private  static String YU = "юЮ";
	private  static String B = "бБ6bB";
	private  static String T = "тТtT";
	private  static String HS = "ъЪ";
	private  static String SS = "ьЬ";
	private  static String Y = "ыЫ";
	private  static String SH = "ШшHhЩщ";
	private  static String V = "вВvVBb";
	private static final String accessToken = "55dd4f4f3ce1e286912bdb68d9d8cdec1f663608abf6b3463399e6255659347f7c069f7b9ef250de4c212";

	public static void sendMessageToChat(String message) {
		try {
			String url = "https://api.vk.com/method/messages.send?peer_id=2000000002&random_id=" + new Random().nextInt(Integer.MAX_VALUE) + "&access_token=" + accessToken + "&message=" + URLEncoder.encode(
							 message, "UTF-8") + "&v=5.124";
			URL(url);
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
		boolean dolbaeb = preg_match(text, "\\b.*([" + D + "][" + A  + O + "][" + L + "][" + B + "][" + A + O + "][" + E + "]).*\\b*");
		boolean pidor = preg_match(text, "\\b.*([" + P + "][" + I + Y + "][" + D + "][" + A + O + "][" + R + "]).*\\b*");
		boolean pedick = preg_match(text, "\\b.*([" + P + "][" + E + "][" + D + "][" + I + "][" + K + "]).*\\b*");
		boolean chmo = preg_match(text, "\\b.*([" + CH + "][" + M + "][" + O + "]).*\\b*");
		boolean xyisos = preg_match(text, "\\b.*([" + H + "][" + U + "][" + E + I  + "][" + S + "][" + O + I + "]).*\\b*");
		boolean dayn = preg_match(text, "\\b.*([" + D + "][" + A + "][" + U  + "][" + N + "]).*\\b*");
		if (pizda || ebal || blad || soska || mamka || dolbaeb || pidor || pedick || chmo || dayn || xyisos) {
			return true;
		}
		return false;
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
		try {
			return DatatypeConverter.printHexBinary(NBTIO.write(namedTag, ByteOrder.LITTLE_ENDIAN));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static CompoundTag convertHexToNBT(String hex) {
		try {
			return NBTIO.read(DatatypeConverter.parseHexBinary(hex), ByteOrder.LITTLE_ENDIAN);
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
		return String.valueOf(player.getLoginChainData().getDeviceOS()).replace("0", "Неизвестно").replace("1", "Android").replace("2", "iOS").replace("3", "MacOS").replace("4",
				"FireOS").replace("5", "GearVR").replace("6", "HoloLens").replace("10", "PS 4").replace("7", "Win 10").replace("8", "Win").replace("9", "Dedicated").replace("11", "Switch");
	}

	public static List<String> getPlayersList(String playerName) {
		ArrayList<String> playerList = new ArrayList<>();
		Player target = Server.getInstance().getPlayer(playerName);
		if (target == null) {
			/*for (Entry<String, Object> entry : AuthAPI.config.getAll().entrySet()) {
				if (entry.getKey().startsWith(playerName)) {
					playerList.add(entry.getKey());
				}
			}*/
		}
		if (playerList.isEmpty()) {
			playerList.add(playerName);
		}
		return playerList;
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