package com.project;

public class ClientsModel {
    private int id;
    private String nom;
    private int telefon;
    private String adreca;
    public ClientsModel(int id, String nom, String adreca, int telefon) {
        this.id = id;
        this.nom = nom;
        this.adreca = adreca;
        this.telefon = telefon;
    }
    public ClientsModel(String nom, String adreca, int telefon) {
        this.nom = nom;
        this.adreca = adreca;
        this.telefon = telefon;
    }
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