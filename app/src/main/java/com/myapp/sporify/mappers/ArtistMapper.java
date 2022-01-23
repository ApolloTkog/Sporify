package com.myapp.sporify.mappers;

import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistMapper {

    public static Artist getArtistFromJson(JSONObject response) throws JSONException {

        Artist artist = new Artist();


        try{
            JSONArray jsonArray = response.getJSONArray("artists");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

                String mbid = jsonObject.getString("strMusicBrainzID");
                String name = jsonObject.getString("strArtist");
                String image = jsonObject.getString("strArtistFanart2").equals("null") ? "" : jsonObject.getString("strArtistFanart2");
                String biography = jsonObject.getString("strBiographyEN");
                String genre = jsonObject.getString("strGenre");
                String mood = jsonObject.getString("strMood");


                artist = new Artist(mbid, name, image, genre, biography);
                artist.setMood(mood);

            }
        }
        catch (JSONException e){
            throw new JSONException("No value for artists");
        }


        return artist;
    }

    public static List<Track> getArtistTracksFromJson(JSONObject response) throws JSONException{

        List<Track> tracks = new ArrayList<>();

        try{
            JSONArray jsonArray = response.getJSONArray("mvids");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("strTrack");
                String image = jsonObject.getString("strTrackThumb").equals("null") ? "" : jsonObject.getString("strTrackThumb");
                String youtubeURL = jsonObject.getString("strMusicVid");

                Track track = new Track(i + 1, name, image, youtubeURL, "");

                tracks.add(track);

            }
        }
        catch (JSONException e){
            throw new JSONException("No value for mvids");
        }


        return tracks;
    }

    public static List<Album> getArtistAlbumsFromJson(JSONObject response) throws JSONException{
        List<Album> albumList = new ArrayList<>();

        JSONObject artists_json = response.getJSONObject("topalbums");
        JSONArray jsonArray = artists_json.getJSONArray("album");

        int rank = 1;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("ARRAY IS" + jsonObject);
            String mbid = "";

            try{
                mbid = jsonObject.getString("mbid");
            }
            catch (JSONException e){
                System.err.println("MBID cannot be parsed!");
            }

//                    String mbid = jsonObject.getString("mbid");
            String name = jsonObject.getString("name");
            String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
            String artistName = jsonObject.getJSONObject("artist").getString("name");
            long playCount = jsonObject.getLong("playcount");


            Album album = new Album(rank, mbid, name, artistName, image, playCount);
            albumList.add(album);
            rank++;
        }

        return albumList;
    }

    public static List<Artist> getTopArtistsFromJson(JSONObject response) throws JSONException {
        List<Artist> artistList = new ArrayList<>();

        JSONObject artists_json = response.getJSONObject("artists");
        JSONArray jsonArray = artists_json.getJSONArray("artist");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String mbid = "";
            try{
                mbid = jsonObject.getString("mbid");
            }
            catch (JSONException e){
                System.err.println("MBID cannot be parsed!");
            }
            String name = jsonObject.getString("name");
            long playcount = jsonObject.getLong("playcount");
            long listeners = jsonObject.getLong("listeners");

            Artist artist = new Artist(mbid, name, "", playcount, listeners);
            artistList.add(artist);
        }

        return artistList;
    }

    public static String getArtistImage(JSONObject response) throws JSONException{
        JSONArray artistThumbs = response.getJSONArray("artistthumb");

        return artistThumbs.getJSONObject(0).getString("url");
    }
}
