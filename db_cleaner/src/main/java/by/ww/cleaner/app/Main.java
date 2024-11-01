package by.ww.cleaner.app;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Application application = new Application("Hide databases");
        application.setMinimumSize(new Dimension(400, 400));
        application.setVisible(true);
    }
}
