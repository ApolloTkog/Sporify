package com.myapp.sporify.mappers;

import android.util.Log;

import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMapper {

    public static List<Playlist> getPlaylistsFromJson(JSONArray response) throws JSONException {
        List<Playlist> playlistList = new ArrayList<>();

        for(int i = 0; i < response.length(); i++){
            List<Track> trackList = new ArrayList<>();

            JSONObject trackObject = response.getJSONObject(i);

            String id = trackObject.getString("id");
            String name = trackObject.getString("name");

            JSONArray playlistTracks = trackObject.getJSONArray("tracks");

            for(int j = 0; j < playlistTracks.length(); j++){
                JSONObject currentTrack = playlistTracks.getJSONObject(j);

                String trackId = currentTrack.getString("id");
                String mbid = currentTrack.getString("musicBrainzId");
                String trackName = currentTrack.getString("name");
                String artist = currentTrack.getString("artist");
                String imageURL = currentTrack.getString("imageURL");
                String youtubeUrl = currentTrack.getString("youtubeUrl");

                Track track = new Track(j+1, trackName, artist, imageURL);
                track.setId(trackId);
                track.setMbid(mbid);
                track.setYoutubeURL(youtubeUrl);
                trackList.add(track);
            }

            Playlist playlist = new Playlist(id, name);
            playlist.setPlaylistTracks(trackList);
            Log.d("Playlist", playlist.getName());

            playlistList.add(playlist);

        }

        return playlistList;

    }
}
