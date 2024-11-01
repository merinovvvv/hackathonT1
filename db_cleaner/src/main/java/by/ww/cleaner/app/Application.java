package by.ww.cleaner.app;

import javax.swing.*;
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

    Application(String string) {

        super(string);

        UIManager.put("OptionPane.messageFont", new Font("Dialog", Font.BOLD, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Dialog", Font.PLAIN, 16));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));


        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // margins for elements in the grid layout (top, left, bottom, right) 5px
        gbc.fill = GridBagConstraints.HORIZONTAL; // elements are stretched horizontally

        selectBaseLabel = new JLabel("Select the data base:");
        bases = new JComboBox<>();
        fillComboBoxWithDatabaseNames(bases);
        execute = new JButton("Execute");
        execute.setFocusPainted(false);

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
        this.add(selectBaseLabel, gbc);

        gbc.gridy = 1;
        this.add(bases, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.add(execute, gbc);

    }

    private static void fillComboBoxWithDatabaseNames(JComboBox<String> bases) {
        File folder = new File("../db_data");
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