package com.myapp.sporify.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album {

    private int rank = 1;
    private String mbid = "";
    private String name = "";
    private String artistName = "";
    private String imageURL = "";
    private String artistImageURL = "";
    private String artistMbid = "";
    private long playCount;

    private List<Track> trackList = new ArrayList<>();

    public Album(){}

    public Album(String mbid, String name, String artistName, String imageURL) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
    }

    public Album(int rank, String mbid, String name, String artistName, String imageURL, long playCount) {
        this.rank = rank;
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.playCount = playCount;
    }

    public Album(String mbid, String name, String artistName, String imageURL, List<Track> trackList) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.trackList = trackList;
    }

    public Album(String mbid, String name, String artistName) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
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

    public int getRank() {
        return rank;
    }

    public long getPlayCount() {
        return playCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return   name.equals(album.name) && artistName.equals(album.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mbid, name, artistName);
    }
}

