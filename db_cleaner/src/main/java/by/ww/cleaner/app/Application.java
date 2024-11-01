package by.ww.cleaner.app;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application extends JFrame {

    JLabel selectBaseLabel;
    JComboBox<String> bases;
    JButton execute;

    Application(String string) {

        super(string);

        UIManager.put("OptionPane.messageFont", new Font("Dialog", Font.BOLD, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Dialog", Font.PLAIN, 16));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));


        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // margins for elements in the grid layout (top, left, bottom, right) 5px
        gbc.fill = GridBagConstraints.HORIZONTAL; // elements are stretched horizontally

        selectBaseLabel = new JLabel("Select the data base:");
        bases = new JComboBox<>();
        execute = new JButton("Execute");
        execute.setFocusPainted(false);


        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(selectBaseLabel, gbc);

        gbc.gridy = 1;
        this.add(bases, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.add(execute, gbc);

    }
}