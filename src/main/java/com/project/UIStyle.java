package com.project;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIStyle {
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    public static final Color DANGER_COLOR = new Color(180, 70, 70);
    public static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    public static void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(LABEL_FONT);
        Border paddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5); // Reducido padding vertical a 2
        Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        textField.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        ((JTextField)comboBox.getEditor().getEditorComponent()).setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(2, 5, 2, 5) // Reducido padding vertical a 2
            )
        );
    }

    public static void styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
    }
}
