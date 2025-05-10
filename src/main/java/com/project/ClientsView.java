package com.project;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ClientsView extends JPanel {

    public JButton reloadButton = new JButton("Refresca");
    public JCheckBox newItemCheckBox = new JCheckBox("Nou client");
    public JComboBox<String> itemComboBox = new JComboBox<>();
    public JLabel loadingLabel = new JLabel("Carregant ...");
    public JTextField itemNomField = new JTextField();
    public JTextField itemAdrecaField = new JTextField();
    public JTextField itemTelefonField = new JTextField();
    public JButton addButton = new JButton("Afegir");
    public JButton modifyButton = new JButton("Modificar");
    public JButton deleteButton = new JButton("Esborrar");

    public ClientsView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(UITheme.BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel titleLabel = new JLabel("Gestió de Clients");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, reloadButton.getPreferredSize().height));
        UITheme.styleButton(reloadButton, UITheme.PRIMARY_COLOR);
        reloadButton.setMnemonic(KeyEvent.VK_R);
        reloadButton.setToolTipText("Actualitzar el llistat de clients (Alt+R)");
        
        actionPanel.add(reloadButton);
        actionPanel.add(Box.createHorizontalGlue());
        
        loadingLabel.setForeground(UITheme.DANGER_COLOR);
        loadingLabel.setFont(UITheme.LABEL_FONT);
        loadingLabel.setVisible(false);
        actionPanel.add(loadingLabel);
        
        mainPanel.add(actionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(UITheme.SECONDARY_COLOR);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(separator);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        newItemCheckBox.setFont(UITheme.LABEL_FONT);
        newItemCheckBox.setBackground(UITheme.BACKGROUND_COLOR);
        newItemCheckBox.setMnemonic(KeyEvent.VK_N);
        newItemCheckBox.setToolTipText("Activar per afegir un nou client (Alt+N)");
        newItemCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        selectionPanel.add(newItemCheckBox);
        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setBackground(UITheme.BACKGROUND_COLOR);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height));
        
        JLabel comboLabel = new JLabel("Client: ");
        comboLabel.setFont(UITheme.LABEL_FONT);
        comboLabel.setLabelFor(itemComboBox);
        comboPanel.add(comboLabel);
        comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        UITheme.styleComboBox(itemComboBox);
        itemComboBox.setToolTipText("Selecciona un client per modificar");
        comboPanel.add(itemComboBox);
        
        mainPanel.add(comboPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UITheme.CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel nomPanel = createLabeledField("Nom:", itemNomField);
        formPanel.add(nomPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel adrecaPanel = createLabeledField("Adreça:", itemAdrecaField);
        formPanel.add(adrecaPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel telefonPanel = createLabeledField("Telèfon:", itemTelefonField);
        formPanel.add(telefonPanel);
        
        mainPanel.add(formPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        UITheme.styleButton(addButton, UITheme.SUCCESS_COLOR);
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.setToolTipText("Afegir nou client (Alt+A)");
        
        UITheme.styleButton(modifyButton, UITheme.PRIMARY_COLOR);
        modifyButton.setMnemonic(KeyEvent.VK_M);
        modifyButton.setToolTipText("Modificar client seleccionat (Alt+M)");
        
        UITheme.styleButton(deleteButton, UITheme.DANGER_COLOR);
        deleteButton.setMnemonic(KeyEvent.VK_E);
        deleteButton.setToolTipText("Esborrar client seleccionat (Alt+E)");
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(modifyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        add(mainPanel, BorderLayout.NORTH);
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
                BorderFactory.createEmptyBorder(2, 3, 2, 3) // Reducir el padding
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