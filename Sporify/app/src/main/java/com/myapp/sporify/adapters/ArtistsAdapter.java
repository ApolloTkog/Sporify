package com.myapp.sporify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sporify.R;
import com.example.sporify.models.Artist;

import java.util.List;

/**
 * Albums adapter for showing albums in recyclerview in HomeFragment
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistHolder> {

    private Context context;
    private List<Artist> artists;


    public ArtistsAdapter(Context context, List<Artist> artists){
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_item , parent , false);
        return new ArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder holder, int position) {
        // getting artist in position from artists list
        Artist artist = artists.get(position);

        // setting the name of artist in textview name
        holder.name.setText(artist.getName());

        // if not empty then load image from URL
        // else load a placeholder image if there is no image
        if(!artist.getImageURL().isEmpty()){
            Glide.with(context)
                    .load(artist.getImageURL())
                    .placeholder(R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ArtistHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView image;

        // initialize artist's fields name and image
        public ArtistHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

        }
    }
}
