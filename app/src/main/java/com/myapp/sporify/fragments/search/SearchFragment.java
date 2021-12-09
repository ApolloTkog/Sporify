package com.myapp.sporify.fragments.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.sporify.R;
import com.example.sporify.adapters.SearchAdapter;
import com.example.sporify.databinding.FragmentSearchBinding;
import com.example.sporify.models.Searchable;
import com.example.sporify.utils.Type;
import com.example.sporify.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private static RequestQueue requestQueue;


    private List<Searchable> searchables;

    private SearchAdapter adapter;

    private SearchView search;
    private RecyclerView searchResults;
    private ProgressBar progressBar;
    private TextView emptyText;

    private LinearLayout albumFilter, artistFilter, trackFilter;
    private TextView albumText, artistText, trackText;



    private Type filterSelected;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requestQueue = VolleySingleton.getmInstance(requireContext()).getRequestQueue();


        search = binding.search;
        searchResults = binding.searchResults;
        progressBar = binding.progressCircular;
        emptyText = binding.emptyText;

        // filters
        albumFilter = binding.albumFilter;
        artistFilter = binding.artistFilter;
        trackFilter = binding.trackFilter;
        albumText = binding.albumText;
        artistText = binding.artistText;
        trackText = binding.trackText;

        setUpFilters();

        searchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchResults.setHasFixedSize(true);

        searchables = new ArrayList<>();
        adapter = new SearchAdapter(requireContext(), searchables);
        searchResults.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchables.clear();
                showLoading(true);
                loadResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

    private void loadResults(String title) {
        SearchViewModel viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.init(title, filterSelected);

        Observer<List<Searchable>> observer = new Observer<List<Searchable>>() {
            @Override
            public void onChanged(List<Searchable> albums) {
                if (albums != null) {
                    searchables = new ArrayList<>(albums);
                    searchResults.setAdapter(new SearchAdapter(requireContext(), searchables));
                    emptyText.setVisibility(albums.size() > 0 ? View.GONE : View.VISIBLE);
                    adapter.notifyItemInserted(searchables.size() - 1);
//                    adapter.notifyDataSetChanged();
                } else {
                    //                    showError();
                }
                showLoading(false);
            }
        };

        viewModel.getDataSearch().observe(this, observer);
    }

    private void showLoading(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
                albumText.setTextColor(Color.WHITE);
                artistText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.GRAY);
                search.setQueryHint("Search for albums");
            }
        });

        artistFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSelected = Type.ARTIST;
                albumFilter.setBackgroundResource(R.drawable.filter_item);
                artistFilter.setBackgroundResource(R.drawable.filter_selected_item);
                trackFilter.setBackgroundResource(R.drawable.filter_item);
                artistText.setTextColor(Color.WHITE);
                albumText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.GRAY);
                search.setQueryHint("Search for artists");

            }
        });

        trackFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSelected = Type.TRACK;
                albumFilter.setBackgroundResource(R.drawable.filter_item);
                artistFilter.setBackgroundResource(R.drawable.filter_item);
                trackFilter.setBackgroundResource(R.drawable.filter_selected_item);
                artistText.setTextColor(Color.GRAY);
                albumText.setTextColor(Color.GRAY);
                trackText.setTextColor(Color.WHITE);
                search.setQueryHint("Search for tracks");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}