package com.myapp.sporify.mappers;

import android.util.Log;

import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumMapper {
    public static Album getAlbumFromJson(JSONObject response) throws JSONException {
        Album album = new Album();
        JSONObject jsonObject = response.getJSONObject("album");

        // from the album object get name, image, artist
        String name = jsonObject.getString("name");
        String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
        String artistName = jsonObject.getString("artist");

        String artistMbid = "";

        List<Track> tracksList = new ArrayList<>();

        // from the object tracks get the json array called track
        JSONArray tracks = jsonObject.getJSONObject("tracks").getJSONArray("track");

        // parse the json array of tracks
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject trackObject = tracks.getJSONObject(i);

            // get track's name
            String trackName = trackObject.getString("name");
            // get track's duration
            int trackDuration = 0;
            try {
                trackDuration = trackObject.getInt("duration");
            }
            catch (JSONException e){
                System.out.println(trackName + " has no duration");
            }
            //get artist's mbid
            artistMbid = trackObject.getJSONObject("artist").getString("mbid");

            // create track object
            Track track = new Track(trackName, artistName, trackDuration);

            // add it to list
            tracksList.add(track);
        }

        // create album object
        album = new Album("", name, artistName, image, tracksList);
        album.setArtistMbid(artistMbid);


        return album;

    }
}
