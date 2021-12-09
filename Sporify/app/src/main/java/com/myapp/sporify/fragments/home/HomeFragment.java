package com.myapp.sporify.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporify.adapters.AlbumsAdapter;
import com.example.sporify.adapters.ArtistsAdapter;
import com.example.sporify.adapters.TopTracksAdapter;
import com.example.sporify.databinding.FragmentHomeBinding;
import com.example.sporify.models.Album;
import com.example.sporify.models.Artist;
import com.example.sporify.models.Track;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final RecyclerView artistsList = binding.artistList;
        artistsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        artistsList.setLayoutManager(layoutManager);
        artistsList.setAdapter(new ArtistsAdapter(requireContext(), new ArrayList<>()));


        homeViewModel.getTopArtists().observe(getViewLifecycleOwner(), new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artists) {
                if (artists.size() > 0){
                    artistsList.setVisibility(View.VISIBLE);
                    artistsList.setAdapter(new ArtistsAdapter(requireContext(), artists));
                }
            }
        });

        final RecyclerView albumsList = binding.albumsList;
        albumsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        albumsList.setLayoutManager(layoutManager2);
        albumsList.setAdapter(new AlbumsAdapter(requireContext(), new ArrayList<>()));


        homeViewModel.getTopAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
//                Toast.makeText(requireContext(), "Hi!", Toast.LENGTH_SHORT).show();
                albumsList.setAdapter(new AlbumsAdapter(requireContext(), albums));
            }
        });


        final RecyclerView tracksList = binding.tracksList;
        tracksList.setHasFixedSize(true);
        tracksList.setLayoutManager(new LinearLayoutManager(requireContext()));
        tracksList.setAdapter(new TopTracksAdapter(requireContext(), new ArrayList<>()));


        homeViewModel.getTopTracks().observe(getViewLifecycleOwner(), new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
//                Toast.makeText(requireContext(), "Hi!", Toast.LENGTH_SHORT).show();
                tracksList.setAdapter(new TopTracksAdapter(requireContext(), tracks));
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}