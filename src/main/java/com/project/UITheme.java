package com.project;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class UITheme {
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Azul más moderno
    public static final Color SECONDARY_COLOR = new Color(66, 165, 245);   // Azul claro
    public static final Color ACCENT_COLOR = new Color(255, 171, 0);       // Naranja/amarillo para acentos
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);      // Verde para éxito
    public static final Color DANGER_COLOR = new Color(211, 47, 47);       // Rojo para eliminar/peligro
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Gris muy claro
    public static final Color CARD_COLOR = new Color(255, 255, 255);       // Blanco para tarjetas
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final int BORDER_RADIUS = 8;
    public static final int PADDING = 10;
    public static void styleTextField(JTextField textField) {
        textField.setFont(LABEL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(2, 3, 2, 3) // Reducir el padding
        ));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding vertical reducido
        ));
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                return this;
            }
        });
    }

    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setForeground(PRIMARY_COLOR);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public static Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }
    
    public static Color darkenColor(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }

    public static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(6, 12, 6, 12)); // Padding consistente
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(lightenColor(color, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darkenColor(color, 0.1f));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(lightenColor(color, 0.1f));
                } else {
                    button.setBackground(color);
                }
            }
        });
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(LABEL_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public static JScrollPane createScrollableTextArea(int rows) {
        JTextArea textArea = new JTextArea();
        textArea.setRows(rows);
        styleTextArea(textArea);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }
}