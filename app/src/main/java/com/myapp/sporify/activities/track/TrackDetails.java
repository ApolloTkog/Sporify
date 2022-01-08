package com.myapp.sporify.activities.track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
    private FavoriteTrackViewModel favoriteTrackViewModel;

    private Track trackInfo;

    private ImageButton addFavorite;

    private Searchable searchable;

    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);

        trackDetailsViewModel = new ViewModelProvider(this).get(TrackDetailsViewModel.class);
        favoriteTrackViewModel = new ViewModelProvider(this).get(FavoriteTrackViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");


        trackName = findViewById(R.id.track_name);
        artistName = findViewById(R.id.track_artist);
        trackImage = findViewById(R.id.track_image);
        artistImage = findViewById(R.id.artist_image);

        trackContent = findViewById(R.id.track_content);
        trackSummary = findViewById(R.id.track_summary);

        addFavorite = findViewById(R.id.add_favorite);

        Intent intent = getIntent();
        searchable = new Searchable();

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


        addFavorite();
    }

    private void addFavorite(){
        // listener when add favorite is tapped
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observer<String> observer1 = new Observer<String>() {
                    @Override
                    public void onChanged(String response) {
                        if(response == null)
                            return;

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        favoriteTrackViewModel.getFavoriteTracksResponse().removeObservers(TrackDetails.this);
                    }
                };

                favoriteTrackViewModel.init(searchable.getMbid(), token, trackInfo);

                favoriteTrackViewModel.getFavoriteTracksResponse().observe(TrackDetails.this, observer1);
            }
        });
    }



}