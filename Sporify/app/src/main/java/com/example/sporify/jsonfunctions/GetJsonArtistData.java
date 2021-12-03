package com.example.sporify.jsonfunctions;

import com.example.sporify.artist.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetJsonArtistData {

    //Class for future use.

    public Artist getJsonData(JSONObject object) throws JSONException {

        Artist currentArtist = null;

        final JSONArray artists = object.getJSONArray("artists");
        final int n = artists.length();
        for (int i = 0; i < n; ++i) {
            final JSONObject artist = artists.getJSONObject(i);
            currentArtist.setArtistId(artist.getInt("idArtist"));
            currentArtist.setName(artist.getString("strArtist"));
            currentArtist.setBio(artist.getString("strBiographyEN"));
            currentArtist.setThumbnailUrl(artist.getString("strArtistThumb"));
            currentArtist.setGenre(artist.getString("strGenre"));
            currentArtist.setMood(artist.getString("strMood"));

        }

        return currentArtist;

    }


}
