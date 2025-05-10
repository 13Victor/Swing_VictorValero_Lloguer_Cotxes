package com.project;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ClientsVehiclesView extends JPanel {

    public JButton reloadButton = new JButton("Refresca");
    public JCheckBox newItemCheckBox = new JCheckBox("Nova observació");
    public JComboBox<String> itemComboBox = new JComboBox<>();
    public JLabel loadingLabel = new JLabel("Carregant ...");
    public JTextField itemObservationsField = new JTextField();
    public JButton addButton = new JButton("Afegir");
    public JButton modifyButton = new JButton("Modificar");
    public JButton deleteButton = new JButton("Esborrar");
    public JComboBox<String> categoryComboBox = new JComboBox<>();
    public JComboBox<String> clientComboBox = new JComboBox<>();

    public ClientsVehiclesView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newItemCheckBox.setSelected(true); // Inicializar como seleccionado
        initComponents();
    }

    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(UITheme.BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Gestió d'Observacions");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);
        
        add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, reloadButton.getPreferredSize().height));
        UITheme.styleButton(reloadButton, UITheme.PRIMARY_COLOR);
        reloadButton.setMnemonic(KeyEvent.VK_R); // Alt+R para refrescar
        reloadButton.setToolTipText("Actualitzar el llistat d'observacions (Alt+R)");
        
        actionPanel.add(reloadButton);
        actionPanel.add(Box.createHorizontalGlue());
        
        loadingLabel.setForeground(UITheme.DANGER_COLOR);
        loadingLabel.setFont(UITheme.LABEL_FONT);
        loadingLabel.setVisible(false);
        actionPanel.add(loadingLabel);
        
        add(actionPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(UITheme.SECONDARY_COLOR);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
        add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        newItemCheckBox.setFont(UITheme.LABEL_FONT);
        newItemCheckBox.setBackground(UITheme.BACKGROUND_COLOR);
        newItemCheckBox.setMnemonic(KeyEvent.VK_N); // Alt+N
        newItemCheckBox.setToolTipText("Activar per afegir una nova observació (Alt+N)");
        newItemCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        selectionPanel.add(newItemCheckBox);
        add(selectionPanel);
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setBackground(UITheme.BACKGROUND_COLOR);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height + 10));
        
        JLabel comboLabel = new JLabel("Observació: ");
        comboLabel.setFont(UITheme.LABEL_FONT);
        comboLabel.setLabelFor(itemComboBox);
        comboPanel.add(comboLabel);
        comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        UITheme.styleComboBox(itemComboBox);
        itemComboBox.setToolTipText("Selecciona una observació per modificar");
        itemComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height));
        comboPanel.add(itemComboBox);
        
        add(comboPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UITheme.CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(createLabeledField("Vehicle:", categoryComboBox));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createLabeledField("Client:", clientComboBox));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel obsPanel = new JPanel();
        obsPanel.setLayout(new BoxLayout(obsPanel, BoxLayout.X_AXIS));
        obsPanel.setBackground(UITheme.CARD_COLOR);
        obsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel obsLabel = new JLabel("Observació:");
        obsLabel.setFont(UITheme.LABEL_FONT);
        obsLabel.setPreferredSize(new Dimension(100, obsLabel.getPreferredSize().height));
        JTextArea obsArea = new JTextArea();
        obsArea.setFont(UITheme.LABEL_FONT);
        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);
        obsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane scrollPane = new JScrollPane(obsArea);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setMinimumSize(new Dimension(300, 100));
        
        obsPanel.add(obsLabel);
        obsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        obsPanel.add(scrollPane);
        
        formPanel.add(obsPanel);
        
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        UITheme.styleButton(addButton, UITheme.SUCCESS_COLOR);
        addButton.setMnemonic(KeyEvent.VK_A); // Alt+A
        addButton.setToolTipText("Afegir nova observació (Alt+A)");
        
        UITheme.styleButton(modifyButton, UITheme.PRIMARY_COLOR);
        modifyButton.setMnemonic(KeyEvent.VK_M); // Alt+M
        modifyButton.setToolTipText("Modificar observació seleccionada (Alt+M)");
        
        UITheme.styleButton(deleteButton, UITheme.DANGER_COLOR);
        deleteButton.setMnemonic(KeyEvent.VK_E); // Alt+E
        deleteButton.setToolTipText("Esborrar observació seleccionada (Alt+E)");
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(modifyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
    private JPanel createLabeledField(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(UITheme.CARD_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height));
        
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.LABEL_FONT);
        label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));
        label.setLabelFor(component);
        
        if (component instanceof JTextField) {
            UITheme.styleTextField((JTextField) component);
        } else if (component instanceof JComboBox) {
            UITheme.styleComboBox((JComboBox<?>) component);
        }
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(component);
        
        return panel;
    }
}