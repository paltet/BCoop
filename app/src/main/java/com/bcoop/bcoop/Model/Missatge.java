package com.bcoop.bcoop.Model;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class Missatge {

    private String remitent;
    private String receptor;
    private String text;
    private String fitxer;
    private Date temps;

    public Missatge() {}
    public Missatge(String remitent, String receptor, String text, String fitxer) {
        this.remitent = remitent;
        this.receptor = receptor;
        this.text = text;
        this.fitxer = fitxer;
        this.temps = Calendar.getInstance().getTime();
    }

    public String getRemitent() {
        return remitent;
    }

    public void setRemitent(String remitent) {
        this.remitent = remitent;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTemps() {
        return temps;
    }

    public void setTemps(Date temps) {
        this.temps = temps;
    }

    public String getFitxer() {
        return fitxer;
    }

    public void setFitxer(String fitxer) {
        this.fitxer = fitxer;
    }
}
