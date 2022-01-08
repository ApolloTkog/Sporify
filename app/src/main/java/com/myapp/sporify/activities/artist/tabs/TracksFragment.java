package com.myapp.sporify.activities.artist.tabs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.sporify.activities.artist.ArtistDetailsViewModel;
import com.myapp.sporify.adapters.artists.ArtistTracksAdapter;
import com.myapp.sporify.databinding.TracksFragmentBinding;
import com.myapp.sporify.models.Track;

import java.util.ArrayList;
import java.util.List;

public class TracksFragment extends Fragment {

    private ArtistDetailsViewModel mViewModel;
    private TracksFragmentBinding binding;

    private RecyclerView artistTracks;
    private List<Track> trackList;

    private ArtistTracksAdapter adapter;



    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        binding = TracksFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        artistTracks = binding.artistTracks;

        trackList = new ArrayList<>();

        artistTracks.setLayoutManager(new LinearLayoutManager(requireContext()));
        artistTracks.setHasFixedSize(true);
        adapter = new ArtistTracksAdapter(requireContext(),trackList);
        artistTracks.setAdapter(adapter);

        String mbid = "";
        if (getArguments() != null) {
            mbid = getArguments().getString("mbid");
        }

        mViewModel.init(mbid);

        Observer<List<Track>> observer2 = new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                if(tracks.size() > 0){
                    trackList = new ArrayList<>(tracks);
                    artistTracks.setAdapter(new ArtistTracksAdapter(requireContext(), trackList));
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


    @Override
    public void onResume() {
        super.onResume();

        if(binding != null)
            binding.getRoot().requestLayout();
    }
}