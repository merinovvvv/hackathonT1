package by.ww.cleaner.app;

import by.ww.cleaner.DatabaseCleaner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Application extends JFrame {

    JLabel selectBaseLabel;
    JComboBox<String> bases;
    JButton execute;
    boolean isDatabaseSelected = true;

    Application(String title) {

        super(title);

        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 16));


        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(255, 182, 193));
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        selectBaseLabel = new JLabel("Select the database:");
        selectBaseLabel.setForeground(new Color(60, 63, 65));
        selectBaseLabel.setBorder(new EmptyBorder(0, 0, 10, 0));


        bases = new JComboBox<>();
        bases.setBackground(Color.WHITE);
        bases.setToolTipText("Select a database from the list");
        fillComboBoxWithDatabaseNames(bases);


        execute = new JButton("Execute");
        execute.setFont(new Font("Arial", Font.BOLD, 14));
        execute.setFocusPainted(false);
        execute.setBackground(new Color(51, 153, 255));
        execute.setForeground(Color.WHITE);
        execute.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));


        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bases.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(
                            Application.this,
                            "No database is selected",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    if (isDatabaseSelected) {
                        DatabaseCleaner.cleanDatabase(bases.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(
                                Application.this,
                                "The database " + bases.getSelectedItem() + " is hidden",
                                "Confirmation",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                Application.this,
                                "The database " + bases.getSelectedItem() + " is not hidden",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        this.add(selectBaseLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        this.add(bases, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.add(execute, gbc);
    }


    private static void fillComboBoxWithDatabaseNames(JComboBox<String> bases) {
        File folder = new File("./db_data");
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

}