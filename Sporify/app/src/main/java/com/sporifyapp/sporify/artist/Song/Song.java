package com.example.sporify.artist.Song;

public class Song {


    String title = null;
    Boolean favourite = false;
    String path = null;

    public Song() {

        //String title, Boolean favourite, String path

      //  this.title = title;
       // this.favourite = favourite;
       // this.path = path;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
