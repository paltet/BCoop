package com.bcoop.bcoop.Model;

import com.google.firebase.Timestamp;
import com.google.type.Date;

public class Notification {
    private String content;
    private String type; // "service request", "service response", "service valoration" or "trading information"
    private String userEmail; // user, who send this notification
    private String serviceName;
    private int price;
    private int duration;
    private Timestamp dataIni;
    private Timestamp dateFi;
    private boolean response;
    private int valor;
    private String comment;
    private boolean isRead = false;
    private Timestamp time;


    public Notification(){
    }

    // service request
    public Notification(String userEmail, String serviceName, int price, int duration, Timestamp dateIni, Timestamp dateFi) {
        this.type = "Service Request";
        this.userEmail = userEmail;
        this.serviceName = serviceName;
        this.price = price;
        this.duration = duration;
        this.dataIni = dateIni;
        this.dateFi = dateFi;
        this.time = Timestamp.now();
    }

    // service response
    public Notification(String userEmail, boolean response) {
        this.type = "Service Response";
        this.userEmail = userEmail;
        this.response = response;
        this.time = Timestamp.now();
    }

    // service valoration
    public Notification(String userEmail, String serviceName, Timestamp dataFi, int valor, String comment) {
        this.type = "Service Valoration";
        this.userEmail = userEmail;
        this.serviceName = serviceName;
        this.dateFi = dataFi;
        this.valor = valor;
        this.comment = comment;
        this.time = Timestamp.now();
    }

    // "trading information"
    public Notification(String content) {
        this.type = "Trading Information";
        this.content = content;
        this.time = Timestamp.now();
    }


    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getDataIni() {
        return dataIni;
    }

    public void setDataIni(Timestamp dataIni) {
        this.dataIni = dataIni;
    }

    public Timestamp getDateFi() {
        return dateFi;
    }

    public void setDateFi(Timestamp dateFi) {
        this.dateFi = dateFi;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
