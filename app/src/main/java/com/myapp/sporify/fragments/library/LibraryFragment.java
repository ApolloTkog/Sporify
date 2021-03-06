package com.myapp.sporify.fragments.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.R;
import com.myapp.sporify.activities.user.User;
import com.myapp.sporify.activities.user.UserViewModel;
import com.myapp.sporify.adapters.LibraryAdapter;
import com.myapp.sporify.databinding.FragmentLibraryBinding;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.models.UserModel;
import com.myapp.sporify.utils.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibraryFragment extends Fragment {

    private LibraryViewModel libraryViewModel;
    private UserViewModel userViewModel;

    private FragmentLibraryBinding binding;

    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;
    private List<Playlist> playlistList;

    private LinearLayout albumFilter, artistFilter, trackFilter, playlistFilter;
    private TextView albumText, artistText, trackText, playlistText;
    private Type filterSelected;

    private RecyclerView albumFavorites, artistFavorites, trackFavorites, playlists;

    private String token, mood = "";

    private ImageButton addPlaylist, findMood;
    private EditText playlistName;

    private CardView userIcon;

    private LibraryAdapter<Playlist> playlistLibraryAdapter;

    Observer<List<Playlist>> playlistObserver;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        libraryViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);
        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");


        // Favorites Filters
        // filters
        albumFilter = binding.albumFilter;
        artistFilter = binding.artistFilter;
        trackFilter = binding.trackFilter;
        playlistFilter = binding.playlistFilter;

        albumText = binding.albumText;
        artistText = binding.artistText;
        trackText = binding.trackText;
        playlistText = binding.playlistText;

        userIcon = binding.userIcon;
        addPlaylist = binding.addPlaylist;
        findMood = binding.findByMood;

        setUpFilters();

        setupRecyclers();

        albums = new ArrayList<>();
        artists = new ArrayList<>();
        tracks = new ArrayList<>();
        playlistList = new ArrayList<>();

        albumFavorites.setAdapter(new LibraryAdapter<>(requireContext(), albums, Type.ALBUM));
        artistFavorites.setAdapter(new LibraryAdapter<>(requireContext(), artists, Type.ARTIST));
        trackFavorites.setAdapter(new LibraryAdapter<>(requireContext(), tracks, Type.TRACK));

        playlistLibraryAdapter = new LibraryAdapter<>(requireContext(), playlistList, Type.PLAYLIST);
        playlistLibraryAdapter.setToken(token);
        playlistLibraryAdapter.setLifecycleOwner(getViewLifecycleOwner());
        playlists.setAdapter(playlistLibraryAdapter);


        // fetch user data from db
        fetchUserData();

        Observer<List<Album>> albumObserver = new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albumList) {
                if(albumList.size() <= 0){
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                albums = new ArrayList<>(albumList);
                albumFavorites.setAdapter(new LibraryAdapter<>(requireContext(), albums,Type.ALBUM));
            }
        };

        libraryViewModel.init(token);
        libraryViewModel.getFavoriteAlbums().observe(getViewLifecycleOwner(), albumObserver);


        Observer<List<Artist>> artistObserver = new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artistList) {
                if(artistList.size() <= 0){
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                artists = new ArrayList<>(artistList);
                artistFavorites.setAdapter(new LibraryAdapter<>(requireContext(), artists,Type.ARTIST));
            }
        };

        libraryViewModel.getFavoriteArtists().observe(getViewLifecycleOwner(), artistObserver);

        Observer<List<Track>> trackObserver = new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> trackList) {
                if(trackList.size() <= 0){
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                tracks = new ArrayList<>(trackList);
                trackFavorites.setAdapter(new LibraryAdapter<>(requireContext(), tracks,Type.TRACK));
            }
        };

        libraryViewModel.getFavoriteTracks().observe(getViewLifecycleOwner(), trackObserver);

        playlistObserver = new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlistsData) {
                if(playlistsData.size() <= 0){
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                playlistList = new ArrayList<>(playlistsData);

                playlistLibraryAdapter.setItems(playlistList);
                playlistLibraryAdapter.notifyDataSetChanged();
//                playlists.setAdapter(new LibraryAdapter<>(requireContext(), playlistList, Type.PLAYLIST));
//                playlists.setAdapter(playlistLibraryAdapter);

                libraryViewModel.getPlaylists().removeObservers(getViewLifecycleOwner());
            }
        };

        libraryViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlistObserver);


        addPlaylist.setOnClickListener(view -> {
            createPlaylistDialog();
        });


        userIcon.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), User.class));
        });

        findMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), MoodFinder.class);
                startActivity(intent);
            }
        });


        return root;
    }


    private void setUpFilters(){
        filterSelected = Type.ALBUM;
        albumText.setTextColor(Color.WHITE);

        albumFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSelected = Type.ALBUM;
                albumFilter.setBackgroundResource(R.drawable.filter_selected_item);
                artistFilter.setBackgroundResource(R.drawable.filter_item);
                trackFilter.setBackgroundResource(R.drawable.filter_item);
                playlistFilter.setBackgroundResource(R.drawable.filter_item);

                albumText.setTextColor(Color.WHITE);
                artistText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.GRAY);
                playlistText.setTextColor(Color.GRAY);

                libraryViewModel.init(token);
                // hide recyclers
                artistFavorites.setVisibility(View.GONE);
                trackFavorites.setVisibility(View.GONE);
                playlists.setVisibility(View.GONE);

                findMood.setVisibility(View.GONE);


                // show
                albumFavorites.setVisibility(View.VISIBLE);
            }
        });

        artistFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSelected = Type.ARTIST;
                albumFilter.setBackgroundResource(R.drawable.filter_item);
                artistFilter.setBackgroundResource(R.drawable.filter_selected_item);
                trackFilter.setBackgroundResource(R.drawable.filter_item);
                playlistFilter.setBackgroundResource(R.drawable.filter_item);

                artistText.setTextColor(Color.WHITE);
                albumText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.GRAY);
                playlistText.setTextColor(Color.GRAY);

                // hide recyclers
                albumFavorites.setVisibility(View.GONE);
                trackFavorites.setVisibility(View.GONE);
                playlists.setVisibility(View.GONE);


                findMood.setVisibility(View.VISIBLE);


                // show
                artistFavorites.setVisibility(View.VISIBLE);
            }
        });

        trackFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSelected = Type.TRACK;
                albumFilter.setBackgroundResource(R.drawable.filter_item);
                artistFilter.setBackgroundResource(R.drawable.filter_item);
                trackFilter.setBackgroundResource(R.drawable.filter_selected_item);
                playlistFilter.setBackgroundResource(R.drawable.filter_item);

                artistText.setTextColor(Color.GRAY);
                albumText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.WHITE);
                playlistText.setTextColor(Color.GRAY);

                // hide recyclers
                albumFavorites.setVisibility(View.GONE);
                artistFavorites.setVisibility(View.GONE);
                playlists.setVisibility(View.GONE);

                findMood.setVisibility(View.GONE);


                // show
                trackFavorites.setVisibility(View.VISIBLE);
            }
        });

        playlistFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                filterSelected = Type.TRACK;
                albumFilter.setBackgroundResource(R.drawable.filter_item);
                artistFilter.setBackgroundResource(R.drawable.filter_item);
                trackFilter.setBackgroundResource(R.drawable.filter_item);
                playlistFilter.setBackgroundResource(R.drawable.filter_selected_item);

                artistText.setTextColor(Color.GRAY);
                albumText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.GRAY);
                playlistText.setTextColor(Color.WHITE);

                // hide recyclers
                albumFavorites.setVisibility(View.GONE);
                artistFavorites.setVisibility(View.GONE);
                trackFavorites.setVisibility(View.GONE);

                findMood.setVisibility(View.GONE);

                // show
                playlists.setVisibility(View.VISIBLE);

            }
        });

    }

    private void setupRecyclers(){
        albumFavorites = binding.albumFavorites;
        artistFavorites =  binding.artistFavorites;
        trackFavorites = binding.trackFavorites;
        playlists = binding.playlists;

        albumFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        albumFavorites.setHasFixedSize(true);

        artistFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        artistFavorites.setHasFixedSize(true);

        trackFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        trackFavorites.setHasFixedSize(true);

        playlists.setLayoutManager(new LinearLayoutManager(requireContext()));
        playlists.setHasFixedSize(true);
    }

    // create dialog when the user taps "+" to add a new playlists
    private void createPlaylistDialog() {
        View v = LayoutInflater.from(requireContext()).inflate(R.layout.playlist_dialog, null);
        EditText playlistName = v.findViewById(R.id.playlist_name);
        Button createPlaylist = v.findViewById(R.id.create_playlist);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // playlist_dialog.xml file as main view
        builder.setView(v);

        AtomicBoolean showed = new AtomicBoolean(false);
        AlertDialog alert = builder.create();

        // when user taps create button
        createPlaylist.setOnClickListener(mV -> {

            String playlistText = playlistName.getText().toString();

            // create playlist with the text that user enters
            libraryViewModel.create(token, playlistText);
            libraryViewModel.getPlaylistCreateResponse().observe(getViewLifecycleOwner(), s -> {
                if(s != null){
                    if(!showed.get()){
                        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
                        showed.set(true);
                    }

                    // re fetch playlists
                    libraryViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlistObserver);
                }
            });

            alert.dismiss();
        });

        alert.show();
    }

    private List<Artist> findArtistsByMood (){
        List<Artist> artistsByMood = new ArrayList<>();

        for(Artist x: artists){
            if(x.getMood().toLowerCase(Locale.ROOT).equals(mood)){
                artistsByMood.add(x);
            }
        }

        return  artistsByMood;
    }

    public void fetchUserData(){
        userViewModel.init(token);

        Observer<UserModel> getUserData = user -> {
            if(user == null)
                return;

            mood = user.getMood();

            userViewModel.getUserData().removeObservers(getViewLifecycleOwner());
        };

        userViewModel.getUserData().observe(getViewLifecycleOwner(), getUserData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(token != null){
            libraryViewModel.init(token);
            libraryViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlistObserver);

        }
    }
}