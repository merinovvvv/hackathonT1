package by.ww.cleaner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCleaner {

    public static void cleanDatabase(String dbFilePath) {
        String dbUrl = "jdbc:sqlite:" + dbFilePath;

        // Step 1: Establish SQLite connection
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            // Step 2: Get all table names in the database
            ResultSet rsTables = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';"
            );

            // Step 3: Loop through each table and clean columns
            while (rsTables.next()) {
                String tableName = rsTables.getString("name");
                System.out.println("Cleaning columns for table: " + tableName);
                ColumnCleaner.cleanTableColumns(tableName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

