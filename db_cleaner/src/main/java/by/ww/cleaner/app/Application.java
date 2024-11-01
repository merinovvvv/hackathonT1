package by.ww.cleaner.app;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    JLabel selectBaseLabel;
    JComboBox<String> bases;

    Application(String string) {


        super(string);
        this.setLayout(new GridBagLayout());
        Font largerFont = new Font("Dialog", Font.BOLD, 16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // margins for elements in the grid layout (top, left, bottom, right) 5px
        gbc.fill = GridBagConstraints.HORIZONTAL; // elements are stretched horizontally

        selectBaseLabel = new JLabel("Select the data base:");
        bases = new JComboBox<>();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(selectBaseLabel, gbc);

        gbc.gridy = 1;
        this.add(bases, gbc);

    }
}
