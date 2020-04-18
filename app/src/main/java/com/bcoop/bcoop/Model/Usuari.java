package com.bcoop.bcoop.Model;

import com.bcoop.bcoop.ui.prize.Premi;

import java.util.List;
import java.util.Map;

public class Usuari {
    private String email;
    private String nom;
    private String foto;
    private boolean esPremium;
    private double monedes;
    private String localitzacio;
    private boolean esAdministrador;
    private Map<String, HabilitatDetall> habilitats;
    private List<Xat> xats;
    private List<String> idiomas;
    private List<Servei> serveis;
    private int nivell;
    private int valoracio;
    private List<Premi> premis;

    public Usuari(String mail, String usrname, String foto, String localitzacio) {
        this.email = mail;
        this.nom = usrname;
        this.foto = foto;
        this.localitzacio = localitzacio;
        esPremium = false;
        monedes = 0;
        esAdministrador = false;
        nivell = 1;
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

    public double getMonedes() {
        return monedes;
    }

    public void setMonedes(double monedes) {
        this.monedes = monedes;
    }

    public String getLocalitzacio() {
        return localitzacio;
    }

    public void setLocalitzacio(String localitzacio) {
        this.localitzacio = localitzacio;
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

    public List<Xat> getXats() {
        return xats;
    }

    public void setXats(List<Xat> xats) {
        this.xats = xats;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<Servei> getServeis() {
        return serveis;
    }

    public void setServeis(List<Servei> serveis) {
        this.serveis = serveis;
    }

    public List<Premi> getPremis() {
        return premis;
    }

    public void setPremis(List<Premi> premis) {
        this.premis = premis;
    }
}
