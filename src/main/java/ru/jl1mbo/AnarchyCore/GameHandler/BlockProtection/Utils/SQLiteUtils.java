package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.Utils;

import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import ru.jl1mbo.AnarchyCore.Main;

public class SQLiteUtils {
    private static final Sql2o sql2o = new Sql2o("jdbc:sqlite:" + Main.getInstance().getDataFolder() + "/BlockProtection/Regions.db", null, null);

    public static Connection query(String query) {
        try (final Connection connection = sql2o.open()) {
            return connection.createQuery(query).executeUpdate();
        }
    }

    public static Integer selectInteger(String query) {
        try (final Connection connection = sql2o.open()) {
            return connection.createQuery(query).executeScalar(Integer.class);
        }
    }

    public static String selectString(String query) {
        try (final Connection connection = sql2o.open()) {
            return connection.createQuery(query).executeScalar(String.class);
        }
    }

    public static List<String> selectStringList(String query) {
        try (final Connection connection = sql2o.open()) {
            return connection.createQuery(query).executeScalarList(String.class);
        }
    }

    public static List<Integer> selectIntegerList(String query) {
        try (final Connection connection = sql2o.open()) {
            return connection.createQuery(query).executeScalarList(Integer.class);
        }
    }
}