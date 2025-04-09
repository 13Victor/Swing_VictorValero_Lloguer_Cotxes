package com.project;

public class ClientsVehiclesModel {
    private int id;
    private int id_client;
    private int id_vehicle;
    private String observacions;

    // Constructores
    public ClientsVehiclesModel(int id, int id_client, int id_vehicle, String observacions) {
        this.id = id;
        this.id_client = id_client;
        this.id_vehicle = id_vehicle;
        this.observacions = observacions;
    }

    // Constructor sin ID para nuevos registros que aún no tienen un ID asignado
    public ClientsVehiclesModel(int id_client, int id_vehicle, String observacions) {
        this.id_client = id_client;
        this.id_vehicle = id_vehicle;
        this.observacions = observacions;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdClient() {
        return id_client;
    }

    public int getIdVehicle() {
        return id_vehicle;
    }

    public String getObservacions() {
        return observacions;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIdClient(int id_client) {
        this.id_client = id_client;
    }

    public void setIdVehicle(int id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }
}
