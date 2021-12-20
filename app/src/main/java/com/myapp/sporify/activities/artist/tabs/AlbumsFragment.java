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
import com.myapp.sporify.adapters.albums.ArtistAlbumsAdapter;
import com.myapp.sporify.databinding.AlbumsFragmentBinding;
import com.myapp.sporify.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment {


    private ArtistDetailsViewModel mViewModel;
    private AlbumsFragmentBinding binding;

    private RecyclerView artistAlbums;
    private List<Album> albumList;

    private ArtistAlbumsAdapter adapter;



    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        binding = AlbumsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        artistAlbums = binding.artistAlbums;

        albumList = new ArrayList<>();

        artistAlbums.setLayoutManager(new LinearLayoutManager(requireContext()));
        artistAlbums.setHasFixedSize(true);
        adapter = new ArtistAlbumsAdapter(requireContext(),albumList);
        artistAlbums.setAdapter(adapter);

        String mbid = "";
        if (getArguments() != null) {
            mbid = getArguments().getString("mbid");
        }

        mViewModel.init(mbid);

        Observer<List<Album>> observer2 = new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> tracks) {
                if(tracks.size() > 0){
                    albumList = new ArrayList<>(tracks);
                    artistAlbums.setAdapter(new ArtistAlbumsAdapter(requireContext(), albumList));
                    adapter.notifyItemInserted(albumList.size() -1);
                }

                mViewModel.getArtistAlbums().removeObservers(AlbumsFragment.this);

            }
        };

        mViewModel.getArtistAlbums().observe(getViewLifecycleOwner(), observer2);

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