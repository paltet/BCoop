package com.bcoop.bcoop.Model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuari {
    private String email;
    private String nom;
    private String foto;
    private boolean esPremium;
    private boolean blocked;
    private int monedes;
    private double locationLatitude;
    private double locationLongitude;
    private boolean esAdministrador;
    private boolean esTienda;
    private Map<String, HabilitatDetall> habilitats;
    private Map<String, List<String>> xats;
    private List<String> idiomas;
    private List<String> serveis; //llista amb els idServei de tots els seus serveis
    private int nivell;
    private int experiencia;
    private int valoracio;
    private String token;
    private Date lastBloqueig;

    public Usuari() {}

    public Usuari(String mail, String usrname, String foto, Double locationLatitude, Double locationLongitude) {
        this.email = mail;
        this.nom = usrname;
        this.foto = foto;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        esPremium = false;
        monedes = 150;
        esAdministrador = false;
        blocked = false;
        habilitats = new HashMap<>();
        xats = new HashMap<>();
        idiomas = new ArrayList<>();
        serveis = new ArrayList<>();
        nivell = 1;
        experiencia = 0;
        valoracio = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isEsPremium() {
        return esPremium;
    }

    public void setEsPremium(boolean esPremium) {
        this.esPremium = esPremium;
    }

    public int getMonedes() {
        return monedes;
    }

    public void setMonedes(int monedes) {
        this.monedes = monedes;
    }

    public boolean isEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public int getNivell() {
        return nivell;
    }

    public void setNivell(int nivell) {
        this.nivell = nivell;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getValoracio() {
        return valoracio;
    }

    public void setValoracio(int valoracio) {
        this.valoracio = valoracio;
    }

    public Map<String, HabilitatDetall> getHabilitats() {
        return habilitats;
    }

    public void setHabilitats(Map<String, HabilitatDetall> habilitats) {
        this.habilitats = habilitats;
    }

    public Map<String, List<String>> getXats() {
        return xats;
    }

    public void setXats(Map<String, List<String>> xats) {
        this.xats = xats;
    }

    public void addXatWithUser(String with, String xat) {
        if (this.xats.containsKey(with)) {
            this.xats.get(with).add(xat);
        }
        else {
            List<String> xats = new ArrayList<>();
            xats.add(xat);
            this.xats.put(with, xats);
        }
    }

    public void deleteXatWithUser(String with) {
        this.xats.remove(with);
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<String> getServeis() {
        return serveis;
    }

    public void setServeis(List<String> serveis) {
        this.serveis = serveis;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastBloqueig() {
        return lastBloqueig;
    }

    public void setLastBloqueig(Date lastBloqueig) {
        this.lastBloqueig = lastBloqueig;
    }

    public boolean isEsTienda() {
        return esTienda;
    }

    public void setEsTienda(boolean esTienda) {
        this.esTienda = esTienda;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
