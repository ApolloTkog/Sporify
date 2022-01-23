package com.myapp.sporify.models;

import com.myapp.sporify.interfaces.Item;

import java.io.Serializable;
import java.util.Objects;

public class Track implements Item, Serializable {

    private String id;
    private int rank;
    private String mbid;
    private String name = "No info";
    private String artistName;
    private String artistMbid;
    private String artistImageURL = "";
    private String imageURL = "";
    private String youtubeURL;
    private int duration;

    private String summary = "No summary for this track", content = "No content for this track";

    public Track(){}

    public Track(int rank, String name, String artistName, String imageURL) {
        this.rank = rank;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
    }

    public Track(String name, String artistName, int duration){
        this.name = name;
        this.artistName = artistName;
        this.duration = duration;
    }

    public Track(String artistMbid, String name, String youtubeURL, String imageURL) {
        this.name = name;
        this.artistMbid = artistMbid;
        this.imageURL = imageURL;
        this.youtubeURL = youtubeURL;
    }

    public Track(int rank , String name, String imageURL, String youtubeURL, String artistName){
        this.rank = rank;
        this.name = name;
        this.imageURL = imageURL;
        this.youtubeURL = youtubeURL;
        this.artistName = artistName;
    }

    public Track(String name, String imageURL, String artistName, String artistMbid, String summary, String content){
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.artistMbid = artistMbid;

        this.summary = summary;
        this.content = content;
    }

    public Track(String artistMbid, String name, String artistName) {
        this.name = name;
        this.artistName = artistName;
        this.artistMbid = artistMbid;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
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
        return artistName;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtistImageURL() {
        return artistImageURL;
    }

    public void setArtistImageURL(String artistImageURL) {
        this.artistImageURL = artistImageURL;
    }

    public String getArtistMbid() {
        return artistMbid;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return name.equals(track.name) && artistName.equals(track.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artistName);
    }
}
