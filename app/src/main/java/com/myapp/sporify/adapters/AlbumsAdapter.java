package com.myapp.sporify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.album.AlbumDetails;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;

import java.util.List;

/**
 * Albums adapter for showing albums in recyclerview
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumsAdapter(Context context, List<Album> albums){
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // setting the album item layout
        View view = LayoutInflater.from(context).inflate(R.layout.album_item , parent , false);
        return new AlbumsAdapter.AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        // getting the album item in list at this position
        Album album = albums.get(position);
        // setting the name of album in textview
        holder.name.setText(album.getName());

        // setting the name of artist in textview
        holder.artistName.setText(album.getArtistName());

        // if not empty then load image from URL
        // else load a placeholder image
        if(!album.getImageURL().isEmpty()){
            Glide.with(context)
                    .load(album.getImageURL())
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

        // Click listener when album is tapped
        holder.albumItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize a searchable item
                Searchable searchable =
                        new Searchable(album.getMbid(),album.getName(), album.getArtistName(),album.getImageURL(), Type.ALBUM );
                Intent intent = new Intent();

                // pass the searchable item to AlbumDetails activity
                intent.putExtra("item", searchable);
                intent.setClass(context, AlbumDetails.class);

                // start the AlbumDetails activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder{

        LinearLayout albumItem;
        TextView  name, artistName;
        ImageView image;

        // initialize the Views from album_item layout
        public AlbumHolder(@NonNull View itemView) {
            super(itemView);

            albumItem = itemView.findViewById(R.id.album_item);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            image = itemView.findViewById(R.id.image);

        }
    }
}

