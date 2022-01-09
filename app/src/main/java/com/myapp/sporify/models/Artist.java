package com.myapp.sporify.models;

import java.io.Serializable;

public class Artist implements Serializable, Item {

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

    public Artist(String mbid, String name) {
        this.mbid = mbid;
        this.name = name;
    }

    public Artist(String mbid, String name, String imageURL){
        this.mbid = mbid;
        this.name = name;
        this.imageURL = imageURL;
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

    @Override
    public String getTitle() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String getDescription() {
        return "Artist";
    }

    public String getGenre() {
        return genre;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return mbid.equals(artist.mbid) && name.equals(artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mbid, name);
    }
}
