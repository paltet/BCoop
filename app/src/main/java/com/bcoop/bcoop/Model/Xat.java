package com.bcoop.bcoop.Model;

import java.util.ArrayList;
import java.util.List;

public class Xat {

    private String usuari1;
    private String usuari2;
    private List<Missatge> missatges;

    public Xat() {}

    public Xat(String usuari1, String usuari2) {
        this.usuari1 = usuari1;
        this.usuari2 = usuari2;
        this.missatges = new ArrayList<>();
    }

    public String getUsuari1() {
        return usuari1;
    }

    public void setUsuari1(String usuari1) {
        this.usuari1 = usuari1;
    }

    public String getUsuari2() {
        return usuari2;
    }

    public void setUsuari2(String usuari2) {
        this.usuari2 = usuari2;
    }

    public List<Missatge> getMissatges() {
        return missatges;
    }

    public void setMissatges(List<Missatge> missatges) {
        this.missatges = missatges;
    }

    public void addMissatge(Missatge missatge) {
        this.missatges.add(missatge);
    }
}

