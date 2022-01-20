package com.myapp.sporify.fragments.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.myapp.sporify.R;
import com.myapp.sporify.adapters.tracks.PlaylistTracksAdapter;
import com.myapp.sporify.adapters.tracks.TracksAdapter;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTracks extends AppCompatActivity {


    private RecyclerView playlistTracks;
    private TextView playlistText;

    private String playlistId = "", playlistName = "Playlist", token;

    private PlaylistTracksAdapter playlistTracksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_tracks);

        SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");


        playlistTracks = findViewById(R.id.playlist_tracks);
        playlistText = findViewById(R.id.playlist_title);

        Intent intent = getIntent();
        List<Track> trackList = new ArrayList<>();

        // getting the tracks from the previous screen
        if(intent.getSerializableExtra("tracks") != null){
            trackList = (List<Track>) intent.getSerializableExtra("tracks");
            playlistId = intent.getStringExtra("playlistId");
            playlistName = intent.getStringExtra("playlistName");
        }

        // set playlist name on title
        playlistText.setText(playlistName);


        // setup the recycler view to show playlist's tracks
        playlistTracksAdapter = new PlaylistTracksAdapter(this, trackList);
        playlistTracksAdapter.setPlaylistId(playlistId);
        playlistTracksAdapter.setLifecycleOwner(this);
        playlistTracksAdapter.setToken(token);


        playlistTracks.setLayoutManager(new LinearLayoutManager(this));
        playlistTracks.setHasFixedSize(true);
        playlistTracks.setAdapter(playlistTracksAdapter);



    }
}