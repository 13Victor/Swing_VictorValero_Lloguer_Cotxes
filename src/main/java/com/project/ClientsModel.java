package com.project;

public class ClientsModel {
    private int id;
    private String nom;
    private int telefon;
    private String adreca;

    // Constructor
    public ClientsModel(int id, String nom, String adreca, int telefon) {
        this.id = id;
        this.nom = nom;
        this.adreca = adreca;
        this.telefon = telefon;
    }

    // Constructor sense ID per a noves categories que encara no tenen un ID assignat
    public ClientsModel(String nom, String adreca, int telefon) {
        this.nom = nom;
        this.adreca = adreca;
        this.telefon = telefon;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdreca() {
        return adreca;
    }

    public int getTelefon() {
        return telefon;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }
}