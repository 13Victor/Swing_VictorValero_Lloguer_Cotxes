package com.project;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class RentalView extends JPanel {

    public JButton reloadButton = new JButton("Refresca");
    public JCheckBox newItemCheckBox = new JCheckBox("Nou lloguer");
    public JComboBox<String> itemComboBox = new JComboBox<>();
    public JLabel loadingLabel = new JLabel("Carregant ...");
    public JTextField itemNameField = new JTextField();
    public JTextField itemDescriptionField = new JTextField();
    //public JFormattedTextField itemPriceField = new JFormattedTextField();
    public JButton addButton = new JButton("Afegir");
    public JButton modifyButton = new JButton("Modificar");
    public JButton deleteButton = new JButton("Esborrar");
    public JComboBox<String> categoryComboBox = new JComboBox<>();
    public JComboBox<String> clientComboBox = new JComboBox<>();

    public RentalView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newItemCheckBox.setSelected(true); // Inicializar como seleccionado
        initComponents();
    }

    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(UIStyle.BACKGROUND_COLOR);

        // Espai superior fins als botons
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Afegir botons (refresca i productes) en un panell horitzontal
        JPanel horizPanel0 = new JPanel();
        Dimension sizeHorizPanel0 = new Dimension(Integer.MAX_VALUE, reloadButton.getMinimumSize().height);
        horizPanel0.setPreferredSize(sizeHorizPanel0);
        horizPanel0.setLayout(new BoxLayout(horizPanel0, BoxLayout.X_AXIS));
        horizPanel0.add(reloadButton);
        horizPanel0.add(Box.createHorizontalGlue()); 
        horizPanel0.add(loadingLabel);
        loadingLabel.setVisible(false);
        loadingLabel.setForeground(Color.RED);
        add(horizPanel0);

        // Linia separadora 0
        JSeparator separator0 = new JSeparator(JSeparator.HORIZONTAL);
        separator0.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        separator0.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        add(separator0);

        // Etiqueta de nova categoria
        newItemCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(newItemCheckBox);

        // Desplegable de categories
        itemComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getMinimumSize().height));
        add(itemComboBox);
        
        // Camps del formulari
        add(createLabeledField("Data Inici:", itemNameField));
        add(createLabeledField("Data Final:", itemDescriptionField));
        add(createLabeledField("Vehicle:", categoryComboBox));
        add(createLabeledField("Client:", clientComboBox));

        // Afegir espai vertical fins als bontons
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Alinear botons (afegir, modificar i esborrar) en un panell horitzontal
        JPanel horizPanel1 = new JPanel();
        horizPanel1.setLayout(new BoxLayout(horizPanel1, BoxLayout.X_AXIS));
        horizPanel1.add(addButton);
        horizPanel1.add(Box.createRigidArea(new Dimension(10, 0)));
        horizPanel1.add(modifyButton);
        horizPanel1.add(Box.createRigidArea(new Dimension(10, 0)));
        horizPanel1.add(deleteButton);
        add(horizPanel1);

        // Expandir verticalment (per pujar els elements la resta d'elements)
        add(Box.createVerticalGlue());

        // Style components
        UIStyle.styleButton(reloadButton, UIStyle.PRIMARY_COLOR);
        UIStyle.styleButton(addButton, UIStyle.PRIMARY_COLOR);
        UIStyle.styleButton(modifyButton, UIStyle.PRIMARY_COLOR);
        UIStyle.styleButton(deleteButton, UIStyle.DANGER_COLOR);

        UIStyle.styleTextField(itemNameField);
        UIStyle.styleTextField(itemDescriptionField);
        
        UIStyle.styleComboBox(itemComboBox);
        UIStyle.styleComboBox(categoryComboBox);
        UIStyle.styleComboBox(clientComboBox);

        // Style checkboxes and labels
        newItemCheckBox.setFont(UIStyle.LABEL_FONT);
        loadingLabel.setFont(UIStyle.LABEL_FONT);

        // Apply modern look to components
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 240, 240));
        
        // Style the buttons
        Color buttonColor = new Color(70, 130, 180);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        
        reloadButton.setBackground(buttonColor);
        reloadButton.setForeground(Color.WHITE);
        reloadButton.setFont(buttonFont);
        reloadButton.setBorderPainted(false);
        reloadButton.setFocusPainted(false);
        
        addButton.setBackground(buttonColor);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(buttonFont);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        
        modifyButton.setBackground(buttonColor);
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setFont(buttonFont);
        modifyButton.setBorderPainted(false);
        modifyButton.setFocusPainted(false);
        
        deleteButton.setBackground(new Color(180, 70, 70));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(buttonFont);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);

        // Style the combo box
        itemComboBox.setBackground(Color.WHITE);
        itemComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add spacing between components
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // Crea una fila amb una etiqueta i un component interactiu SWING
    private JPanel createLabeledField(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(component);
        
        return panel;
    }
}
