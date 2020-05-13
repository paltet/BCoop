package com.bcoop.bcoop.ui.chat.chatnotification;

public class Data {
    private String user;
    private String title;
    private String body;
    private String sented;

    public Data(String user, String title, String body, String sented) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.sented = sented;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
