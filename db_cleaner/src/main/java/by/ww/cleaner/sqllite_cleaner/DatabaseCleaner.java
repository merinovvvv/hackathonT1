package by.ww.cleaner.sqllite_cleaner;

import by.ww.cleaner.detector.Checker;

import java.sql.*;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.ww.cleaner.Config;

public class DatabaseCleaner {
    public static void cleanDatabase(String dbfilePath) {
        String dbUrl = "jdbc:sqlite:" + dbfilePath;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            ExecutorService executor = Executors.newCachedThreadPool();

            ResultSet rsTables = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';");

            while (rsTables.next()) {
                String tableName = rsTables.getString("name");
                executor.submit(() -> cleanTableColumns(conn, tableName));
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}
        } catch (SQLException e) {
            throw new RuntimeException("Invalid file type", e);
        }
    }


    private static void cleanTableColumns(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
            ArrayList<String> columnsToDrop = new ArrayList<>();
            Checker checker = new Checker(Config.credNamesJsonPath);
            while (rs.next()) {
                String columnName = rs.getString("name");

                if (checker.isConfidential(columnName)) {
                    columnsToDrop.add(columnName);
                }
            }

            dropColumns(conn, tableName, columnsToDrop);

        } catch (Exception e){
            throw new RuntimeException("Invalid file type, expected .db", e);
        }
    }

    private static void dropColumns(Connection conn, String tableName, ArrayList<String> columnNames) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
        StringBuilder columns = new StringBuilder();

        while (rs.next()) {
            String currentColumn = rs.getString("name");
            boolean isColumnToAdd = true;
            for(String columnName : columnNames) {
                if (currentColumn.equals(columnName)) {
                    isColumnToAdd = false;
                    break;
                }
            }
            if(isColumnToAdd) {
                if (!columns.isEmpty()) {
                    columns.append(", ");
                }
                columns.append(currentColumn);
            }
        }

        conn.setAutoCommit(false);

        try {
            String tempTableName = tableName + "_temp";
            stmt.execute("ALTER TABLE " + tableName + " RENAME TO " + tempTableName + ";");
            stmt.execute("CREATE TABLE " + tableName + " AS SELECT " + columns + " FROM " + tempTableName + ";");
            stmt.execute("DROP TABLE " + tempTableName + ";");

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException("", e);
        } finally {
            conn.setAutoCommit(true);
        }
    }
}

