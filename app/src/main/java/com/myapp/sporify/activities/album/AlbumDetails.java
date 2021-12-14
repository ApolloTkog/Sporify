package com.myapp.sporify.activities.album;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.adapters.TracksAdapter;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AlbumDetails is the activity when the user is tapping on an album
 * Showing all the details of an album
 */
public class AlbumDetails extends AppCompatActivity {

    private Album albumInfo;
    private AlbumDetailsViewModel albumDetailsViewModel;
    private ImageButton addFavorite;
    private RecyclerView albumTracks;

    private List<Track> trackList;

    private TracksAdapter tracksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        // Getting favorites albums list from shared prefs
        SharedPreferences sharedPref = this.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Initializing the AlbumDetailsViewModel
        albumDetailsViewModel = new ViewModelProvider(this).get(AlbumDetailsViewModel.class);

        // finding Views from activity_album_details.xml
        TextView albumName = findViewById(R.id.album_name);
        TextView albumArtist = findViewById(R.id.album_artist);
        ImageView albumImage = findViewById(R.id.album_image);
        ImageView artistImage = findViewById(R.id.artist_image);
        addFavorite = findViewById(R.id.add_favorite);

        // initialize a list with album's tracks
        trackList = new ArrayList<>();

        // initialize the recycler view with album's tracks
        setUpRecyclerView();


        // getting the album that user clicks on
        Intent intent = getIntent();
        Searchable searchable = new Searchable();
        if(intent.getSerializableExtra("item") != null){
            searchable = (Searchable) intent.getSerializableExtra("item");
        }

        // initialize an Album object
        albumInfo = new Album();

        // Observer that observes livedata from AlbumDetailsViewModel and returns an album with all the info required
        Observer<Album> observer = album -> {
            if(album != null){
                albumInfo = album;
                trackList = new ArrayList<>(album.getTrackList());
                // setting up the adapter with album's tracks (tracklist)
                albumTracks.setAdapter(new TracksAdapter(getApplicationContext(), trackList));

                // setting up the textviews with the album info
                albumName.setText(album.getName());
                albumArtist.setText(album.getArtistName());

                // notifying adapter that data has been updated
                tracksAdapter.notifyItemInserted(trackList.size() -1);


                // Using glide library to load album image from URL on albumImage ImageView
                Glide.with(getApplicationContext())
                        .load(album.getImageURL().isEmpty() ? R.drawable.artist_placeholder : album.getImageURL())
                        .fitCenter()
                        .into(albumImage);


                // Using glide library to load artist image from URL on artistImage ImageView
                Glide.with(getApplicationContext())
                        .load(album.getArtistImageURL().isEmpty() ? R.drawable.artist_placeholder : album.getArtistImageURL())
                        .fitCenter()
                        .into(artistImage);
            }

//            albumDetailsViewModel.getAlbum().removeObservers(AlbumDetails.this);

        };

        // getting the data for the specific album given the album's id -> searchable.getMbid()
        albumDetailsViewModel.getAlbums(searchable.getMbid()).observe(this, observer);

        // listener when add favorite is tapped
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the HashSet from shared prefs
                Set<String> set = new HashSet<>(sharedPref.getStringSet("albums", new HashSet<>()));
                // adding the new album's name
                set.add(albumInfo.getName());
                Toast.makeText(getApplicationContext(), "Save: " + albumInfo.getName()  , Toast.LENGTH_SHORT).show();

                // set the update Set in shared prefs (local storing data)
                editor.putStringSet("albums", set).apply();
            }
        });

    }

    /**
     * method to setup recyclerview with album's tracks
     */
    private void setUpRecyclerView(){
        albumTracks = findViewById(R.id.album_tracks);
        albumTracks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        albumTracks.setHasFixedSize(true);
        tracksAdapter = new TracksAdapter(getApplicationContext(), trackList);
        albumTracks.setAdapter(tracksAdapter);
    }

}
