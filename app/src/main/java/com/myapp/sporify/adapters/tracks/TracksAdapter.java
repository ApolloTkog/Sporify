package com.myapp.sporify.adapters.tracks;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.sporify.R;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.Helper;

import java.util.List;

/**
 * Tracks adapter for showing album's tracks in recycler view inside AlbumDetailsActivity
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TrackHolder> {

    private Context context;
    private List<Track> tracks;

    public TracksAdapter(Context context, List<Track> tracks){
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.track_item, parent , false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHolder holder, int position) {
        // get track in that position from tracks list
        Track track = tracks.get(position);
        // setting up the text in textviews
        holder.name.setText(track.getName());
        holder.artistName.setText(track.getArtistName());
        holder.duration.setText(Helper.durationConverter(track.getDuration()));

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class TrackHolder extends RecyclerView.ViewHolder{

        TextView mbid, name, artistName,duration, imageURL;
        ImageView image;

        // initialize and find View from track_item layout
        public TrackHolder(@NonNull View itemView) {
            super(itemView);

            mbid = itemView.findViewById(R.id.mbid);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            duration = itemView.findViewById(R.id.track_duration);
            image = itemView.findViewById(R.id.image);
            imageURL = itemView.findViewById(R.id.imageURL);

        }
    }
}