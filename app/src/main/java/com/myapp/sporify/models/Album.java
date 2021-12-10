package com.myapp.sporify.models;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String mbid = "";
    private String name = "";
    private String artistName = "";
    private String imageURL = "";
    private String artistImageURL = "";
    private String artistMbid = "";

    private List<Track> trackList = new ArrayList<>();

    public Album(){}

    public Album(String mbid, String name, String artistName, String imageURL) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
    }


    public Album(String mbid, String name, String artistName, String imageURL, List<Track> trackList) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.trackList = trackList;
    }


    public String getMbid() {
        return mbid;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getArtistName() {
        return artistName;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setArtistImageURL(String artistImageURL) {
        this.artistImageURL = artistImageURL;
    }

    public String getArtistImageURL() {
        return artistImageURL;
    }

    public void setArtistMbid(String artistMbid) {
        this.artistMbid = artistMbid;
    }

    public String getArtistMbid() {
        return artistMbid;
    }
}
