package com.project;

public class CategoriaModel {
    private int id;
    private String marca;
    private String model;
    private String any;
    private String disponibilitat; // Lo mantenemos pero ahora se obtiene de la tabla disponibilitats
    private String foto;

    // Constructor
    public CategoriaModel(int id, String marca, String model, String any, String disponibilitat, String foto) {
        this.id = id;
        this.marca = marca;
        this.model = model;
        this.any = any;
        this.disponibilitat = disponibilitat;
        this.foto = foto;
    }

    // Constructor sin ID para nuevas categorías que aún no tienen un ID asignado
    public CategoriaModel(String marca, String model, String any, String disponibilitat, String foto) {
        this.marca = marca;
        this.model = model;
        this.any = any;
        this.disponibilitat = disponibilitat;
        this.foto = foto;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModel() {
        return model;
    }

    public String getAny() {
        return any;
    }

    public String getDisponibilitat() {
        return disponibilitat;
    }

    public String getFoto() {
        return foto;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAny(String any) {
        this.any = any;
    }

    public void setDisponibilitat(String disponibilitat) {
        this.disponibilitat = disponibilitat;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}