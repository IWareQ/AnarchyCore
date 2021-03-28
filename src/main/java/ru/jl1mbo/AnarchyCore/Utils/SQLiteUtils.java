package ru.jl1mbo.AnarchyCore.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ru.jl1mbo.AnarchyCore.Main;
import ru.nukkit.dblib.DbLib;

public class SQLiteUtils {

	public static Connection connectToSQLite(String filename) {
		return DbLib.getSQLiteConnection(new File(Main.getInstance().getDataFolder() + File.separator + filename));
	}

	public static boolean query(String filename, String query) {
		Connection connection = connectToSQLite(filename);
		if (connection == null) return false;
		try {
			connection.createStatement().executeUpdate(query);
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return true;
	}

	public static String buildQueryFromMap(HashMap<String, String> hashMap) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, String> entry : hashMap.entrySet()) {
			stringBuilder.append("`").append(entry.getKey()).append("` = \"").append(entry.getValue()).append("\", ");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 2);
	}

	public static HashMap<String, String> getHashMap(String filename, String query) {
		HashMap<String, String> hashMap = new HashMap<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;

			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					hashMap.put(rsmd.getColumnName(i), result.getString(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return hashMap;
	}

	public static HashMap<String, String> getStringHashMap(String filename, String query) {
		HashMap<String, String> hashMap = new HashMap<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;

			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					String s = result.getString(i);
					hashMap.put(rsmd.getColumnName(i), s == null ? "0" : s);
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return hashMap;
	}

	public static HashMap<String, Integer> getIntegerHashMap(String filename, String query) {
		HashMap<String, Integer> hashMap = new HashMap<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;

			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					hashMap.put(rsmd.getColumnName(i), result.getInt(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return hashMap;
	}

	public static ArrayList<String> getStringList(String filename, String query) {
		ArrayList<String> arrayList = new ArrayList<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;

			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					arrayList.add(result.getString(1));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return arrayList;
	}

	public static ArrayList<Integer> getIntegerList(String filename, String query) {
		ArrayList<Integer> arrayList = new ArrayList<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;

			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					arrayList.add(result.getInt(1));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return arrayList;
	}

	public static String getString(String filename, String query) {
		String res = null;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			res = result.getString(1);
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return res;
	}

	public static Integer getInteger(String filename, String query) {
		int res = -1;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1;
			res = result.getInt(1);
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return res;
	}

	public static Long getLong(String filename, String query) {
		long res = -1L;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1L;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1L;
			res = result.getLong(1);
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return res;
	}

	public static Double getDouble(String filename, String query) {
		double res = -1D;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1D;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1D;
			res = result.getDouble(1);
			if (connection != null) connection.close();
		} catch (SQLException exception) {
			//exception.printStackTrace();
		}
		return res;
	}
}