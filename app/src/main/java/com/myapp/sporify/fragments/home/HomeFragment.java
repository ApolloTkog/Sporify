package com.myapp.sporify.fragments.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.activities.user.UserViewModel;
import com.myapp.sporify.adapters.albums.AlbumsAdapter;
import com.myapp.sporify.adapters.artist.ArtistsAdapter;
import com.myapp.sporify.adapters.tracks.TopTracksAdapter;
import com.myapp.sporify.databinding.FragmentHomeBinding;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private TopAlbumsViewModel topAlbumsViewModel;
    private TopArtistsViewModel topArtistsViewModel;
    private TopTracksViewModel topTracksViewModel;
    private FragmentHomeBinding binding;

    UserViewModel userViewModel;

    String token;

    private TextView username, albumsHeader;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        topAlbumsViewModel =
                new ViewModelProvider(this).get(TopAlbumsViewModel.class);

        topArtistsViewModel =
                new ViewModelProvider(this).get(TopArtistsViewModel.class);

        topTracksViewModel =
                new ViewModelProvider(this).get(TopTracksViewModel.class);

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        username = binding.usernameText;
        albumsHeader = binding.albumsHeader;

        fetchUserData();

        final RecyclerView artistsList = binding.artistList;
        artistsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        artistsList.setLayoutManager(layoutManager);
        artistsList.setAdapter(new ArtistsAdapter(requireContext(), new ArrayList<>()));


        topArtistsViewModel.getTopArtists(20).observe(getViewLifecycleOwner(), new Observer<List<Artist>>() {
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


        topAlbumsViewModel.getTopAlbums(token, 20).observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
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


        topTracksViewModel.getTopTracks(20).observe(getViewLifecycleOwner(), new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
//                Toast.makeText(requireContext(), "Hi!", Toast.LENGTH_SHORT).show();
                tracksList.setAdapter(new TopTracksAdapter(requireContext(), tracks));
            }
        });


        return root;
    }

    public void fetchUserData(){
        userViewModel.init(token);

        Observer<UserModel> getUserData = user -> {
            if(user == null)
                return;

            username.setText("Hey " + user.getUsername());
            albumsHeader.setText("Top " + user.getGenre() + " Albums");


            userViewModel.getUserData().removeObservers(getViewLifecycleOwner());
        };

        userViewModel.getUserData().observe(getViewLifecycleOwner(), getUserData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}