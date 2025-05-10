package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentalDAO {

    public static RentalModel getItem(int id) {
        String sql = "SELECT id, data_inici, data_final, id_vehicle, id_client FROM lloguers WHERE id = " + id;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return new RentalModel(
                (int)row.get("id"), 
                (String)row.get("data_inici"), 
                (String)row.get("data_final"),
                (int)row.get("id_vehicle"),
                (int)row.get("id_client")
            );
        }
        return null;
    }

    public static void addItem(RentalModel product) {
        String sql = String.format(Locale.US,
            "INSERT INTO lloguers (data_inici, data_final, id_vehicle, id_client) VALUES ('%s', '%s', %d, %d)",
            product.getDataInici(), product.getDataFinal(), product.getIdVehicle(), product.getIdClient()
        );
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void updateItem(RentalModel product) {
        String sql = String.format(Locale.US,
            "UPDATE lloguers SET data_inici = '%s', data_final = '%s', id_vehicle = %d, id_client = %d WHERE id = %d",
            product.getDataInici(), product.getDataFinal(), product.getIdVehicle(), product.getIdClient(), product.getId()
        );
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void deleteItem(int id) {
        String sql = "DELETE FROM lloguers WHERE id = " + id;
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static ArrayList<RentalModel> getAll() {
        String sql = "SELECT id, data_inici, data_final, id_vehicle, id_client FROM lloguers";
        AppData db = AppData.getInstance();
        ArrayList<RentalModel> list = new ArrayList<>();
        List<Map<String, Object>> results = db.query(sql);
        
        for (Map<String, Object> row : results) {
            int id = (Integer) row.get("id");
            String data_inici = (String) row.get("data_inici");
            String data_final = (String) row.get("data_final");
            int categoryId = (Integer) row.get("id_vehicle");
            int clientId = (Integer) row.get("id_client");
            list.add(new RentalModel(id, data_inici, data_final, categoryId, clientId));
        }
        return list;
    }
}
