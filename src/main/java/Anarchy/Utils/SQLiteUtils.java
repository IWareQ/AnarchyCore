package Anarchy.Utils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class SQLiteUtils {
	private static Sql2o sql2o = new Sql2o("jdbc:mysql://95.217.71.75:3306/mysql10389?useUnicode=true&serverTimezone=UTC&useSSL=false", "mysql10389", "h5TdOzF4UV8K0lB2");

	public static void query(String query) {
		try (Connection con = sql2o.open()) {
			con.createQuery(query).executeUpdate();
			con.close();
		}
	}

	public static StringBuilder buildQueryFromMap(Map<String, String> map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			stringBuilder.append("`").append(entry.getKey()).append("` = '").append(entry.getValue()).append("', ");
		}
		return stringBuilder;
	}

	public static Map<String, String> selectMap(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).getColumnMappings();
		}
	}

	public static Map<String, String> selectStringMap(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).getColumnMappings();
		}
	}

	public static Map<String, List<Integer>> selectIntegerMap(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).getParamNameToIdxMap();
		}
	}

	public static List<Integer> selectIntegerList(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalarList(Integer.class);
		}
	}

	public static List<String> selectStringList(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalarList(String.class);
		}
	}

	public static String selectString(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalar(String.class);
		}
	}

	public static Integer selectInteger(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalar(Integer.class);
		}
	}

	public static Double selectDouble(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalar(Double.class);
		}
	}

	public static Long selectLong(String query) {
		try (Connection con = sql2o.open()) {
			return con.createQuery(query).executeScalar(Long.class);
		}
	}
}