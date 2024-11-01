package by.ww.cleaner.app;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;

import by.ww.cleaner.Config;
import by.ww.cleaner.sqllite_cleaner.DatabaseCleaner;

public class Application extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);    // Increased header font size
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 18);   // Increased button font size
    private static final Font COMBOBOX_FONT = new Font("Arial", Font.PLAIN, 18); // Increased combo box font size

    JLabel selectBaseLabel;
    JComboBox<String> bases;
    RoundedButton execute;

    public Application(String title) {
        super(title);
        setupLookAndFeel();
        initComponents();
        layoutComponents();
        applyModernStyling();
        addActionListeners();
    }

    private void setupLookAndFeel() {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 16));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Panel.background", BACKGROUND_COLOR);
    }

    private void initComponents() {
        selectBaseLabel = new JLabel("Select the database:");
        selectBaseLabel.setFont(HEADER_FONT); // Set font for label to make it larger

        bases = new JComboBox<>();
        bases.setFont(COMBOBOX_FONT); // Set font for combo box
        fillComboBoxWithDatabaseNames(bases);

        execute = new RoundedButton("Execute");
        execute.setFont(BUTTON_FONT); // Set font for button
    }

    private void layoutComponents() {
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased margins for a "zoomed" effect
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(selectBaseLabel, gbc);

        gbc.gridy = 1;
        this.add(bases, gbc);

        gbc.gridy = 2;
        this.add(execute, gbc);
    }

    private void applyModernStyling() {
        this.getContentPane().setBackground(BACKGROUND_COLOR);
        execute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void fillComboBoxWithDatabaseNames(JComboBox<String> bases) {
        File folder = new File("../examples");

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".db")) {
                    bases.addItem(file.getName());
                }
            }
        } else {
            throw new IllegalArgumentException("The directory does not exist");
        }
    }

    static class RoundedButton extends JButton {

        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setBackground(BUTTON_COLOR);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            super.paintComponent(g);
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            repaint();
        }
    }

    private void addActionListeners() {
        execute.addActionListener(e -> {
            String selectedDatabase = (String) bases.getSelectedItem();
            if (selectedDatabase != null) {
                File databaseFile = new File(Config.workingDirectory + selectedDatabase);
                if (databaseFile.exists()) {
                    try {
                        DatabaseCleaner.cleanDatabase(databaseFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(this,
                                "Cleaning completed for database: " + selectedDatabase,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error cleaning database: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Selected database file does not exist.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No database selected!",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
