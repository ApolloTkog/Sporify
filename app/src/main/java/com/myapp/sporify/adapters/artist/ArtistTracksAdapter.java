package com.myapp.sporify.adapters.artist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.track.PlaylistDialog;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;

import java.util.List;

/**
 * ArtistTracks adapter for showing artist's tracks in recycler view inside ArtistDetails activity
 */
public class ArtistTracksAdapter extends RecyclerView.Adapter<ArtistTracksAdapter.ArtistTrackHolder> {

    private Context context;
    private List<Track> tracks;
    private List<Playlist> playlists;

    private  String token;

    private FragmentManager fragmentManager;

    public ArtistTracksAdapter(Context context, List<Track> tracks, String token, List<Playlist> playlists){
        this.context = context;
        this.tracks = tracks;
        this.token = token;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ArtistTrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_track_item, parent , false);
        return new ArtistTrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistTrackHolder holder, int position) {
        // get the track in that position from tracks list
        Track track = tracks.get(position);

        // setting the text on textviews
        holder.rank.setText(String.valueOf(track.getRank()));
        holder.name.setText(track.getName());
        holder.youtubeURL.setText(track.getYoutubeURL());

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

        // click listener when you tap on track item
        holder.track_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open track's youtube URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(track.getYoutubeURL()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });

        // long click listener when you tap long on track item
        holder.track_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                new PlaylistDialog("", token, track, playlists).show(manager, "TAG");

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static class ArtistTrackHolder extends RecyclerView.ViewHolder{

        LinearLayout track_item;
        TextView rank, name, youtubeURL;
        ImageView image;

        // initialize the Views from artist_track_item layout
        public ArtistTrackHolder(@NonNull View itemView) {
            super(itemView);

            track_item = itemView.findViewById(R.id.track_item);
            name = itemView.findViewById(R.id.name);
            rank = itemView.findViewById(R.id.rank);
            youtubeURL = itemView.findViewById(R.id.artist_vid);
            image = itemView.findViewById(R.id.image);

        }
    }
}
