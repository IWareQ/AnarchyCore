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
		StringBuilder s = new StringBuilder();
		for (String arg : args) {
			s.append(" ").append(arg);
		}
		return s.toString().substring(1);
	}
	
	public static String implode(String[] args, int start) {
		StringBuilder s = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			s.append(" ").append(args[i]);
		}
		return s.toString().substring(1);
	}
	
	public static String implode(String[] args, int start, int stop) {
		StringBuilder s = new StringBuilder();
		for (int i = start; i < stop; i++) {
			s.append(" " + args[i]);
		}
		return s.toString().substring(1);
	}
	
	public static boolean isValidString(String string) {
		return !preg_match("/^[a-zA-Zа-яА-Я0-9]+$/u", string);
	}
	
	public String convertStreamToString(InputStream input) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		input.close();
		return sb.toString();
	}
	
	public static String getDate() {
		return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(Calendar.getInstance().getTime());
	}
	
	public static String getDate(Long date) {
		return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date(date));
	}
	
	public static String getOnlineString(String playerName) {
		return Server.getInstance().getPlayerExact(playerName) == null ? "§6Оффлайн" : "§6Онлайн";
	}
	
	public static boolean isInteger(String string) {
		return string.matches("^[0-9]+$");
	}
}