package model;

public class Premi {
    private String nom;
    private String descripció;
    private String imatge;
    private double preu;

    public Premi() {
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
}
