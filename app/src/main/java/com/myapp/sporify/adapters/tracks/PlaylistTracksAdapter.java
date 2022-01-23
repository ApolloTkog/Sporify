package com.myapp.sporify.adapters.tracks;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Track;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tracks adapter for showing playlist's tracks in recycler view inside PlaylistTracks activity
 */
public class PlaylistTracksAdapter extends RecyclerView.Adapter<PlaylistTracksAdapter.TrackHolder> {

    private Context context;
    private List<Track> tracks;

    private LibraryViewModel libraryViewModel;

    String token, playlistId;

    LifecycleOwner lifecycleOwner;

    public PlaylistTracksAdapter(Context context, List<Track> tracks){
        this.context = context;
        this.tracks = tracks;

        libraryViewModel = new LibraryViewModel();
    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_track_item, parent , false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHolder holder, int position) {
        // get track in that position from tracks list
        Track track = tracks.get(position);
        // setting up the text in textviews
        holder.name.setText(track.getName());

        holder.artistName.setText(track.getArtistName());
        holder.youtubeUrl.setText(!track.getYoutubeURL().equals("null") ? track.getYoutubeURL() : "");

        // if not empty then load image from URL
        // else load a placeholder image (default image for tracks without image)
        if(!track.getImageURL().isEmpty()){
            Glide.with(context)
                    .load(track.getImageURL())
                    .fitCenter()
                    .into(holder.image);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }

        // click when you long click a track
        holder.trackItem.setOnLongClickListener(view -> {
            AtomicBoolean showed = new AtomicBoolean(false);

            // delete tack from api
            libraryViewModel.deleteTrack(token,playlistId, track.getId());
            libraryViewModel.getPlaylistTrackDeleteResponse().observe(lifecycleOwner, s -> {
                if(s != null && !showed.get()){
                    showed.set(true);
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

                    // delete track by removing it from the ui
                    int actualPosition = holder.getAdapterPosition();
                    tracks.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition, tracks.size());
                }

            });


            return true;
        });


        // click listener when you tap on track item
        holder.trackItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open track's youtube URL
                if(!track.getYoutubeURL().isEmpty() && !track.getYoutubeURL().equals("null")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(track.getYoutubeURL()));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class TrackHolder extends RecyclerView.ViewHolder{

        LinearLayout trackItem;
        TextView mbid, name, artistName, youtubeUrl, imageURL;
        ImageView image;

        // initialize and find View from track_item layout
        public TrackHolder(@NonNull View itemView) {
            super(itemView);

            trackItem = itemView.findViewById(R.id.track_item);
            mbid = itemView.findViewById(R.id.mbid);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            youtubeUrl = itemView.findViewById(R.id.youtube_url);
            image = itemView.findViewById(R.id.image);
            imageURL = itemView.findViewById(R.id.imageURL);

        }
    }
}
