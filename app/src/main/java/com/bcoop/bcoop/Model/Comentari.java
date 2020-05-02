package com.bcoop.bcoop.Model;

import com.google.firebase.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Comentari {
    private String contingut;
    private String autor;
    private Date temps;

    public Comentari() {}

    public Comentari(String contingut, String autor) {
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getTemps() {
        return temps;
    }

    public void setTemps(Date temps) {
        this.temps = temps;
    }
}