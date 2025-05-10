package com.project;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RentalView extends JPanel {

    public JButton reloadButton = new JButton("Refresca");
    public JCheckBox newItemCheckBox = new JCheckBox("Nou lloguer");
    public JComboBox<String> itemComboBox = new JComboBox<>();
    public JLabel loadingLabel = new JLabel("Carregant ...");
    public JTextField itemNameField = new JTextField();
    public JTextField itemDescriptionField = new JTextField();
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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(UITheme.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel titleLabel = new JLabel("Gestió de Lloguers");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, reloadButton.getPreferredSize().height));
        UITheme.styleButton(reloadButton, UITheme.PRIMARY_COLOR);
        reloadButton.setMnemonic(KeyEvent.VK_R); // Alt+R para refrescar
        reloadButton.setToolTipText("Actualitzar el llistat de lloguers (Alt+R)");
        
        actionPanel.add(reloadButton);
        actionPanel.add(Box.createHorizontalGlue());
        
        loadingLabel.setForeground(UITheme.DANGER_COLOR);
        loadingLabel.setFont(UITheme.LABEL_FONT);
        loadingLabel.setVisible(false);
        actionPanel.add(loadingLabel);
        
        contentPanel.add(actionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(UITheme.SECONDARY_COLOR);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        newItemCheckBox.setFont(UITheme.LABEL_FONT);
        newItemCheckBox.setBackground(UITheme.BACKGROUND_COLOR);
        newItemCheckBox.setMnemonic(KeyEvent.VK_N); // Alt+N
        newItemCheckBox.setToolTipText("Activar per afegir un nou lloguer (Alt+N)");
        newItemCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        selectionPanel.add(newItemCheckBox);
        contentPanel.add(selectionPanel);
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setBackground(UITheme.BACKGROUND_COLOR);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height + 10));
        
        JLabel comboLabel = new JLabel("Lloguer: ");
        comboLabel.setFont(UITheme.LABEL_FONT);
        comboLabel.setLabelFor(itemComboBox);
        comboPanel.add(comboLabel);
        comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        UITheme.styleComboBox(itemComboBox);
        itemComboBox.setToolTipText("Selecciona un lloguer per modificar");
        itemComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height));
        comboPanel.add(itemComboBox);
        
        contentPanel.add(comboPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UITheme.CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(createLabeledField("Data Inici:", itemNameField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createLabeledField("Data Final:", itemDescriptionField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createLabeledField("Vehicle:", categoryComboBox));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createLabeledField("Client:", clientComboBox));
        
        contentPanel.add(formPanel);
        add(contentPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        UITheme.styleButton(addButton, UITheme.SUCCESS_COLOR);
        addButton.setMnemonic(KeyEvent.VK_A); // Alt+A
        addButton.setToolTipText("Afegir nou lloguer (Alt+A)");
        
        UITheme.styleButton(modifyButton, UITheme.PRIMARY_COLOR);
        modifyButton.setMnemonic(KeyEvent.VK_M); // Alt+M
        modifyButton.setToolTipText("Modificar lloguer seleccionat (Alt+M)");
        
        UITheme.styleButton(deleteButton, UITheme.DANGER_COLOR);
        deleteButton.setMnemonic(KeyEvent.VK_E); // Alt+E
        deleteButton.setToolTipText("Esborrar lloguer seleccionat (Alt+E)");
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(modifyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private JPanel createLabeledField(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(UITheme.CARD_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height + 5));
        
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.LABEL_FONT);
        label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));
        label.setLabelFor(component);
        
        if (component instanceof JTextField) {
            ((JTextField) component).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 3, 2, 3) // Reducir el padding vertical e horizontal
            ));
        } else if (component instanceof JComboBox) {
            UITheme.styleComboBox((JComboBox<?>) component);
        }
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(component);
        
        return panel;
    }
}