package com.bcoop.bcoop.Model;


import com.google.firebase.Timestamp;

public class Premi {
    private String nom;
    private String descripció;
    private String imatge;
    private Integer preu;
    private Timestamp time;
    private boolean use;
    private String id;

    public Premi() {
    }

    public Premi(String nom, String descripció, String imatge, Integer preu, Timestamp time, String id) {
        this.nom = nom;
        this.descripció = descripció;
        this.imatge = imatge;
        this.preu = preu;
        this.time = time;
        this.id = id;
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

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
