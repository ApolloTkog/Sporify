package com.myapp.sporify.mappers;

import android.util.Log;

import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackMapper {

    public static Track getTrackFromJson(JSONObject response) throws JSONException {
        JSONObject jsonObject = response.getJSONObject("track");

        String name = jsonObject.getString("name");
        String image = jsonObject.getJSONObject("album").getJSONArray("image").getJSONObject(3).getString("#text");
        String artistName = jsonObject.getJSONObject("artist").getString("name");
        String artistMbid = jsonObject.getJSONObject("artist").getString("mbid");

        String summary = jsonObject.getJSONObject("wiki").getString("summary");
        String content = jsonObject.getJSONObject("wiki").getString("content");


        Log.d("ARTIST MBID", artistMbid);

        return new Track(name, image, artistName, artistMbid, summary, content);
    }

    public static List<Track> getTopTracksFromJson(JSONObject response) throws JSONException {
        List<Track> trackList = new ArrayList<>();

        JSONObject artists_json = response.getJSONObject("tracks");
        JSONArray jsonArray = artists_json.getJSONArray("track");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

//                    String mbid = jsonObject.getString("mbid");
            String name = jsonObject.getString("name");
            String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
            String artistName = jsonObject.getJSONObject("artist").getString("name");

            Track track = new Track(i + 1, name, artistName, image);
            trackList.add(track);
        }
        return trackList;
    }
}

