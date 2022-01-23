package com.myapp.sporify.activities.track;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.adapters.PlaylistAdapter;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;

import java.util.List;

/**
 * This is a the popup window when you tap to add a track to a playlist
 */

public class PlaylistDialog extends DialogFragment {

    private RecyclerView mRecyclerView;
    private final List<Playlist> playlists;

    Track track;
    String mbid,token;


    public PlaylistDialog(String mbid, String token, Track track, List<Playlist> playlists){
        this.playlists = playlists;
        this.mbid = mbid;
        this.token = token;
        this.track = track;
    }


    /**
     * Creates the dialog window and adds a recycler view inside to show the available playlists
     * @param savedInstanceState
     * @return the alert dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(requireContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new PlaylistAdapter(requireContext(), playlists, mbid, token, track, this));

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                150);

        mRecyclerView.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        AlertDialog alert = builder.setMessage("Add it to your preferred playlist")
                .setView(mRecyclerView)
                .setPositiveButton("ok", (dialog, which) -> {} )
                .setCancelable(false)
                .create();

        alert.getWindow().setLayout(600, 200);

        return alert ;
    }

    public static String TAG = "PlaylistDialog";
}