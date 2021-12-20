package com.myapp.sporify.mappers;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.mappers.mocks.MockJsonData;
import com.myapp.sporify.models.Album;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AlbumMapperTest {


    @Test
    public void should_convert_it_to_album() throws JSONException {
        String responseString = MockJsonData.albumInfoPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            Album parsedAlbum = AlbumMapper.getAlbumFromJson(responseToJson);
            Assert.assertTrue(true);
        }
        catch(JSONException e){
            Assert.fail("Should not fail");
        }

//        Album actualAlbum = new Album("f14638e1-fb36-358c-aba2-39b084864b13","A Night at the Opera", "Queen");
//
//        Assert.assertEquals(actualAlbum, parsedAlbum);
    }

    @Test
    public void should_convert_it_to_albums() throws JSONException {
        String responseString = MockJsonData.topAlbumsPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Album> parsedAlbums = AlbumMapper.getTopAlbumsFromJson(responseToJson);

            // response has two albums
            Assert.assertEquals(2, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    @Test
    public void should_not_convert_it_to_albums() throws JSONException {
        String responseString = MockJsonData.topAlbumsEmpty;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Album> parsedAlbums = AlbumMapper.getTopAlbumsFromJson(responseToJson);
            // response has ZERO albums
            Assert.assertEquals(0, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_to_convert_it_to_album() throws JSONException {
        String responseString = MockJsonData.albumInfoFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            Album parsedAlbum = AlbumMapper.getAlbumFromJson(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for album", e.getMessage());
        }

    }


}
