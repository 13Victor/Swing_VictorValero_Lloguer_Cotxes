package com.project;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;

public class Main {

    // Crear el singleton (això es connecta a la base de dades)
    private static AppData db = AppData.getInstance();

    public static void main(String[] args) {
        
        System.out.println("\nIniciar les dades de la base de dades:");
        initData();

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            
            // Afegir un WindowListener per tancar la base de dades quan la finestra es tanca
            mainWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closeApp();
                }
            });
        });
    }

    private static void closeApp() {
        db.close();
        System.out.println("Connexió amb la base de dades tancada.");
        System.exit(0);
    }
public static void initData() {
    // Obtenir un apuntador al singleton de la base de dades
    AppData db = AppData.getInstance();

    // Esborrar les taules 'vehicles' i 'lloguers' si existeixen
    db.update("DROP TABLE IF EXISTS vehicles");
    db.update("DROP TABLE IF EXISTS clients");
    db.update("DROP TABLE IF EXISTS lloguers");
    db.update("DROP TABLE IF EXISTS clients_vehicles");

    // Crear la taula 'vehicles'
    db.update("CREATE TABLE IF NOT EXISTS vehicles (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "marca TEXT NOT NULL," +
                      "model TEXT NOT NULL," +
                      "any TEXT NOT NULL," +
                      "disponibilitat TEXT NOT NULL," +
                      "foto TEXT NOT NULL)");
    
    // Crear la taula 'clients'
    db.update("CREATE TABLE IF NOT EXISTS clients (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "nom TEXT NOT NULL," +
                      "adreca TEXT NOT NULL," +
                      "telefon INTEGER)");

    // Crear la taula 'lloguers'
    db.update("CREATE TABLE IF NOT EXISTS lloguers (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "id_vehicle INTEGER," +
                      "id_client INTEGER," +
                      "data_inici TEXT," +
                      "data_final TEXT," +
                      "FOREIGN KEY (id_vehicle) REFERENCES vehicles(id)," +
                      "FOREIGN KEY (id_client) REFERENCES clients(id))");
    
    // Crear la taula 'lloguers'
    db.update("CREATE TABLE IF NOT EXISTS clients_vehicles (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "id_vehicle INTEGER," +
                      "id_client INTEGER," +
                      "observacions TEXT," +
                      "FOREIGN KEY (id_vehicle) REFERENCES vehicles(id)," +
                      "FOREIGN KEY (id_client) REFERENCES clients(id))");

    // Inserir dades a la taula 'vehicles'
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Toyota', 'Corolla', '2022-01-01', 'Disponible', '(foto1)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Honda', 'Civic', '2022-01-02', 'Disponible', '(foto2)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Ford', 'Mustang', '2022-01-03', 'Disponible', '(foto3)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Chevrolet', 'Camaro', '2022-01-04', 'Disponible', '(foto4)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Nissan', 'Altima', '2022-01-05', 'Disponible', '(foto5)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Audi', 'A4', '2022-01-06', 'Disponible', '(foto6)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('BMW', '3 Series', '2022-01-07', 'Disponible', '(foto7)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Mercedes', 'C-Class', '2022-01-08', 'Disponible', '(foto8)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Volkswagen', 'Golf', '2022-01-09', 'Disponible', '(foto9)')");
    db.update("INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('Tesla', 'Model 3', '2022-01-10', 'Disponible', '(foto10)')");

    // Inserir dades a la taula 'clients'
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Maria Lopez', 'C/Sant Pau nº12 1ºB', 612345678)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Juan Martinez', 'C/Gran Via nº4 3ºA', 625678912)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Ana Garcia', 'C/Rambla nº21 5ºC', 634567890)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Carlos Ruiz', 'C/Aribau nº8 2ºD', 645789123)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Laura Torres', 'C/Balmes nº45 1ºA', 656789345)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Pablo Sanchez', 'C/Ronda nº30 2ºB', 667891234)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Marta Fernandez', 'C/Pelayo nº15 3ºC', 678912345)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Sergio Gomez', 'C/Gran Via nº10 4ºA', 689123456)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Lucia Diaz', 'C/Rambla nº5 1ºD', 690234567)");
    db.update("INSERT INTO clients (nom, adreca, telefon) VALUES ('Alberto Ruiz', 'C/Aragon nº20 2ºB', 701345678)");

// Insertar datos en la tabla 'lloguers'
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (1, 1, '2022-01-01', '2022-01-07')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (2, 2, '2022-01-08', '2022-01-14')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (3, 3, '2022-01-15', '2022-01-21')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (4, 4, '2022-01-22', '2022-01-28')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (5, 5, '2022-01-29', '2022-02-04')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (6, 6, '2022-02-05', '2022-02-11')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (7, 7, '2022-02-12', '2022-02-18')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (8, 8, '2022-02-19', '2022-02-25')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (9, 9, '2022-02-26', '2022-03-04')");
db.update("INSERT INTO lloguers (id_vehicle, id_client, data_inici, data_final) VALUES (10, 10, '2022-03-05', '2022-03-11')");

// Insertar datos en la tabla 'clients_vehicles'
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (1, 1, 'El coche fue alquilado en perfecto estado y regresó sin daños.')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (2, 2, 'El cliente informó problemas con el aire acondicionado durante el alquiler.')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (3, 3, 'El vehículo fue devuelto con el tanque lleno y sin incidentes.')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (4, 4, 'El cliente solicitó una extensión del periodo de alquiler debido a retrasos en su viaje.')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (5, 5, 'Se observó un rayón en la puerta delantera izquierda al devolver el coche.')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (6, 6, 'Observacion6')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (7, 7, 'Observacion7')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (8, 8, 'Observacion8')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (9, 9, 'Observacion9')");
db.update("INSERT INTO clients_vehicles (id_vehicle, id_client, observacions) VALUES (10, 10, 'Observacion10')");

}
}
