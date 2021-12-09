package com.myapp.sporify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporify.R;
import com.example.sporify.models.Track;

import java.util.List;

public class TopTracksAdapter extends RecyclerView.Adapter<TopTracksAdapter.TopTrackHolder> {

    private Context context;
    private List<Track> tracks;

    public TopTracksAdapter(Context context, List<Track> tracks){
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TopTrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_track_item, parent , false);
        return new TopTrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTrackHolder holder, int position) {
        Track track = tracks.get(position);
//        holder.mbid.setText(artist.getMbid());
        holder.rank.setText("#" + track.getRank());
        holder.name.setText(track.getName());
        holder.artistName.setText(track.getArtistName());

//        // if not empty then load image from URL
//        // else load a placeholder image
//        if(!track.getImageURL().isEmpty()){
//            Glide.with(context)
//                    .load(track.getImageURL())
//                    .fitCenter()
//                    .into(holder.image);
//        }
//        else{
//            Glide.with(context)
//                    .load(R.drawable.artist_placeholder)
//                    .fitCenter()
//                    .into(holder.image);
//        }


        if(track.getRank() == 1){
            holder.rank.setTextColor(context.getResources().getColor(R.color.gold));
        }
        else{
            holder.rank.setTextColor(context.getResources().getColor(R.color.darker_gray));
        }

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class TopTrackHolder extends RecyclerView.ViewHolder{

        TextView mbid, rank, name, artistName, imageURL;
        ImageView image;

        public TopTrackHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            mbid = itemView.findViewById(R.id.mbid);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            image = itemView.findViewById(R.id.image);
            imageURL = itemView.findViewById(R.id.imageURL);

        }
    }
}
