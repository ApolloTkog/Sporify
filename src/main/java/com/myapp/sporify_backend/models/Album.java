package com.myapp.sporify_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


/**
 * Είναι ένα document της MongoDB που αντιστοιχεί στο collection albums
 * Αντιστοιχεί στα αγαπημένα album του κάθε χρήστη
 * @id Είναι ένα unique id της MongoDB
 * @name Το όνομα του album
 * @imageURL Η εικόνα του album
 * @musicBrainzId Το mbid όπως το βλέπουμε στο last.fm
 * @userId Που κάνει reference σε έναν χρήστη, δηλαδή οτι αυτό το album αντιστοιχεί σε ένα χρήστη
 */


@Data
@Document(value = "albums")
@NoArgsConstructor
public class Album {

    @Id
    private String id;

    private String name;
    private String artist;
    private String imageURL;

    private String musicBrainzId;


    @Reference
    private User userId;

    // Σύγκριση για το πότε δυο αντικείμενα album είναι ίδια
    // Κοινώς, όταν έχουν ίδιο όνομα και ίδιο καλλιτέχνη
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return name.equals(album.name) && artist.equals(album.artist);
    }

    // hashCode για να ξέρουμε πότε ένα album είναι ίδιο με ένα άλλο
    // ώστε να γίνεται η συγκριση στο Set<Album> ή HashSet<Album>
    @Override
    public int hashCode() {
        return Objects.hash(name, artist);
    }
}
