package com.example.sporify.artist;

import com.example.sporify.artist.Song.Song;

import java.util.ArrayList;
import java.util.List;

public class Artsist {

    List<Song> songs = new ArrayList<Song>();
    String name = null;
    String bio = null;
    String picturePath = null;

    public Artsist() {

    }


    public List<Song> getSongs() {
        return (songs);
    }

    public void setSongs(List<Song> songs) {
        this.songs.add((Song) songs);
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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
