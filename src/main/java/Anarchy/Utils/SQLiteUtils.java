package Anarchy.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import DbLib.DbLib;

public class SQLiteUtils {

	public static Connection connectToSQLite(String filename) {
		return DbLib.getSQLiteConnection(new File(AnarchyMain.port + File.separator + filename));
	}

	public static boolean query(String filename, String query) {
		Connection connection;
		connection = connectToSQLite(filename);
		if (connection == null) return false;
		try {
			connection.createStatement().executeUpdate(query);
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return true;
	}

	public static String buildQueryFromMap(Map<String, String> map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			stringBuilder.append("`").append(entry.getKey()).append("` = \"").append(entry.getValue()).append("\", ");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 2);
	}

	public static Map<String, String> selectMap(String filename, String query) {
		Map<String, String> map = new HashMap<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();
			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					map.put(rsmd.getColumnName(i), result.getString(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return map;
	}

	public static Map<String, String> selectStringMap(String filename, String query) {
		Map<String, String> map = new HashMap<>();
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
					map.put(rsmd.getColumnName(i), s == null ? "0" : s);
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return map;
	}

	public static Map<String, Integer> selectIntegerMap(String filename, String query) {
		Map<String, Integer> map = new HashMap<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();
			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					map.put(rsmd.getColumnName(i), result.getInt(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return map;
	}

	public static ArrayList<String> selectList(String filename, String query) {
		ArrayList<String> list = new ArrayList<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet resultSet = connection.createStatement().executeQuery(query);
			if (resultSet == null) return null;
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int count = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= count; i++) {
					list.add(resultSet.getString(1));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return list;
	}

	public static ArrayList<Integer> selectIntegerList(String filename, String query) {
		ArrayList<Integer> list = new ArrayList<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();
			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					list.add(result.getInt(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return list;
	}

	public static ArrayList<String> selectStringList(String filename, String query) {
		ArrayList<String> list = new ArrayList<>();
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			ResultSetMetaData rsmd = result.getMetaData();
			int count = rsmd.getColumnCount();
			while (result.next()) {
				for (int i = 1; i <= count; i++) {
					list.add(result.getString(i));
				}
			}
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return list;
	}

	public static String selectString(String filename, String query) {
		String res;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return null;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return null;
			res = result.getString(1);
			if (connection != null) connection.close();
		} catch (SQLException e) {
			res = null;
		}
		return res;
	}

	public static Integer selectInteger(String filename, String query) {
		int res = -1;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1;
			res = result.getInt(1);
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return res;
	}

	public static Double selectDouble(String filename, String query) {
		Double res = -1.0;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1.0;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1.0;
			res = result.getDouble(1);
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return res;
	}

	public static Long selectLong(String filename, String query) {
		Long res = -1L;
		Connection connection = connectToSQLite(filename);
		if (connection == null) return -1L;
		try {
			ResultSet result = connection.createStatement().executeQuery(query);
			if (result == null) return -1L;
			res = result.getLong(1);
			if (connection != null) connection.close();
		} catch (SQLException e) {/**/}
		return res;
	}
}