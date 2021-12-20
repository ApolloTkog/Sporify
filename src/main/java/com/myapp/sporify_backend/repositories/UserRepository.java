package com.myapp.sporify_backend.repositories;

import com.myapp.sporify_backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * To album repository που κάνει extend το MongoRepository
 * Εδώ μέσα υπάρχουν πολλές λειτουργίες που δεν φαίνονται όπως το
 * insert στην βάση δεδομένων που εισάγει έναν χρήστη ή findById, findAll, findByName κλπ
 *
 * Οι μέθοδοι findByUsername, findById, existsByUsername δεν έχουν υλοποιηθεί κάπου
 * αλλά οι mongo της υλοποιεί μόνη της απλά κάνοντας extend το MongoRepository κοιτάει τα fields
 * του User πχ username, email, id και δημιουργεί μόνο τους αντίστοιχες μεθόδους findByEmail, findById ...
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}