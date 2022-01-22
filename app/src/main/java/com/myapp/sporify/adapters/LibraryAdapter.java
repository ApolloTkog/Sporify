package com.myapp.sporify.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.myapp.sporify.activities.album.AlbumDetails;
import com.myapp.sporify.activities.track.TrackDetails;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.fragments.library.PlaylistTracks;
import com.myapp.sporify.interfaces.Item;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.Type;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Favorite adapter for showing favorite albums in recycler view
 */
public class LibraryAdapter<T> extends RecyclerView.Adapter<LibraryAdapter.FavoriteHolder> {

    private Context context;
    private List<T> items;
    private Type type;

    private LibraryViewModel libraryViewModel;

    LifecycleOwner lifecycleOwner;
    String token;

    public LibraryAdapter(Context context, List<T> albums, Type type){
        this.context = context;
        this.items = albums;
        this.type = type;

        libraryViewModel = new LibraryViewModel();
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set the favorite item layout
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent , false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = null;


        if(type == Type.ALBUM)
            item = (Album) items.get(position);
        else if(type == Type.ARTIST)
            item = (Artist) items.get(position);
        else if(type == Type.TRACK)
            item = (Track) items.get(position);
        else
            item = (Playlist) items.get(position);



//        Album album = (Album) albums.get(position);

        // setting to the textview name the album's name
        holder.name.setText(item.getTitle());
        holder.artist.setText(item.getDescription());

        // if not empty then load image from URL
        // else load a placeholder image if there is no image
        if(!item.getImageURL().isEmpty()){
            Glide.with(context)
                    .load(item.getImageURL())
                    .placeholder(R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }
        else{
            Glide.with(context)
                    .load(type == Type.PLAYLIST ? R.drawable.disc_placeholder : R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }



        // if type is playlist
        if(type == Type.PLAYLIST){

            // get the playlist item
            Playlist playlist = (Playlist) items.get(position);

            // when we click a playlist item
            // navigate to playlist's tracks screen
            holder.item.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra("tracks", (Serializable)  playlist.getPlaylistTracks());
                intent.putExtra("playlistId", playlist.getId());
                intent.putExtra("playlistName", playlist.getName());

                intent.setClass(context, PlaylistTracks.class);
                context.startActivity(intent);
            });

            // when we long click playlist item
            // delete playlist from database
            // and then delete it from the ui accordingly
            holder.item.setOnLongClickListener(view -> {

                AtomicBoolean showed = new AtomicBoolean(false);
                libraryViewModel.delete(token, playlist.getId());

                libraryViewModel.getPlaylistDeleteResponse().observe(lifecycleOwner, s -> {
                    if(s != null && !showed.get()){
                        showed.set(true);
                        Log.d("CHECK ANSWER", s);
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        int actualPosition = holder.getAdapterPosition();
                        items.remove(actualPosition);
                        notifyItemRemoved(actualPosition);
                        notifyItemRangeChanged(actualPosition, items.size());
                    }

                });

                return true;
            });
        }
        else{
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetails(position);
                }
            });
        }



    }

    private void openDetails(int position) {
        // getting the kind of searched result
        // could be Album/Artist/Track



        Searchable searchable = new Searchable();
        Intent intent = new Intent();

        Item item = null;

        // if kind is album/artist/track open the corresponding activity
        switch (type){
            case ALBUM:
                item = (Album) items.get(position);
                searchable.setMbid(item.getMbid());
                intent.putExtra("item", searchable);
                intent.setClass(context, AlbumDetails.class);
                context.startActivity(intent);
                break;
            case ARTIST:
//                intent.setClass(context, ArtistDetails.class);
//                context.startActivity(intent);
                break;
            case TRACK:
                item = (Track) items.get(position);
                searchable.setMbid(item.getMbid());
                intent.putExtra("item", searchable);
                intent.setClass(context, TrackDetails.class);
                context.startActivity(intent);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public static class FavoriteHolder extends RecyclerView.ViewHolder{

        LinearLayout item;
        TextView name, artist;
        ImageView image;

        // initializes the textview name (album's name)
        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.favorite_item);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            artist = itemView.findViewById(R.id.artist_name);

        }
    }
}