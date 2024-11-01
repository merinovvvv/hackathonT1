package by.ww.cleaner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ColumnCleaner {


    public static void cleanTableColumns(String tableName) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
            while (rs.next()) {
                String columnName = rs.getString("name");

                if (isConfident(columnName)) {
                    System.out.println("Deleting column: " + columnName);
                    dropColumn(conn, tableName, columnName);
                } else {
                    System.out.println("Skipping column: " + columnName);
                }
            }
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

    private static boolean isConfident(String columnName) {
        return columnName.startsWith("conf_");
    }


}
