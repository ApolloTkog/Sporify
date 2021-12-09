package com.myapp.sporify.models;

public class Track {

    private int rank;
    private String name;
    private String artistName;
    private String artistMbid;
    private String artistImageURL = "";
    private String imageURL = "";
    private String youtubeURL;
    private int duration;

    private String summary = "", content = "";

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

    public Track(int rank ,String name, String imageURL, String youtubeURL, String artistName){
        this.rank = rank;
        this.name = name;
        this.imageURL = imageURL;
        this.youtubeURL = youtubeURL;
    }

    public Track(String name,String imageURL, String artistName, String artistMbid, String summary, String content){
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.artistMbid = artistMbid;

        this.summary = summary;
        this.content = content;
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

    public String getImageURL() {
        return imageURL;
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
}
