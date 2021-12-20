package com.myapp.sporify.activities.artist.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    private String mbid, extraId;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String mbid, String extraId) {
        super(fragmentManager, lifecycle);
        this.mbid = mbid;
        this.extraId = extraId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle tracksBundle = new Bundle();
        Bundle albumsBundle = new Bundle();


        tracksBundle.putString("mbid", mbid);
        albumsBundle.putString("mbid", extraId);

        AlbumsFragment albumsFragment = new AlbumsFragment();
        albumsFragment.setArguments(albumsBundle);

        TracksFragment tracksFragment = new TracksFragment();
        tracksFragment.setArguments(tracksBundle);

        if (position == 1) {
            return albumsFragment;
        }

        return tracksFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}