package com.myapp.sporify.activities.artist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.artist.tabs.FragmentAdapter;
import com.myapp.sporify.adapters.artist.ArtistTracksAdapter;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetails extends AppCompatActivity {

    private TextView artistName, artistGenre, artistBio, seeMore;
    private ImageView artistImage;
    private RecyclerView artistTracks;
    private ImageButton addFavorite;

    private ArtistDetailsViewModel artistDetailsViewModel;
    private FavoriteArtistViewModel favoriteArtistViewModel;

    private List<Track> trackList;
    private ArtistTracksAdapter adapter;

    private boolean bioHidden = false;


    private String token;

    private Artist artistInfo;
    private Searchable searchable;

    // TAB BAR
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        artistDetailsViewModel = new ViewModelProvider(this).get(ArtistDetailsViewModel.class);
        favoriteArtistViewModel = new ViewModelProvider(this).get(FavoriteArtistViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");

        artistName = findViewById(R.id.artist_name);
        artistImage = findViewById(R.id.artist_image);
        artistGenre = findViewById(R.id.artist_genre);
        artistBio = findViewById(R.id.artist_bio);
        seeMore = findViewById(R.id.see_more);
        artistTracks = findViewById(R.id.artist_tracks);
        addFavorite = findViewById(R.id.add_favorite);

        // tab bar views
        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        Intent intent = getIntent();
        searchable = new Searchable();
        if(intent.getSerializableExtra("item") != null){
            searchable = (Searchable) intent.getSerializableExtra("item");
        }

        artistInfo = new Artist();

        artistDetailsViewModel.init(searchable.getMbid());

        trackList = new ArrayList<>();

        artistTracks.setLayoutManager(new LinearLayoutManager(this));
        artistTracks.setHasFixedSize(true);
        adapter = new ArtistTracksAdapter(this,trackList);
        artistTracks.setAdapter(adapter);

        // get artist info
        getArtist();
        getArtistTracks();


        // hide/show bio
        manageBio();


        //  setup tab bar layout
        tabBarSetup(searchable.getMbid(), searchable.getExtraId());


        // add favorite listener
        addFavorite();
    }

    private void getArtist() {

        Observer<Artist> observer = artist -> {
            if(artist != null){
                artistInfo = artist;
                artistName.setText(artist.getName());
                artistGenre.setText(artist.getGenre());
                artistBio.setText(artist.getBiography());

                Glide.with(getApplicationContext())
                        .load(artist.getImageURL())
                        .placeholder(R.drawable.sample_banner_template)
                        .fitCenter()
                        .into(artistImage);
            }

            artistDetailsViewModel.getArtist().removeObservers(ArtistDetails.this);

        };

        artistDetailsViewModel.getArtist().observe(this, observer);
    }

    private void getArtistTracks(){
        Observer<List<Track>> observer2 = tracks -> {
            if(tracks.size() > 0){
                trackList = new ArrayList<>(tracks);
                artistTracks.setAdapter(new ArtistTracksAdapter(getApplicationContext(), trackList));
                adapter.notifyItemInserted(trackList.size() -1);
            }

            artistDetailsViewModel.getArtistTracks().removeObservers(ArtistDetails.this);

        };

        artistDetailsViewModel.getArtistTracks().observe(this, observer2);
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

                        favoriteArtistViewModel.getFavoriteArtistsResponse().removeObservers(ArtistDetails.this);
                    }
                };

                favoriteArtistViewModel.init(searchable.getMbid(),searchable.getImageURL(), token, artistInfo);

                favoriteArtistViewModel.getFavoriteArtistsResponse().observe(ArtistDetails.this, observer1);
            }
        });
    }

    private void manageBio(){
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

    }

    private void tabBarSetup(String mbid, String extraId){
        FragmentManager fm = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle(), mbid, extraId);
        pager2.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Albums"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

}}