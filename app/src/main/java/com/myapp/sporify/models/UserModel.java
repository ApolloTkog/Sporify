package com.myapp.sporify.models;

public class UserModel {

    private String username;

    private String genre;

    private String mood;

    public UserModel(){}

    public UserModel(String username, String genre, String mood) {
        this.username = username;
        this.genre = genre;
        this.mood = mood;
    }

    public String getUsername() {
        return username;
    }

    public String getMood() {
        return mood;
    }

    public String getGenre() {
        return genre;
    }
}
