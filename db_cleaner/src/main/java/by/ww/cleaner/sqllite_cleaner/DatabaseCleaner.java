package by.ww.cleaner.sqllite_cleaner;

import by.ww.cleaner.detect.Checker;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseCleaner {
    public static void cleanDatabase(String dbFilePath) {
        String dbUrl = "jdbc:sqlite:" + dbFilePath;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            ResultSet rsTables = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';"
            );

            while (rsTables.next()) {
                String tableName = rsTables.getString("name");
                System.out.println("Cleaning columns for table: " + tableName);
                cleanTableColumns(tableName);
            }
        } catch (SQLException e){
            throw new RuntimeException("Invalid file type, expected .db", e);
        }
    }

    private static void cleanTableColumns(String tableName) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
            ArrayList<String> columnsToDrop = new ArrayList<>();
            Checker checker = new Checker("/main/by/ww/cleaner/detect/data.json");
            while (rs.next()) {
                String columnName = rs.getString("name");

                if (checker.isConfidential(columnName)) {
                    System.out.println("Deleting column: " + columnName);
                    columnsToDrop.add(columnName);
                } else {
                    System.out.println("Skipping column: " + columnName);
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
                    if (!columns.isEmpty()) {
                        columns.append(", ");
                    }
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

