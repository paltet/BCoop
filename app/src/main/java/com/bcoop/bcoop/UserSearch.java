package com.bcoop.bcoop;

import java.util.ArrayList;

public class UserSearch {

    public String name;
    public String description;
    public String imageURL;
    public ArrayList<String> habilitats;
    private boolean expanded;

    public static ArrayList<UserSearch> initUsers(){

        ArrayList<UserSearch> list = new ArrayList<>();

        ArrayList<String> habilitats_edu = new ArrayList<>();

        habilitats_edu.add("option1");
        habilitats_edu.add("option5");

        list.add(new UserSearch("edu", "estic bé", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("edu22", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("joan", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("natalia", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("carles", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("toni", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("sergi", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));
        list.add(new UserSearch("maria", "hola", "https://bit.ly/CBImageCinque", habilitats_edu));

        return list;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public UserSearch(String name, String description, String imageURL, ArrayList<String> habilitats) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.habilitats = habilitats;
        this.expanded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<String> getHabilitats() {
        return habilitats;
    }
    public void setHabilitats(ArrayList<String> habilitats){this.habilitats = habilitats;}
}
