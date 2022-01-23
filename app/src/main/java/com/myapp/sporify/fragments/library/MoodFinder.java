package com.myapp.sporify.fragments.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.R;
import com.myapp.sporify.activities.user.UserViewModel;
import com.myapp.sporify.adapters.LibraryAdapter;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.utils.Type;

import java.util.ArrayList;
import java.util.List;

public class MoodFinder extends AppCompatActivity {

    List<Artist> artists;

    private RecyclerView artistsMood;

    UserViewModel userViewModel;

    String token;

    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_finder);

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");

        artistsMood = findViewById(R.id.artists_mood);
        emptyText = findViewById(R.id.empty_text);
        artists = new ArrayList<>();

        artistsMood.setLayoutManager(new LinearLayoutManager(this));
        artistsMood.setAdapter(new LibraryAdapter<>(this, artists, Type.ARTIST));
        artistsMood.setHasFixedSize(true);


        fetchArtists();
    }

    private void fetchArtists(){
        userViewModel.artists(token);
        Observer<List<Artist>> artistObserver = new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artistList) {
                if(artistList.size() <= 0){
                    emptyText.setVisibility(View.VISIBLE);
                    return;
                }

                emptyText.setVisibility(View.GONE);

                artists = new ArrayList<>(artistList);
                artistsMood.setAdapter(new LibraryAdapter<>(getApplicationContext(), artists, Type.ARTIST));
            }
        };

        userViewModel.getUserTracksLiveData().observe(MoodFinder.this, artistObserver);
    }
}