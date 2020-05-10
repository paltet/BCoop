package com.bcoop.bcoop.Model;


public class CurrentUser {
    private static CurrentUser instance = null;
    private Usuari currentUser;

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public CurrentUser() {
        currentUser = new Usuari();
    }

    public Usuari getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuari currentUser) {
        this.currentUser = currentUser;
    }
}
