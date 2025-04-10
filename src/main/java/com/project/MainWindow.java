package com.project;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {

    // CardLayout no és un component visible
    // només gestiona la visibilitat d'un component al mateix temps
    JTabbedPane tabbedPane = new JTabbedPane();

    // Model, vistes i controlador
    private CategoriaController categoriaController;
    private CategoriaView categoriaView;

    private RentalController rentalController;
    private RentalView rentalView;

    private ClientsController clientsController;
    private ClientsView clientsView;

    private ClientsVehiclesController clientsVehiclesController;
    private ClientsVehiclesView clientsVehiclesView;

    public MainWindow() {

        // Títol i mida de la finestra
        super("Gestor de Lloguer de Vehicles");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        // Inicialitza els controladors amb els models i vistes carregats
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
        // Crea un panell amb BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // Espai superior fins als botons
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // Inicialitza les vistes
        categoriaView = new CategoriaView();
        rentalView = new RentalView();
        clientsView = new ClientsView();
        clientsVehiclesView = new ClientsVehiclesView();
    
        // Afegeix les vistes als espais del 'tabbedPane'
        tabbedPane.addTab("Vehicles", categoriaView);
        tabbedPane.addTab("Lloguers", rentalView);
        tabbedPane.addTab("Clients", clientsView);
        tabbedPane.addTab("Observacions", clientsVehiclesView);
    
        // Afegeix el tabbedPane al panell
        panel.add(tabbedPane);
    
        // Afegeix el panell al JFrame principal
        getContentPane().add(panel);
    }
    
}
