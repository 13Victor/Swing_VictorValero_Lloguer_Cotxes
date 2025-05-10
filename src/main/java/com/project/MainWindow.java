package com.project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    // Pestañas para navegación
    JTabbedPane tabbedPane = new JTabbedPane();

    // Controladores y vistas
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
        
        // Intentar aplicar look and feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Configuración de la ventana principal
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Centrar ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        // Inicializa los controladores
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
        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        // Cabecera con título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Gestor de Lloguer de Vehicles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Inicializa las vistas
        categoriaView = new CategoriaView();
        rentalView = new RentalView();
        clientsView = new ClientsView();
        clientsVehiclesView = new ClientsVehiclesView();
        vehicleCatalogView = new VehicleCatalogView(); // Nueva vista
    
        // Configurar el tabbedPane
        UITheme.styleTabbedPane(tabbedPane);
        
        // Añadir pestañas
        tabbedPane.addTab("Vehicles", null, categoriaView, "Gestionar vehicles disponibles");
        tabbedPane.addTab("Lloguers", null, rentalView, "Gestionar lloguers actius");
        tabbedPane.addTab("Clients", null, clientsView, "Gestionar clients");
        tabbedPane.addTab("Observacions", null, clientsVehiclesView, "Gestionar observacions");
        tabbedPane.addTab("Catàleg", null, vehicleCatalogView, "Veure catàleg de vehicles");
        
        // Añadir teclas de acceso rápido para cambiar de pestaña
        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        
        // Agregar acción para navegar hacia adelante
        tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlTab, "navigateNext");
        tabbedPane.getActionMap().put("navigateNext", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex + 1) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            }
        });
        
        // Agregar acción para navegar hacia atrás
        tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlShiftTab, "navigatePrev");
        tabbedPane.getActionMap().put("navigatePrev", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex - 1 + tabbedPane.getTabCount()) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            }
        });
        
        // Barra de estado inferior
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JLabel statusLabel = new JLabel("Aplicació iniciada correctament");
        statusLabel.setFont(UITheme.LABEL_FONT);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(UITheme.LABEL_FONT);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(dateLabel, BorderLayout.EAST);
        
        // Organizar todo en el panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        // Añadir panel principal al frame
        getContentPane().add(mainPanel);
    }
}