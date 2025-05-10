package com.project;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

public class VehicleCatalogView extends JPanel {
    private JScrollPane scrollPane;
    private JPanel catalogPanel;
    private ArrayList<CategoriaModel> vehicles;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    
    public VehicleCatalogView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(UITheme.BACKGROUND_COLOR);
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(UITheme.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel searchFieldPanel = new JPanel(new BorderLayout(5, 0));
        searchFieldPanel.setBackground(UITheme.BACKGROUND_COLOR);
        searchField = new JTextField();
        UITheme.styleTextField(searchField);
        searchField.setToolTipText("Cercar per marca o model");
        
        searchButton = new JButton("Cercar");
        UITheme.styleButton(searchButton, UITheme.PRIMARY_COLOR);
        searchButton.addActionListener(e -> filterVehicles());
        
        searchField.addActionListener(e -> filterVehicles()); // Al presionar Enter
        searchFieldPanel.add(searchField, BorderLayout.CENTER);
        
        JPanel filterPanel = new JPanel(new BorderLayout(5, 0));
        filterPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JLabel filterLabel = new JLabel("Disponibilitat:");
        filterLabel.setFont(UITheme.LABEL_FONT);
        
        filterComboBox = new JComboBox<>(new String[]{"Tots", "Disponible", "No disponible"});
        UITheme.styleComboBox(filterComboBox);
        filterComboBox.addActionListener(e -> filterVehicles());
        
        filterPanel.add(filterLabel, BorderLayout.WEST);
        filterPanel.add(filterComboBox, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JButton refreshButton = new JButton("Actualitzar");
        UITheme.styleButton(refreshButton, UITheme.SECONDARY_COLOR);
        refreshButton.addActionListener(e -> loadVehicles());
        
        buttonsPanel.add(refreshButton);
        
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);
        searchPanel.add(buttonsPanel, BorderLayout.SOUTH);
        catalogPanel = new JPanel();
        catalogPanel.setLayout(new GridLayout(0, 4, 10, 10)); // 4 columnas, filas automáticas
        catalogPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        scrollPane = new JScrollPane(catalogPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UITheme.BACKGROUND_COLOR);
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(UITheme.BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Catàleg de vehicles disponibles");
        infoLabel.setFont(UITheme.LABEL_FONT);
        infoLabel.setForeground(Color.GRAY);
        
        JLabel countLabel = new JLabel("0 vehicles");
        countLabel.setFont(UITheme.LABEL_FONT);
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        infoPanel.add(infoLabel, BorderLayout.WEST);
        infoPanel.add(countLabel, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        loadVehicles();
    }
    
    private void loadVehicles() {
        UtilsSwingThread.run(() -> {
            catalogPanel.removeAll();
            vehicles = CategoriaDAO.getAll();
            
            for (CategoriaModel vehicle : vehicles) {
                catalogPanel.add(createVehicleCard(vehicle));
            }
            
            updateResults(vehicles.size() + " vehicles en total");
            
            catalogPanel.revalidate();
            catalogPanel.repaint();
        });
    }
    
    private void filterVehicles() {
        String searchTerm = searchField.getText().toLowerCase();
        String filterValue = (String) filterComboBox.getSelectedItem();
        
        UtilsSwingThread.run(() -> {
            catalogPanel.removeAll();
            int count = 0;
            
            for (CategoriaModel vehicle : vehicles) {
                boolean matchesSearch = searchTerm.isEmpty() || 
                    vehicle.getMarca().toLowerCase().contains(searchTerm) || 
                    vehicle.getModel().toLowerCase().contains(searchTerm);
                
                boolean matchesFilter = "Tots".equals(filterValue) || 
                    vehicle.getDisponibilitat().equals(filterValue);
                
                if (matchesSearch && matchesFilter) {
                    catalogPanel.add(createVehicleCard(vehicle));
                    count++;
                }
            }
            
            updateResults(count + " vehicles trobats");
            
            catalogPanel.revalidate();
            catalogPanel.repaint();
        });
    }
    
    private void updateResults(String message) {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getComponentCount() > 0 && panel.getComponent(1) instanceof JLabel) {
                    JLabel countLabel = (JLabel) panel.getComponent(1);
                    countLabel.setText(message);
                    break;
                }
            }
        }
    }
    
    private JPanel createVehicleCard(CategoriaModel vehicle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(180, 120));
        imageLabel.setMaximumSize(new Dimension(180, 120));
        imageLabel.setMinimumSize(new Dimension(180, 120));
        String photoPath = vehicle.getFoto();
        if (photoPath != null && !photoPath.isEmpty()) {
            try {
                File imageFile = new File(photoPath);
                if (!imageFile.isAbsolute()) {
                    imageFile = new File(System.getProperty("user.dir"), photoPath);
                }
                
                if (imageFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    System.err.println("Archivo no encontrado: " + imageFile.getAbsolutePath());
                    imageLabel.setText("Imatge no trobada");
                    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setText("Error al carregar");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } else {
            imageLabel.setText("Sense imatge");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (imageLabel.getIcon() == null) {
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        JLabel nameLabel = new JLabel(vehicle.getMarca() + " " + vehicle.getModel());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel yearLabel = new JLabel("Any: " + vehicle.getAny());
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel availabilityLabel = new JLabel(vehicle.getDisponibilitat());
        availabilityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        availabilityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if ("Disponible".equals(vehicle.getDisponibilitat())) {
            availabilityLabel.setForeground(UITheme.SUCCESS_COLOR);
        } else {
            availabilityLabel.setForeground(UITheme.DANGER_COLOR);
        }
        card.add(imageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(yearLabel);
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(availabilityLabel);
        
        return card;
    }
}