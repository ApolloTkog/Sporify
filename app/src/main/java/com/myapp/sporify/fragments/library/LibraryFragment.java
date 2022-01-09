package com.myapp.sporify.fragments.library;

import android.app.AlertDialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.R;
import com.myapp.sporify.adapters.LibraryAdapter;
import com.myapp.sporify.databinding.FragmentLibraryBinding;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
//import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibraryFragment extends Fragment {

    private LibraryViewModel libraryViewModel;
    private FragmentLibraryBinding binding;

    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;

    private LinearLayout albumFilter, artistFilter, trackFilter, playlistFilter;
    private TextView albumText, artistText, trackText, playlistText;
    private Type filterSelected;

    private RecyclerView albumFavorites, artistFavorites, trackFavorites, playlists;

    private String token;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        libraryViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);

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


        setUpFilters();

        setupRecyclers();

        albums = new ArrayList<>();
        artists = new ArrayList<>();
        tracks = new ArrayList<>();

        albumFavorites.setAdapter(new LibraryAdapter<>(requireContext(), albums, Type.ALBUM));
        artistFavorites.setAdapter(new LibraryAdapter<>(requireContext(), artists, Type.ARTIST));
        trackFavorites.setAdapter(new LibraryAdapter<>(requireContext(), tracks, Type.TRACK));

        // playlist adapter

        Observer<List<Album>> albumObserver = new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albumList) {
                if (albumList.size() <= 0) {
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                albums = new ArrayList<>(albumList);
                albumFavorites.setAdapter(new LibraryAdapter<>(requireContext(), albums, Type.ALBUM));
            }
        };

        libraryViewModel.init(token);
        libraryViewModel.getFavoriteAlbums().observe(getViewLifecycleOwner(), albumObserver);


        Observer<List<Artist>> artistObserver = new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artistList) {
                if (artistList.size() <= 0) {
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                artists = new ArrayList<>(artistList);
                artistFavorites.setAdapter(new LibraryAdapter<>(requireContext(), artists, Type.ARTIST));
            }
        };

        libraryViewModel.getFavoriteArtists().observe(getViewLifecycleOwner(), artistObserver);

        Observer<List<Track>> trackObserver = new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> trackList) {
                if (trackList.size() <= 0) {
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                tracks = new ArrayList<>(trackList);
                trackFavorites.setAdapter(new LibraryAdapter<>(requireContext(), tracks, Type.TRACK));
            }
        };

        libraryViewModel.getFavoriteTracks().observe(getViewLifecycleOwner(), trackObserver);


        //addPlaylist.setOnClickListener(view -> {
        // createPlaylistDialog();
        //});

        // return root;
        // }


        private void setUpFilters() {
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

                    // show
                    playlists.setVisibility(View.VISIBLE);

                }
            });

        }

        private void setupRecyclers() {
            albumFavorites = binding.albumFavorites;
            artistFavorites = binding.artistFavorites;
            trackFavorites = binding.trackFavorites;

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

    }
}