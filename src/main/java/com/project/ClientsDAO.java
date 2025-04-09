package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientsDAO {

    public static ClientsModel getItem(int id) {
        String sql = "SELECT id, nom, adreca, telefon FROM clients WHERE id = " + id;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return new ClientsModel((int)row.get("id"), (String)row.get("nom"), (String)row.get("adreca"), (int)row.get("telefon"));
        }
        return null;
    }

    public static void addItem(ClientsModel category) {
        String sql = "INSERT INTO clients (nom, adreca, telefon) VALUES ('" + category.getNom() + "', '" + category.getAdreca() + "'," + category.getTelefon() + ")";
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void updateItem(ClientsModel category) {
        String sql = "UPDATE clients SET nom = '" + category.getNom() + "', adreca = '" + category.getAdreca() + "', telefon = " + category.getTelefon() + " WHERE id = " + category.getId();
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void deleteItem(int id) {
        String sql = "DELETE FROM clients WHERE id = " + id;
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static ArrayList<ClientsModel> getAll() {
        // Retorna una llista amb totes les clients
        String sql = "SELECT id, nom, adreca, telefon FROM clients";
        AppData db = AppData.getInstance();
        ArrayList<ClientsModel> list = new ArrayList<>();
        List<Map<String, Object>> results = db.query(sql);
        
        for (Map<String, Object> row : results) {
            int id = (Integer) row.get("id");
            String nom = (String) row.get("nom");
            String adreca = (String) row.get("adreca");
            int telefon = (Integer) row.get("telefon");
            list.add(new ClientsModel(id, nom, adreca, telefon));
        }
        return list;
    }
}
