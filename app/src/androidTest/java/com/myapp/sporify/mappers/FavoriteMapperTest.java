package com.myapp.sporify.mappers;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.mappers.mocks.MockJsonData;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FavoriteMapperTest {

    @Test
    public void should_convert_it_to_albums() throws JSONException {
        String responseString = MockJsonData.albumFavoritesPass;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Album> parsedAlbums = FavoriteMapper.getFavoriteAlbumsFromJson(responseToJson);

            // response has two albums
            Assert.assertEquals(4, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    @Test
    public void should_get_empty_album_list() throws JSONException {
        String responseString = MockJsonData.albumFavoritesEmpty;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Album> parsedAlbums = FavoriteMapper.getFavoriteAlbumsFromJson(responseToJson);
            // response has ZERO albums
            Assert.assertEquals(0, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_convert_it_to_artists() throws JSONException {
        String responseString = MockJsonData.artistFavoritesPass;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Artist> parsedAlbums = FavoriteMapper.getFavoriteArtistsFromJson(responseToJson);

            // response has four artists
            Assert.assertEquals(4, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    @Test
    public void should_get_empty_artist_list() throws JSONException {
        String responseString = MockJsonData.artistFavoritesEmpty;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Artist> parsedAlbums = FavoriteMapper.getFavoriteArtistsFromJson(responseToJson);
            // response has ZERO artists
            Assert.assertEquals(0, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }


    @Test
    public void should_convert_it_to_tracks() throws JSONException {
        String responseString = MockJsonData.trackFavoritesPass;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Track> parsedAlbums = FavoriteMapper.getFavoriteTracksFromJson(responseToJson);

            // response has one track
            Assert.assertEquals(1, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    @Test
    public void should_get_empty_track_list() throws JSONException {
        String responseString = MockJsonData.trackFavoritesEmpty;
        JSONArray responseToJson = new JSONArray(responseString);

        try{
            List<Track> parsedAlbums = FavoriteMapper.getFavoriteTracksFromJson(responseToJson);
            // response has ZERO albums
            Assert.assertEquals(0, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

}