package com.myapp.sporify_backend.repositories;

import com.myapp.sporify_backend.models.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * To album repository που κάνει extend το MongoRepository
 * Εδώ μέσα υπάρχουν πολλές λειτουργίες που δεν φαίνονται όπως το
 * insert στην βάση δεδομένων που εισάγει favorite albums ή findById, findAll, findByName κλπ
 */

@Repository
public interface AlbumRepository extends MongoRepository<Album, String>{
    List<Album> findAlbumsByArtist(String artist);

    Optional<Album> findAlbumById(String id);
    Optional<Album> findByNameAndArtist(String name, String artist);

}
