package by.ww.cleaner.parser;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import by.ww.cleaner.exceptions.*;


public class SQLiteParser {
    public static boolean Validate(File db_file) {
        String prefix = "jdbc:sqlite:";
        try {
            Connection connection = DriverManager.getConnection(prefix + db_file.getAbsolutePath());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static void Clone(File source, File target) {
        Path source_path = Paths.get(source.getAbsolutePath());
        Path target_path = Paths.get(target.getAbsolutePath());
        try {
            if (Files.isSameFile(source_path, target_path)) {
                return;
            }
            Files.copy(source_path, target_path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Can't copy selected file.", e);
        }
    }
}
