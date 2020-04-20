package com.bcoop.bcoop.ui.prize;

import java.sql.Time;

public class Premi {
    private String nom;
    private String descripció;
    private String imatge;
    private double preu;
    private Time time;

    public Premi() {
    }

    public Premi(String nom, String descripció, String imatge, double preu) {
        this.nom = nom;
        this.descripció = descripció;
        this.imatge = imatge;
        this.preu = preu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripció() {
        return descripció;
    }

    public void setDescripció(String descripció) {
        this.descripció = descripció;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
