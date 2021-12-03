package com.example.sporify.artist;

import java.util.ArrayList;
import java.util.List;

public class Artist {

    int artistId;
    String name = null;
    String bio = null;
    String thumbnailUrl = null;
    String genre = null;
    String mood = null;

    public Artist(){

}

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getBio() {

        return bio;
    }

    public void setBio(String bio) {

        this.bio = bio;
    }

    public String getThumbnailUrl() {

        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {

        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
