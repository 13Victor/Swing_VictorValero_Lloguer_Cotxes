package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaDAO {

    public static CategoriaModel getItem(int id) {
        String sql = "SELECT v.id, v.marca, v.model, v.any, d.estat AS disponibilitat, v.foto " +
                     "FROM vehicles v " +
                     "LEFT JOIN disponibilitats d ON v.id = d.vehicle_id " +
                     "WHERE v.id = " + id;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return new CategoriaModel(
                (int)row.get("id"), 
                (String)row.get("marca"), 
                (String)row.get("model"), 
                (String)row.get("any"), 
                (String)row.get("disponibilitat"), 
                (String)row.get("foto")
            );
        }
        return null;
    }

    public static void addItem(CategoriaModel category) {
        // Primero insertar en vehicles
        String sql = "INSERT INTO vehicles (marca, model, any, foto) VALUES ('" + 
                     category.getMarca() + "', '" + 
                     category.getModel() + "', '" + 
                     category.getAny() + "', '" + 
                     category.getFoto() + "')";
        AppData db = AppData.getInstance();
        db.update(sql);
        
        // Obtener el último ID insertado
        List<Map<String, Object>> results = db.query("SELECT last_insert_rowid() as id");
        int vehicleId = (Integer) results.get(0).get("id");
        
        // Luego insertar en disponibilitats
        sql = "INSERT INTO disponibilitats (vehicle_id, estat) VALUES (" + 
              vehicleId + ", '" + category.getDisponibilitat() + "')";
        db.update(sql);
    }

    public static void updateItem(CategoriaModel category) {
        // Actualizar vehicles
        String sql = "UPDATE vehicles SET " +
                     "marca = '" + category.getMarca() + "', " +
                     "model = '" + category.getModel() + "', " + 
                     "any = '" + category.getAny() + "', " +
                     "foto = '" + category.getFoto() + "' " +
                     "WHERE id = " + category.getId();
        AppData db = AppData.getInstance();
        db.update(sql);
        
        // Actualizar disponibilitat
        DisponibilitatDAO.updateVehicleAvailability(category.getId(), category.getDisponibilitat());
    }

    public static void deleteItem(int id) {
        // Primero borramos las referencias en disponibilitats
        String sql = "DELETE FROM disponibilitats WHERE vehicle_id = " + id;
        AppData db = AppData.getInstance();
        db.update(sql);
        
        // Luego borramos el vehículo
        sql = "DELETE FROM vehicles WHERE id = " + id;
        db.update(sql);
    }

    public static ArrayList<CategoriaModel> getAll() {
        // Retorna una llista amb totes les vehicles
        String sql = "SELECT v.id, v.marca, v.model, v.any, d.estat AS disponibilitat, v.foto " +
                     "FROM vehicles v " +
                     "LEFT JOIN disponibilitats d ON v.id = d.vehicle_id";
        AppData db = AppData.getInstance();
        ArrayList<CategoriaModel> list = new ArrayList<>();
        List<Map<String, Object>> results = db.query(sql);
        
        for (Map<String, Object> row : results) {
            int id = (Integer) row.get("id");
            String marca = (String) row.get("marca");
            String model = (String) row.get("model");
            String any = (String) row.get("any");
            String disponibilitat = (String) row.get("disponibilitat");
            String foto = (String) row.get("foto");
            list.add(new CategoriaModel(id, marca, model, any, disponibilitat, foto));
        }
        return list;
    }
    
    // Nuevo método: Retorna solo los vehículos disponibles
    public static ArrayList<CategoriaModel> getAvailableVehicles() {
        ArrayList<CategoriaModel> allVehicles = getAll();
        ArrayList<CategoriaModel> availableVehicles = new ArrayList<>();
        
        for (CategoriaModel vehicle : allVehicles) {
            if ("Disponible".equals(vehicle.getDisponibilitat())) {
                availableVehicles.add(vehicle);
            }
        }
        
        return availableVehicles;
    }
}