package com.project;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CategoriaView extends JPanel {

    public JButton reloadButton = new JButton("Refresca");
    public JCheckBox newItemCheckBox = new JCheckBox("Nou Vehicle");
    public JComboBox<String> itemComboBox = new JComboBox<>();
    public JLabel loadingLabel = new JLabel("Carregant ...");
    public JTextField itemNameField = new JTextField();
    public JTextField itemName2Field = new JTextField();
    public JTextField itemYearField = new JTextField();
    // Cambio: Reemplazar campo de texto por checkbox
    public JCheckBox itemAvailabilityCheckBox = new JCheckBox("Disponible");
    public JTextField itemPhotoField = new JTextField();
    public JButton addButton = new JButton("Afegir");
    public JButton modifyButton = new JButton("Modificar");
    public JButton deleteButton = new JButton("Esborrar");
    public JLabel imageLabel = new JLabel();
    public JButton selectImageButton = new JButton("Seleccionar Imatge");
    public JFileChooser fileChooser = new JFileChooser();

    public CategoriaView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    private void initComponents() {
        // [Código existente hasta el punto donde se añadía el campo de disponibilidad]
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(UITheme.BACKGROUND_COLOR);
        
        // Usamos un BorderLayout principal para controlar mejor la posición
        setLayout(new BorderLayout());
        
        // Panel contenedor principal (TOP)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Título de la sección
        JLabel titleLabel = new JLabel("Gestió de Vehicles");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel de acciones con reloadButton y loadingLabel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, reloadButton.getPreferredSize().height));
        
        // Aplicar estilo al botón
        UITheme.styleButton(reloadButton, UITheme.PRIMARY_COLOR);
        reloadButton.setMnemonic(KeyEvent.VK_R); // Alt+R para refrescar
        reloadButton.setToolTipText("Actualitzar el llistat de vehicles (Alt+R)");
        
        actionPanel.add(reloadButton);
        actionPanel.add(Box.createHorizontalGlue());
        
        loadingLabel.setForeground(UITheme.DANGER_COLOR);
        loadingLabel.setFont(UITheme.LABEL_FONT);
        loadingLabel.setVisible(false);
        actionPanel.add(loadingLabel);
        
        contentPanel.add(actionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Separador estilizado
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(UITheme.SECONDARY_COLOR);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel de selección (nuevo vehículo o modificar)
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        newItemCheckBox.setFont(UITheme.LABEL_FONT);
        newItemCheckBox.setBackground(UITheme.BACKGROUND_COLOR);
        newItemCheckBox.setMnemonic(KeyEvent.VK_N); // Alt+N
        newItemCheckBox.setToolTipText("Activar per afegir un nou vehicle (Alt+N)");
        newItemCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        selectionPanel.add(newItemCheckBox);
        contentPanel.add(selectionPanel);

        // ComboBox para seleccionar vehículos
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setBackground(UITheme.BACKGROUND_COLOR);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height + 10));
        
        JLabel comboLabel = new JLabel("Vehicle: ");
        comboLabel.setFont(UITheme.LABEL_FONT);
        comboLabel.setLabelFor(itemComboBox);
        comboPanel.add(comboLabel);
        comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        UITheme.styleComboBox(itemComboBox);
        itemComboBox.setToolTipText("Selecciona un vehicle per modificar");
        itemComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemComboBox.getPreferredSize().height));
        comboPanel.add(itemComboBox);
        
        contentPanel.add(comboPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel principal con layout horizontal para formulario e imagen
        JPanel mainFormPanel = new JPanel();
        mainFormPanel.setLayout(new BoxLayout(mainFormPanel, BoxLayout.X_AXIS));
        mainFormPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainFormPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel de previsualización de imagen (ahora a la izquierda con tamaño fijo)
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(UITheme.CARD_COLOR);
        previewPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            "Previsualització"
        ));
        // Establecer un ancho fijo pero menor para dar más espacio al formulario
        previewPanel.setPreferredSize(new Dimension(180, 200));
        previewPanel.setMinimumSize(new Dimension(180, 200));
        previewPanel.setMaximumSize(new Dimension(180, 250));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setText("No hi ha imatge seleccionada");

        previewPanel.add(imageLabel, BorderLayout.CENTER);

        // Panel de formulario principal con más espacio
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UITheme.CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Reducir el padding aquí también
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Añadir campos del formulario con spacing reducido
        formPanel.add(createLabeledField("Marca:", itemNameField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Reducir el espacio entre campos

        formPanel.add(createLabeledField("Model:", itemName2Field));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(createLabeledField("Any:", itemYearField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // CAMBIO: Reemplazar el campo de texto por un checkbox
        JPanel availabilityPanel = new JPanel();
        availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.X_AXIS));
        availabilityPanel.setBackground(UITheme.CARD_COLOR);
        availabilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        availabilityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemAvailabilityCheckBox.getPreferredSize().height));
        
        JLabel availabilityLabel = new JLabel("Disponibilitat:");
        availabilityLabel.setFont(UITheme.LABEL_FONT);
        availabilityLabel.setPreferredSize(new Dimension(100, availabilityLabel.getPreferredSize().height));
        availabilityLabel.setLabelFor(itemAvailabilityCheckBox);
        
        itemAvailabilityCheckBox.setFont(UITheme.LABEL_FONT);
        itemAvailabilityCheckBox.setBackground(UITheme.CARD_COLOR);
        
        availabilityPanel.add(availabilityLabel);
        availabilityPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        availabilityPanel.add(itemAvailabilityCheckBox);
        availabilityPanel.add(Box.createHorizontalGlue()); // Para que el checkbox no ocupe todo el ancho
        
        formPanel.add(availabilityPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Ajustar también el campo de foto
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.X_AXIS));
        photoPanel.setBackground(UITheme.CARD_COLOR);
        photoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        photoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, selectImageButton.getPreferredSize().height));

        JLabel photoLabel = new JLabel("Foto:");
        photoLabel.setFont(UITheme.LABEL_FONT);
        photoLabel.setPreferredSize(new Dimension(100, photoLabel.getPreferredSize().height));

        itemPhotoField.setEditable(false);
        // Reducir padding en el campo de foto
        itemPhotoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        ));

        // Reducir margen del botón para que ocupe menos espacio
        selectImageButton.setMargin(new Insets(1, 6, 1, 6));

        photoPanel.add(photoLabel);
        photoPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        photoPanel.add(itemPhotoField);
        photoPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        photoPanel.add(selectImageButton);

        formPanel.add(photoPanel);

        // Añadir espaciado horizontal entre el panel de imagen y el formulario
        mainFormPanel.add(previewPanel);
        mainFormPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Reducir el espaciado horizontal
        mainFormPanel.add(formPanel);
        
        contentPanel.add(mainFormPanel);
        
        // Añadir panel contenedor al panel principal (arriba)
        add(contentPanel, BorderLayout.NORTH);
        
        // Panel de botones (abajo)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        UITheme.styleButton(addButton, UITheme.SUCCESS_COLOR);
        addButton.setMnemonic(KeyEvent.VK_A); // Alt+A
        addButton.setToolTipText("Afegir nou vehicle (Alt+A)");
        
        UITheme.styleButton(modifyButton, UITheme.PRIMARY_COLOR);
        modifyButton.setMnemonic(KeyEvent.VK_M); // Alt+M
        modifyButton.setToolTipText("Modificar vehicle seleccionat (Alt+M)");
        
        UITheme.styleButton(deleteButton, UITheme.DANGER_COLOR);
        deleteButton.setMnemonic(KeyEvent.VK_E); // Alt+E
        deleteButton.setToolTipText("Esborrar vehicle seleccionat (Alt+E)");
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(modifyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        
        // Añadir panel de botones al panel principal (abajo)
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para crear un campo con etiqueta
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