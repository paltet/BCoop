package com.bcoop.bcoop.Model;

import com.google.firebase.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Comentari {
    private String contingut;
    private Usuari autor;
    private Timestamp temps;

    public Comentari() {}

    public Comentari(String contingut, Usuari autor) {
        this.contingut = contingut;
        this.autor = autor;
        temps = Timestamp.now();
    }

    public String getContingut() {
        return contingut;
    }

    public void setContingut(String contingut) {
        this.contingut = contingut;
    }

    public Usuari getAutor() {
        return autor;
    }

    public void setAutor(Usuari autor) {
        this.autor = autor;
    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }
}