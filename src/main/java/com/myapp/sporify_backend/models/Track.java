package com.myapp.sporify_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Data
@Document(value = "tracks")
@NoArgsConstructor
public class Track {


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
        Track track = (Track) o;
        return name.equals(track.name) && artist.equals(track.artist);
    }

    // hashCode για να ξέρουμε πότε ένα album είναι ίδιο με ένα άλλο
    // ώστε να γίνεται η συγκριση στο Set<Album> ή HashSet<Album>
    @Override
    public int hashCode() {
        return Objects.hash(name, artist);
    }

}
