package com.example.sporify.adapters;

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
import com.example.sporify.R;
import com.example.sporify.activities.album.AlbumDetails;
import com.example.sporify.activities.artist.ArtistDetails;
import com.example.sporify.activities.track.TrackDetails;
import com.example.sporify.models.Searchable;
import com.example.sporify.utils.Type;

import java.util.List;

/**
 * Search adapter that shows the results when searching for Album/Artist/Track
 */
public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.SearchHolder>  {

    private List<Searchable> searchItems;
    private Context context;

    public SearchAdapter(Context context, List<Searchable> searchItems){
        this.context = context;
        this.searchItems = searchItems;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item , parent , false);
        return new SearchAdapter.SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        // getting the searchable item in position from searchItems
        Searchable searchable = searchItems.get(position);

        // setting the name & artistNae TextViews
        holder.name.setText(searchable.getName());
        holder.artistName.setText(searchable.getArtistName());

        // if image is not empty the set the imageURL in holder.image -> ImageView
        // else place a placeholder when an imageURL doesn't exist
        if(!searchable.getImageURL().isEmpty()){
            Glide.with(context)
                    .load(searchable.getImageURL())
                    .fitCenter()
                    .into(holder.image);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.artist_placeholder)
                    .fitCenter()
                    .into(holder.image);
        }


        // click listener when a search item is tapped
        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open details function
                openDetails(searchable);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    /**
     * Method that handles when a search item is clicked
     *
     * @param searchable Searchable object
     */
    private void openDetails(Searchable searchable) {
        // getting the kind of searched result
        // could be Album/Artist/Track
        Type kind = searchable.getKind();

        Intent intent = new Intent();
        intent.putExtra("item", searchable);

        // if kind is album/artist/track open the corresponding activity
        switch (kind){
            case ALBUM:
                intent.setClass(context, AlbumDetails.class);
                context.startActivity(intent);
                break;
            case ARTIST:
                intent.setClass(context, ArtistDetails.class);
                context.startActivity(intent);
                break;
            case TRACK:
                intent.setClass(context, TrackDetails.class);
                context.startActivity(intent);
                break;
        }

    }

    public static class SearchHolder extends RecyclerView.ViewHolder{

        LinearLayout search_item;
        TextView name,artistName;
        ImageView image;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            search_item = itemView.findViewById(R.id.search_item);
            name = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artist_name);
            image = itemView.findViewById(R.id.image);

        }
    }
}
