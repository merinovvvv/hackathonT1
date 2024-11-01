package by.ww.cleaner.app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Application app = new Application("Database Cleaner");
            app.setSize(500, 400); // Increased frame size
            app.setLocationRelativeTo(null); // Center the application
            app.setVisible(true);
        });
    }
}