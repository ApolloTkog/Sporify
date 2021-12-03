package com.example.sporify.jsonfunctions;

import com.example.sporify.artist.Artist;
import com.example.sporify.tracks.Tracks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToDataFactory {

    //WIll have to make exclusive objects for each class instead of calling them with their corresponding class.

    public Artist getJsonData(JSONObject object, Artist currentArtist) throws JSONException {

        final JSONArray  artists = object.getJSONArray("artists");
        final int n = artists.length();
        for (int i = 0; i < n; ++i) {
            final JSONObject artist = artists.getJSONObject(i);
            currentArtist.setArtistId(artist.getInt("idArtist"));
            currentArtist.setName(artist.getString("strArtist"));
            currentArtist.setBio(artist.getString("strBiographyEN"));
            currentArtist.setThumbnailUrl(artist.getString("strArtistThumb"));
            currentArtist.setGenre(artist.getString("strGenre"));
            currentArtist.setMood(artist.getString("strMood"));


        return artist;
    }

    public Tracks getJsonData(JSONObject object, Tracks tracks){




        return null;
    }


}
