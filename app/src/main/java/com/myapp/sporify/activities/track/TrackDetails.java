package com.myapp.sporify.activities.track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.album.AlbumDetailsViewModel;
import com.myapp.sporify.adapters.TracksAdapter;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;

public class TrackDetails extends AppCompatActivity {

    TextView trackName, artistName, trackContent, trackSummary;
    ImageView trackImage, artistImage;
    private TrackDetailsViewModel trackDetailsViewModel;

    private Track trackInfo;

    //Test Comment!

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
            Log.d("SEARCHABLE", searchable.getMbid());
        }

        trackInfo = new Track();

        Observer<Track> observer = track -> {
            if(track != null){
                trackInfo = track;
                trackName.setText(track.getName());
                artistName.setText(track.getArtistName());

                trackSummary.setMovementMethod(LinkMovementMethod.getInstance());
                trackSummary.setText(Html.fromHtml(track.getSummary()));
                trackContent.setMovementMethod(LinkMovementMethod.getInstance());
                trackContent.setText(Html.fromHtml(track.getContent()));
                // trackContent.setText(track.getContent());


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