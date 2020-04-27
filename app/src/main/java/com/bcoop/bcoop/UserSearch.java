package com.bcoop.bcoop;

import com.bcoop.bcoop.Model.HabilitatDetall;

import java.util.Map;

public class UserSearch {

    private static Map<String, HabilitatDetall> habilitats;
    public String name;
    public String distance;
    public String imageURL;
    public Double lat;
    public Double lon;
    public int rating;
    public String email;

    private boolean expanded;



    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public UserSearch(String name, String distance, String imageURL, Map<String, HabilitatDetall> habilitats, Double lat, Double lon, int rating, String email) {
        this.name = name;
        this.distance = distance;
        this.imageURL = imageURL;
        this.habilitats = habilitats;
        this.expanded = false;
        this.lat = lat;
        this.lon = lon;
        this.rating = rating;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public static Map<String, HabilitatDetall> getHabilitats() {
        return habilitats;
    }

    public static void setHabilitats(Map<String, HabilitatDetall> hab){habilitats = hab;}

    public Double getLat() {
        return this.lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public int getRating() {
        return this.rating;
    }

    public String getMail() {
        return this.email;
    }
}
