package com.myapp.sporify.adapters;

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

import java.util.List;


/**
 * Favorite adapter for showing favorite albums in recycler view
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavoriteHolder>{

    private Context context;
    private List<String> albums;

    public FavouriteAdapter(Context context, List<String> albums){
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set the favorite item layout
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_item, parent , false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        String album = albums.get(position);

        // setting to the textview name the album's name
        holder.name.setText(album);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class FavoriteHolder extends RecyclerView.ViewHolder{

        TextView name;

        // initializes the textview name (album's name)
        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

        }
    }
}

