package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaDAO {

    public static CategoriaModel getItem(int id) {
        String sql = "SELECT id, marca, model, any, disponibilitat, foto FROM vehicles WHERE id = " + id;
        AppData db = AppData.getInstance();
        List<Map<String, Object>> results = db.query(sql);
        
        if (!results.isEmpty()) {
            Map<String, Object> row = results.get(0);
            return new CategoriaModel((int)row.get("id"), (String)row.get("marca"), (String)row.get("model"), (String)row.get("any"), (String)row.get("disponibilitat"), (String)row.get("foto"));
        }
        return null;
    }

    public static void addItem(CategoriaModel category) {
        String sql = "INSERT INTO vehicles (marca, model, any, disponibilitat, foto) VALUES ('" + category.getMarca() + "', '" + category.getModel() + "', '" + category.getAny() + "', '" + category.getDisponibilitat() + "', '" + category.getFoto() + "')";
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void updateItem(CategoriaModel category) {
        String sql = "UPDATE vehicles SET marca = '" + category.getMarca() + "', model = '" + category.getModel() + "', any = '" + category.getAny() + "', disponibilitat = '" + category.getDisponibilitat() + "', foto = '" + category.getFoto() + "' WHERE id = " + category.getId();
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static void deleteItem(int id) {
        String sql = "DELETE FROM vehicles WHERE id = " + id;
        AppData db = AppData.getInstance();
        db.update(sql);
    }

    public static ArrayList<CategoriaModel> getAll() {
        // Retorna una llista amb totes les vehicles
        String sql = "SELECT id, marca, model, any, disponibilitat, foto FROM vehicles";
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
}
