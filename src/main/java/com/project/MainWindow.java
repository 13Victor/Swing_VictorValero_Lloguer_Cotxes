package com.project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    JTabbedPane tabbedPane = new JTabbedPane();
    private CategoriaController categoriaController;
    private CategoriaView categoriaView;

    private RentalController rentalController;
    private RentalView rentalView;

    private ClientsController clientsController;
    private ClientsView clientsView;

    private ClientsVehiclesController clientsVehiclesController;
    private ClientsVehiclesView clientsVehiclesView;
    
    private VehicleCatalogView vehicleCatalogView; // Nueva vista de catálogo

    public MainWindow() {
        super("Gestor de Lloguer de Vehicles");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Centrar ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        categoriaController = new CategoriaController(categoriaView, tabbedPane);
        categoriaController.start();
        rentalController = new RentalController(rentalView, tabbedPane);
        rentalController.start();
        clientsController = new ClientsController(clientsView, tabbedPane);
        clientsController.start();
        clientsVehiclesController = new ClientsVehiclesController(clientsVehiclesView, tabbedPane);
        clientsVehiclesController.start();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Gestor de Lloguer de Vehicles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        categoriaView = new CategoriaView();
        rentalView = new RentalView();
        clientsView = new ClientsView();
        clientsVehiclesView = new ClientsVehiclesView();
        vehicleCatalogView = new VehicleCatalogView(); // Nueva vista
        UITheme.styleTabbedPane(tabbedPane);
        tabbedPane.addTab("Vehicles", null, categoriaView, "Gestionar vehicles disponibles");
        tabbedPane.addTab("Lloguers", null, rentalView, "Gestionar lloguers actius");
        tabbedPane.addTab("Clients", null, clientsView, "Gestionar clients");
        tabbedPane.addTab("Observacions", null, clientsVehiclesView, "Gestionar observacions");
        tabbedPane.addTab("Catàleg", null, vehicleCatalogView, "Veure catàleg de vehicles");
        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlTab, "navigateNext");
        tabbedPane.getActionMap().put("navigateNext", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex + 1) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            }
        });
        tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlShiftTab, "navigatePrev");
        tabbedPane.getActionMap().put("navigatePrev", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex - 1 + tabbedPane.getTabCount()) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            }
        });
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JLabel statusLabel = new JLabel("Aplicació iniciada correctament");
        statusLabel.setFont(UITheme.LABEL_FONT);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(UITheme.LABEL_FONT);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(dateLabel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
    }
}