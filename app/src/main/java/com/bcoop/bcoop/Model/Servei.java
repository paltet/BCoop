package com.bcoop.bcoop.Model;

import java.util.Date;

public class Servei {
    private String proveidor;
    private String demander;
    private String habilitat;
    private Date date;
    private Integer coins_to_pay;
    private String message;
    private String estat;
    public String idServei;

    public Servei() {}

    public Servei(String idServei, String proveidor, String demander, String habilitat, Date date, Integer coins_to_pay, String message, String estat){
        this.idServei = idServei;
        this.proveidor = proveidor;
        this.demander = demander;
        this.habilitat = habilitat;
        this.date = date;
        this.coins_to_pay = coins_to_pay;
        this.message = message;
        this.estat = estat;
    }
    //getters i setters
    public String getIdServei(){return idServei;}

    public void setIdServei(String idServei){this.idServei = idServei;}

    public String getEstat(){return estat;}

    public void setEstat(String estat){this.estat = estat;}

    public String getProveidor(){return proveidor;}

    public void setProveidor(String proveidor){this.proveidor = proveidor;}

    public String getDemander() { return demander; }

    public void setDemander(String demander) {
        this.demander = demander;
    }

    public String getHabilitat() {
        return habilitat;
    }

    public void setHabilitat(String habilitat) {
        this.habilitat = habilitat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCoins_to_pay() {
        return coins_to_pay;
    }

    public void setCoins_to_pay(Integer coins_to_pay) {
        this.coins_to_pay = coins_to_pay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
