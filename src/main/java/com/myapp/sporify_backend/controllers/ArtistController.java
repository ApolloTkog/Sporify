package com.myapp.sporify_backend.controllers;


import com.myapp.sporify_backend.models.Artist;
import com.myapp.sporify_backend.models.User;
import com.myapp.sporify_backend.payload.response.MessageResponse;
import com.myapp.sporify_backend.repositories.ArtistRepository;
import com.myapp.sporify_backend.repositories.UserRepository;
import com.myapp.sporify_backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class ArtistController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @PostMapping("/artist")
    public ResponseEntity<?> toggleFavoriteArtist(@RequestBody Artist artist, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String currentUseId = userDetails.getId();

        User user = userRepository.findById(currentUseId).orElseGet(User::new);
        Set<Artist> userArtists = user.getArtists();


        if(userArtists.contains(artist)){
            Artist artistFound = new Artist();
            for(Artist x : userArtists){
                if(x.getName().equals(artist.getName())){
                    artistFound = x;
                    break;
                }
            }

            artistRepository.delete(artistFound);

            userArtists.remove(artistFound);

            userRepository.save(user);

            return ResponseEntity.ok().body(new MessageResponse("Artist with id " + artistFound.getId() + " has been successfully deleted!"));
        }

        // set track's owner
        artist.setUserId(user);
        artistRepository.insert(artist);
        userArtists.add(artist);

        userRepository.save(user);


        return ResponseEntity.ok(new MessageResponse(
                "Successfully added!",
                artist
        ));

    }

    @GetMapping("/artists")
    public Set<Artist> getFavoriteArtists(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String currentUseId = userDetails.getId();

        return userRepository.findById(currentUseId).orElseGet(User::new).getArtists();
    }
}