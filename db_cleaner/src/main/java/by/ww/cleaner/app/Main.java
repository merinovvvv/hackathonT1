package by.ww.cleaner.app;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            Application application = new Application("DB Cleaner");
            application.setMinimumSize(new Dimension(300, 300));
            application.setVisible(true);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}