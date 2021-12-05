package com.example.sporify.models;

import com.example.sporify.utils.Type;

import java.io.Serializable;

/**
 * Searchable is a model to show an item when the user uses the search feature
 *
 * Searchable model can be at the same time an Album/Artist/Track depending on the kind value
 */
public class Searchable implements Serializable {

    private String mbid = "";
    private String name;
    private String artistName;
    private String imageURL;
    private Type kind;

    public Searchable(){}

    public Searchable(String mbid, String name, String artistName, String imageURL, Type kind) {
        this.mbid = mbid;
        this.name = name;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.kind = kind;
    }

    public String getMbid() {
        return mbid;
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

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Type getKind() {
        return kind;
    }
}
