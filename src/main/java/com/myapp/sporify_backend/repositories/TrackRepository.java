package com.myapp.sporify_backend.repositories;


import com.myapp.sporify_backend.models.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackRepository extends MongoRepository<Track, String> {
    Optional<Track> findTrackById(String id);
    Optional<Track> findByNameAndArtist(String name, String artist);

}