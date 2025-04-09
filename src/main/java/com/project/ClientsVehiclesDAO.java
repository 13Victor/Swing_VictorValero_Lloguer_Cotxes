package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientsVehiclesDAO {

    public static ClientsVehiclesModel getItem(int id) {
        String sql = "SELECT id, id_client, id_vehicle, observacions FROM clients_vehicles WHERE id = " + id;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return new ClientsVehiclesModel((int)row.get("id"), (int)row.get("id_client"), (int)row.get("id_vehicle"), (String)row.get("observacions"));
        }
        return null;
    }

    public static void addItem(ClientsVehiclesModel clientsVehicles) {
        String sql = "INSERT INTO clients_vehicles (id_client, id_vehicle, observacions) VALUES (" + clientsVehicles.getIdClient() + ", " + clientsVehicles.getIdVehicle() + ", '" + clientsVehicles.getObservacions() + "')";
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void updateItem(ClientsVehiclesModel clientsVehicles) {
        String sql = "UPDATE clients_vehicles SET id_client = " + clientsVehicles.getIdClient() + ", id_vehicle = " + clientsVehicles.getIdVehicle() + ", observacions = '" + clientsVehicles.getObservacions() + "' WHERE id = " + clientsVehicles.getId();
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void deleteItem(int id) {
        String sql = "DELETE FROM clients_vehicles WHERE id = " + id;
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static ArrayList<ClientsVehiclesModel> getAll() {
        // Retorna una lista con todos los clientes
        String sql = "SELECT id, id_client, id_vehicle, observacions FROM clients_vehicles";
        AppData db = AppData.getInstance();
        ArrayList<ClientsVehiclesModel> list = new ArrayList<>();
        List<Map<String, Object>> results = db.query(sql);
        
        for (Map<String, Object> row : results) {
            int id = (Integer) row.get("id");
            int id_client = (Integer) row.get("id_client");
            int id_vehicle = (Integer) row.get("id_vehicle");
            String observacions = (String) row.get("observacions");
            list.add(new ClientsVehiclesModel(id, id_client, id_vehicle, observacions));
        }
        return list;
    }
}
