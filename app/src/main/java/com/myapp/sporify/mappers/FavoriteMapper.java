package com.myapp.sporify.mappers;

import android.util.Log;

import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMapper {

    public static List<Album> getFavoriteAlbumsFromJson(JSONArray response) throws JSONException {
        List<Album> albumList = new ArrayList<>();

        for(int i = 0; i < response.length(); i++){
            JSONObject trackObject = response.getJSONObject(i);

            String mbid = trackObject.getString("musicBrainzId");
            String name = trackObject.getString("name");
            String artist = trackObject.getString("artist");
            String imageURL = trackObject.getString("imageURL");

            Album album = new Album(mbid, name, artist, imageURL);
            Log.d("Favorites", album.getName());

            albumList.add(album);

        }

        return albumList;

    }

    public static List<Artist> getFavoriteArtistsFromJson(JSONArray response) throws JSONException {
        List<Artist> artistList = new ArrayList<>();


        for(int i = 0; i < response.length(); i++){
            JSONObject trackObject = response.getJSONObject(i);

            String mbid = trackObject.getString("musicBrainzId");
            String name = trackObject.getString("name");
            String imageURL = trackObject.getString("imageURL");

            Artist artist = new Artist(mbid, name, imageURL);
            Log.d("Favorites", artist.getName());

            artistList.add(artist);

        }

        return artistList;
    }

    public static List<Track> getFavoriteTracksFromJson(JSONArray response) throws JSONException {
        List<Track> trackList = new ArrayList<>();

        for(int i = 0; i < response.length(); i++){
            JSONObject trackObject = response.getJSONObject(i);

            String mbid = trackObject.getString("musicBrainzId");
            String name = trackObject.getString("name");
            String artist = trackObject.getString("artist");
            String imageURL = trackObject.getString("imageURL");

            Track track = new Track(i + 1, name, artist, imageURL);
            track.setMbid(mbid);
            // Log.d("Favorites", album.getName());

            trackList.add(track);

        }

        return trackList;
    }
}