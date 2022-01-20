package com.myapp.sporify.models;

import com.myapp.sporify.interfaces.Item;

import java.util.ArrayList;
import java.util.List;

public class Playlist implements Item {

    private String id;
    private String name;


    List<Track> playlistTracks;


    public Playlist(){

    }

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;

        playlistTracks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getPlaylistTracks() {
        return playlistTracks;
    }


    public void setPlaylistTracks(List<Track> playlistTracks) {
        this.playlistTracks = playlistTracks;
    }

    @Override
    public String getMbid() {
        return "";
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getImageURL() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Playlist";
    }
}
