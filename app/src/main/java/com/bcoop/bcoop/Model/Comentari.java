package com.bcoop.bcoop.Model;

import java.util.Calendar;
import java.util.Date;

public class Comentari {
    private String contingut;
    private Usuari autor;
    private Date temps;

    public Comentari() {}

    public Comentari(String contingut, Usuari autor) {
        this.contingut = contingut;
        this.autor = autor;
        temps = Calendar.getInstance().getTime();
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

    public Date getTemps() {
        return temps;
    }

    public void setTemps(Date temps) {
        this.temps = temps;
    }
}