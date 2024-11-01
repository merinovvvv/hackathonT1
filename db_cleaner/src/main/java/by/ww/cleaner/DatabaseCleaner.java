package by.ww.cleaner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
                cleanTableColumns(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cleanTableColumns(String tableName) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
            ArrayList<String> columnsToDrop = new ArrayList<>();

            while (rs.next()) {
                String columnName = rs.getString("name");

                if (isConfident(columnName)) {
                    System.out.println("Deleting column: " + columnName);
                    columnsToDrop.add(columnName);
                    //dropColumn(conn, tableName, columnName);
                } else {
                    System.out.println("Skipping column: " + columnName);
                }
            }

            dropColumns(conn, tableName, columnsToDrop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void dropColumn(Connection conn, String tableName, String columnName) throws Exception {
        // Step 1: Get current columns (excluding the one to drop)
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
        StringBuilder columns = new StringBuilder();
        while (rs.next()) {
            String currentColumn = rs.getString("name");
            if (!currentColumn.equals(columnName)) {
                if (columns.length() > 0) columns.append(", ");
                columns.append(currentColumn);
            }
        }

        // Step 2: Start transaction for atomicity
        conn.setAutoCommit(false);

        try {
            // Step 3: Rename old table
            String tempTableName = tableName + "_temp";
            stmt.execute("ALTER TABLE " + tableName + " RENAME TO " + tempTableName + ";");

            // Step 4: Recreate the table without the dropped column
            stmt.execute("CREATE TABLE " + tableName + " AS SELECT " + columns + " FROM " + tempTableName + ";");

            // Step 5: Drop the temporary table
            stmt.execute("DROP TABLE " + tempTableName + ";");

            // Commit transaction
            conn.commit();
        } catch (Exception e) {
            conn.rollback(); // Rollback transaction on failure
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restore default commit behavior
        }
    }

    private static void dropColumns(Connection conn, String tableName, ArrayList<String> columnNames) throws Exception {
        // Step 1: Get current columns (excluding the one to drop)
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
        StringBuilder columns = new StringBuilder();
        while (rs.next()) {
            String currentColumn = rs.getString("name");
            boolean isColumnToAdd = true;
            for(String columnName : columnNames) {
                if (currentColumn.equals(columnName)) {
                    if (columns.length() > 0) {
                        columns.append(", ");
                    }
                    isColumnToAdd = false;
                    break;
                    //columns.append(currentColumn);
                }
            }
            if(isColumnToAdd) {
                if (columns.length() > 0) {
                    columns.append(", ");
                }
                columns.append(currentColumn);
            }
        }

        // Step 2: Start transaction for atomicity
        conn.setAutoCommit(false);

        try {
            // Step 3: Rename old table
            String tempTableName = tableName + "_temp";
            stmt.execute("ALTER TABLE " + tableName + " RENAME TO " + tempTableName + ";");

            // Step 4: Recreate the table without the dropped column
            stmt.execute("CREATE TABLE " + tableName + " AS SELECT " + columns + " FROM " + tempTableName + ";");

            // Step 5: Drop the temporary table
            stmt.execute("DROP TABLE " + tempTableName + ";");

            // Commit transaction
            conn.commit();
        } catch (Exception e) {
            conn.rollback(); // Rollback transaction on failure
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restore default commit behavior
        }
    }


    private static boolean isConfident(String columnName) {
        return columnName.startsWith("conf_");
    }
}

