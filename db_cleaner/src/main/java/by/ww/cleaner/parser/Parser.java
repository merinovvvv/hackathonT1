package by.ww.cleaner.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Parser {
    Parser(File db_file) {
        this.db_file = db_file;
    }

    boolean Connect() {
        String prefix = "jdbc:sqlite:";
        try {
            connection = DriverManager.getConnection(prefix + db_file.getAbsolutePath());
        } catch (SQLException _) {
            return false;
        }
        return true;
    }

    Connection connection;
    File db_file;
}
