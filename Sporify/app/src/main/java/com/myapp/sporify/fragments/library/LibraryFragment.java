package com.myapp.sporify.fragments.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporify.adapters.AlbumsAdapter;
import com.example.sporify.adapters.FavoriteAdapter;
import com.example.sporify.databinding.FragmentLibraryBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LibraryFragment extends Fragment {

    private LibraryViewModel notificationsViewModel;
    private FragmentLibraryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE);

        Set<String> fetch = sharedPref.getStringSet("albums", new HashSet<>());


        final RecyclerView albumFavorites = binding.albumFavorites;
        albumFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        albumFavorites.setHasFixedSize(true);
        albumFavorites.setAdapter(new FavoriteAdapter(requireContext(), new ArrayList<>(fetch)));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}