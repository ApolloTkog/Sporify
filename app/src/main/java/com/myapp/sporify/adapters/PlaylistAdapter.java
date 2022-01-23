package com.myapp.sporify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.R;
import com.myapp.sporify.activities.track.PlaylistDialog;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {

    private Context context;
    private List<Playlist> playlists;

    private LibraryViewModel libraryViewModel;

    private PlaylistDialog playlistDialog;


    Track track;
    String mbid, accessToken;

    boolean showedUp = false;

    public PlaylistAdapter(Context context, List<Playlist> playlists, String mbid, String accessToken, Track track, PlaylistDialog playlistDialog){
        this.context = context;
        this.playlists = playlists;
        this.track = track;
        this.accessToken = accessToken;
        this.mbid = mbid;

        this.playlistDialog = playlistDialog;

        libraryViewModel = new LibraryViewModel();
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set the favorite item layout
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item, parent , false);
        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.name.setText(playlist.getName());
        holder.artistName.setText(playlist.getDescription());

        holder.playlistItem.setOnClickListener(view -> {
            libraryViewModel.send(playlist.getId(), mbid, accessToken, track);


            Observer<String> observer = new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if(s != null && !showedUp){
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        if(playlistDialog != null){
                            playlistDialog.dismiss();
                        }
                        showedUp = true;
                    }
                }
            };

            libraryViewModel.getPlaylistTrackResponse().observeForever(observer);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistHolder extends RecyclerView.ViewHolder{

        LinearLayout playlistItem;
        TextView name, artistName;
        ImageView image;

        // initialize the Views from album_item layout
        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);

            playlistItem = itemView.findViewById(R.id.playlist_item);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            image = itemView.findViewById(R.id.image);
        }
    }
}
