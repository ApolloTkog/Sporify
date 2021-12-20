package com.myapp.sporify_backend.controllers;

import com.myapp.sporify_backend.models.Album;
import com.myapp.sporify_backend.repositories.AlbumRepository;
import com.myapp.sporify_backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class AlbumController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @PostMapping("/album")
    public ResponseEntity<?> toggleFavoriteAlbum(@RequestBody Album album, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String currentUserId = userDetails.getId();

        // find current authenticated user
        User user = userRepository.findById(currentUserId).orElseGet(User::new);
        // get current user's albums
        Set<Album> userAlbums = user.getAlbums();

        // if user's albums contains the album we want to insert
        // then show a message
        if(userAlbums.contains(album)){
            // find album that we want to be deleted
            Album albumFound = albumRepository.findByNameAndArtist(album.getName(), album.getArtist()).orElseGet(Album::new);

            // delete from albums collection
            albumRepository.delete(albumFound);

            // remove it from user's set
            userAlbums.remove(albumFound);

            // update user's album list
            // with newly deleted album
            userRepository.save(user);

            return ResponseEntity.ok().body(new MessageResponse("Album with id " + albumFound.getId() + " has been successfully deleted!"));
        }

        // set album's owner
        album.setUserId(user);
        // create album and insert it inside collection
        albumRepository.insert(album);
        userAlbums.add(album);

        // update user's album list
        // with newly inserted album
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(
                "Successfully added!",
                album
        ));

    }

    @GetMapping("/albums")
    public Set<Album> getFavoriteAlbums(Authentication authentication){
        // gets current userId from token from the authorization header
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String currentUseId = userDetails.getId();

        // finding user and returning his album set
        return userRepository.findById(currentUseId).orElseGet(User::new).getAlbums();
    }
}