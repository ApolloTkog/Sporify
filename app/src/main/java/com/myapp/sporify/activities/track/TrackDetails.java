package com.myapp.sporify.activities.track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;

public class TrackDetails extends AppCompatActivity {

    TextView trackName, artistName, trackContent, trackSummary;
    ImageView trackImage, artistImage;
    private TrackDetailsViewModel trackDetailsViewModel;

    private Track trackInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);

        trackDetailsViewModel = new ViewModelProvider(this).get(TrackDetailsViewModel.class);


        trackName = findViewById(R.id.track_name);
        artistName = findViewById(R.id.track_artist);
        trackImage = findViewById(R.id.track_image);
        artistImage = findViewById(R.id.artist_image);

        trackContent = findViewById(R.id.track_content);
        trackSummary = findViewById(R.id.track_summary);

        Intent intent = getIntent();
        Searchable searchable = new Searchable();

        if(intent.getSerializableExtra("item") != null){
            searchable = (Searchable) intent.getSerializableExtra("item");
        }

        trackInfo = new Track();

        Observer<Track> observer = track -> {
            if(track != null){
                trackInfo = track;
                trackName.setText(track.getName());
                artistName.setText(track.getArtistName());
                trackContent.setText(track.getContent());
                trackSummary.setText(track.getSummary());

                Glide.with(getApplicationContext())
                        .load(track.getImageURL().isEmpty() ? R.drawable.artist_placeholder : track.getImageURL())
                        .fitCenter()
                        .into(trackImage);


                Glide.with(getApplicationContext())
                        .load(track.getArtistImageURL().isEmpty() ? R.drawable.artist_placeholder : track.getArtistImageURL())
                        .fitCenter()
                        .into(artistImage);
            }

//            trackDetailsViewModel.getTrack().removeObservers(TrackDetails.this);

        };

        trackDetailsViewModel.getTrackInfo(searchable.getMbid()).observe(this, observer);

    }
}
