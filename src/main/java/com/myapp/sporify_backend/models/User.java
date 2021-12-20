package com.myapp.sporify_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(value = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Reference
    private Set<Album> albums;

    @JsonIgnore
    @Reference
    private Set<Artist> artists;

    @JsonIgnore
    @Reference
    private Set<Track> tracks;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public User(String username, String email, String password, Set<Album> albums) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.albums = albums;
    }
}

