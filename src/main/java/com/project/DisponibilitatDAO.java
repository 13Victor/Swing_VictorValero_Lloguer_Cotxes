package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisponibilitatDAO {
    public static String getVehicleAvailability(int vehicleId) {
        String sql = "SELECT estat FROM disponibilitats WHERE vehicle_id = " + vehicleId;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return (String) row.get("estat");
        }
        return "No disponible"; // Por defecto, si no se encuentra
    }
    public static void updateVehicleAvailability(int vehicleId, String estat) {
        String sql = "UPDATE disponibilitats SET estat = '" + estat + "' WHERE vehicle_id = " + vehicleId;
        AppData db = AppData.getInstance();
        db.update(sql);
    }
    public static ArrayList<Integer> getAvailableVehicleIds() {
        String sql = "SELECT vehicle_id FROM disponibilitats WHERE estat = 'Disponible'";
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        ArrayList<Integer> ids = new ArrayList<>();
        for (Map<String, Object> row : results) {
            ids.add((Integer) row.get("vehicle_id"));
        }
        return ids;
    }
    public static boolean isVehicleAvailable(int vehicleId) {
        return "Disponible".equals(getVehicleAvailability(vehicleId));
    }
}