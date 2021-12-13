package com.myapp.sporify.models;

import java.io.Serializable;

public class Artist implements Serializable {

    private String mbid;
    private String name;
    private long playcount;
    private long listeners;
    private String imageURL;
    private String biography;
    private String genre;

    public Artist(){}

    public Artist(String mbid, String name,String imageURL,String genre, String biography ) {
        this.mbid = mbid;
        this.name = name;
        this.imageURL = imageURL;
        this.genre = genre;
        this.biography = biography;
    }


    public Artist(String mbid, String name,String imageURL, long playcount, long listeners) {
        this.mbid = mbid;
        this.name = name;
        this.imageURL = imageURL;
        this.playcount = playcount;
        this.listeners = listeners;
    }

    public String getMbid() {
        return mbid;
    }

    public String getName() {
        return name;
    }

    public long getPlaycount() {
        return playcount;
    }

    public long getListeners() {
        return listeners;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getGenre() {
        return genre;
    }

    public String getBiography() {
        return biography;
    }
}

