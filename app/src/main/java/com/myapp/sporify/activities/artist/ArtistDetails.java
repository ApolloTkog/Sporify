package com.myapp.sporify.activities.artist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.adapters.ArtistTracksAdapter;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetails extends AppCompatActivity {

    private TextView artistName, artistGenre, artistBio, seeMore;
    private ImageView artistImage;
    private RecyclerView artistTracks;

    private ArtistDetailsViewModel artistDetailsViewModel;

    private List<Track> trackList;
    private ArtistTracksAdapter adapter;

    private boolean bioHidden = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        artistDetailsViewModel = new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        artistName = findViewById(R.id.artist_name);
        artistImage = findViewById(R.id.artist_image);
        artistGenre = findViewById(R.id.artist_genre);
        artistBio = findViewById(R.id.artist_bio);
        seeMore = findViewById(R.id.see_more);
        artistTracks = findViewById(R.id.artist_tracks);

        Intent intent = getIntent();
        Searchable searchable = new Searchable(mbid, name, artistName, image, "album");

        if(intent.getSerializableExtra("item") != null){
            searchable = (Searchable) intent.getSerializableExtra("item");
        }

        artistDetailsViewModel.init(searchable.getMbid());

        trackList = new ArrayList<>();

        artistTracks.setLayoutManager(new LinearLayoutManager(this));
        artistTracks.setHasFixedSize(true);
        adapter = new ArtistTracksAdapter(this,trackList);
        artistTracks.setAdapter(adapter);

        Observer<Artist> observer = artist -> {
            if(artist != null){
//              albumInfo = album;
//                trackList = new ArrayList<>(album.getTrackList());
//                albumTracks.setAdapter(new TracksAdapter(getApplicationContext(), trackList));
                artistName.setText(artist.getName());
                artistGenre.setText(artist.getGenre());
                artistBio.setText(artist.getBiography());
//                albumArtist.setText(album.getArtistName());
//                tracksAdapter.notifyItemInserted(trackList.size() -1);

                Glide.with(getApplicationContext())
                        .load(artist.getImageURL())
                        .fitCenter()
                        .into(artistImage);
            }

            artistDetailsViewModel.getArtist().removeObservers(ArtistDetails.this);

        };


        artistDetailsViewModel.getArtist().observe(this, observer);

        Observer<List<Track>> observer2 = new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                if(tracks.size() > 0){
                    trackList = new ArrayList<>(tracks);
                    artistTracks.setAdapter(new ArtistTracksAdapter(getApplicationContext(), trackList));
                    adapter.notifyItemInserted(trackList.size() -1);
                }

                artistDetailsViewModel.getArtistTracks().removeObservers(ArtistDetails.this);

            }
        };

        artistDetailsViewModel.getArtistTracks().observe(this, observer2);


        seeMore.setOnClickListener(view -> {
            seeMore.setText(bioHidden ? "read more" : "read less");
            artistBio.setMaxLines(bioHidden ? 8 : Integer.MAX_VALUE);
            bioHidden = !bioHidden;
        });

        View.OnClickListener hideBio = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeMore.setText(bioHidden ? "read more" : "read less");
                artistBio.setMaxLines(bioHidden ? 4 : Integer.MAX_VALUE);
                bioHidden = !bioHidden;
            }
        };

        seeMore.setOnClickListener(hideBio);
        artistBio.setOnClickListener(hideBio);


//        TAB BAR LAYOUT THINGS

    }

}