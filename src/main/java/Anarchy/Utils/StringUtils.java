package Anarchy.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nukkit.Server;

public class StringUtils {
	
	public static boolean preg_match(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	
	public static String implode(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String arg : args) {
			stringBuilder.append(" ").append(arg);
		}
		return stringBuilder.toString().substring(1);
	}
	
	public static String implode(String[] args, int start) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}
		return stringBuilder.toString().substring(1);
	}
	
	public static String implode(String[] args, int start, int stop) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = start; i < stop; i++) {
			stringBuilder.append(" " + args[i]);
		}
		return stringBuilder.toString().substring(1);
	}
	
	public static boolean isValidString(String string) {
		return !preg_match("/^[a-zA-Zа-яА-Я0-9]+$/u", string);
	}
	
	public String convertStreamToString(InputStream input) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		input.close();
		return stringBuilder.toString();
	}
	
	public static String getDate() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public static String getDate(Long date) {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(date));
	}
	
	public static String getOnlineString(String playerName) {
		return Server.getInstance().getPlayerExact(playerName) == null ? "§3Оффлайн" : "§3Онлайн";
	}
	
	public static boolean isInteger(String string) {
		return string.matches("^[0-9]+$");
	}
	
	public static boolean isDouble(String string) {
		return string.matches("^[0.1-9.0]+$");
	}
}