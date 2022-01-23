package com.myapp.sporify.activities.artist.tabs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myapp.sporify.activities.artist.ArtistDetailsViewModel;
import com.myapp.sporify.adapters.artist.ArtistTracksAdapter;
import com.myapp.sporify.databinding.TracksFragmentBinding;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;
import java.util.List;

public class TracksFragment extends Fragment {

    private ArtistDetailsViewModel mViewModel;
    private LibraryViewModel libraryViewModel;


    private TracksFragmentBinding binding;

    private RecyclerView artistTracks;
    private List<Track> trackList;
    private List<Playlist> playlists;

    private ArtistTracksAdapter adapter;

    String token, artist = "";

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        libraryViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");

        binding = TracksFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        artistTracks = binding.artistTracks;

        trackList = new ArrayList<>();
        playlists = new ArrayList<>();

        getPlaylists();

        artistTracks.setLayoutManager(new LinearLayoutManager(requireContext()));
        artistTracks.setHasFixedSize(true);
        adapter = new ArtistTracksAdapter(requireContext(),trackList, token, playlists);
        adapter.setFragmentManager(getParentFragmentManager());

        artistTracks.setAdapter(adapter);

        String mbid = "";
        if (getArguments() != null) {
            mbid = getArguments().getString("mbid");
            artist = getArguments().getString("artist");
        }

        mViewModel.init(mbid);

        Observer<List<Track>> observer2 = new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                if(tracks.size() > 0){
                    for(Track x: tracks){
                        x.setArtistName(artist);
                    }
                    trackList = new ArrayList<>(tracks);
                    artistTracks.setAdapter(new ArtistTracksAdapter(requireContext(), trackList, token, playlists));
                    adapter.notifyItemInserted(trackList.size() -1);
                }

                mViewModel.getArtistTracks().removeObservers(TracksFragment.this);

            }
        };

        mViewModel.getArtistTracks().observe(getViewLifecycleOwner(), observer2);

        return  root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // fetch playlists
    private void getPlaylists(){
        libraryViewModel.init(token);
        Observer<List<Playlist>> playlistObserver = new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlistsData) {
                if(playlistsData.size() <= 0){
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show();
                }

                playlists = new ArrayList<>(playlistsData);
            }
        };

        libraryViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlistObserver);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(binding != null)
            binding.getRoot().requestLayout();
    }
}