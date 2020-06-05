package com.bcoop.bcoop.Model;

import com.google.firebase.Timestamp;

public class Notification {
    private String content;
    private int type; // "1 = service request", "2 = service response", "3 = service valoration" or "4 = trading information"
    private String title;
    private String userName;
    private String userEmail; // user, who send this notification
    private String serviceName;
    private String serviceId;
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
    public Notification(String userEmail, String userName, String serviceName, String serviceId, int price, int duration, Timestamp dateIni, Timestamp dateFi, String content) {
        this.type = 1;
        this.userEmail = userEmail;
        this.userName = userName;
        this.serviceName = serviceName;
        this.serviceId = serviceId;
        this.price = price;
        this.duration = duration;
        this.dataIni = dateIni;
        this.dateFi = dateFi;
        this.time = Timestamp.now();
        this.content = content;
    }

    // service response
    public Notification(String userEmail, String userName, boolean response) {
        this.type = 2;
        this.userEmail = userEmail;
        this.userName = userName;
        this.response = response;
        this.time = Timestamp.now();
    }

    // service valoration
    public Notification(String userEmail, String userName,String serviceName, Timestamp dataFi, int valor, String comment) {
        this.type = 3;
        this.userEmail = userEmail;
        this.userName = userName;
        this.serviceName = serviceName;
        this.dateFi = dataFi;
        this.valor = valor;
        this.comment = comment;
        this.time = Timestamp.now();
    }

    // "trading information"
    public Notification(int price) {
        this.type = 4;
        this.price = price;
        this.time = Timestamp.now();
    }

    public Notification(int price, String userEmail){
        this.type = 5;
        this.price = price;
        this.time = Timestamp.now();
        this.userEmail = userEmail;
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

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
