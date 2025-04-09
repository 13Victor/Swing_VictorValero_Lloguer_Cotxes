package com.project;

public class ProducteModel {
    private int id;
    private String data_inici;
    private String data_final;
    private int id_vehicle;
    private int id_client;

    // Constructor complet
    public ProducteModel(int id, String data_inici, String data_final, int id_vehicle, int id_client) {
        this.id = id;
        this.data_inici = data_inici;
        this.data_final = data_final;
        this.id_vehicle = id_vehicle;
        this.id_client = id_client;
    }

    // Constructor sense ID per a nous productes que encara no tenen un ID assignat
    public ProducteModel(String data_inici, String data_final, int id_vehicle, int id_client) {
        this.data_inici = data_inici;
        this.data_final = data_final;
        this.id_vehicle = id_vehicle;
        this.id_client = id_client;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDataInici() {
        return data_inici;
    }

    public String getDataFinal() {
        return data_final;
    }

    public int getIdVehicle() {
        return id_vehicle;
    }

    public int getIdClient() {
        return id_client;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDataInici(String data_inici) {
        this.data_inici = data_inici;
    }

    public void setDataFinal(String data_final) {
        this.data_final = data_final;
    }

    public void setIdVehicle(int id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public void setIdClient(int id_client) {
        this.id_client = id_client;
    }
}


