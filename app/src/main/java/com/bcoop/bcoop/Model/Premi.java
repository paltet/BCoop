package com.bcoop.bcoop.Model;


import com.google.firebase.Timestamp;

public class Premi {
    private String nom;
    private String descripció;
    private String imatge;
    private Integer preu;
    private Timestamp time;
    private boolean use;

    public Premi() {
    }

    public Premi(String nom, String descripció, String imatge, Integer preu, Timestamp time) {
        this.nom = nom;
        this.descripció = descripció;
        this.imatge = imatge;
        this.preu = preu;
        this.time = time;
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

    public Integer getPreu() {
        return preu;
    }

    public void setPreu(Integer preu) {
        this.preu = preu;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

}
