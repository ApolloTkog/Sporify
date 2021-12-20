package com.myapp.sporify.mappers;

import com.myapp.sporify.mappers.mocks.MockJsonData;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ArtistMapperTest {

    @Test
    public void should_convert_it_to_artist() throws JSONException {
        String responseString = MockJsonData.artistInfoPass;
        JSONObject responseToJson = new JSONObject(responseString);

        Artist parsedArtist = ArtistMapper.getArtistFromJson(responseToJson);
        Artist actualArtist = new Artist("cc197bad-dc9c-440d-a5b5-d52ba2e14234","Coldplay");

        Assert.assertEquals(actualArtist, parsedArtist);
    }

    @Test
    public void should_convert_it_to_artists() throws JSONException {
        String responseString = MockJsonData.topArtistsPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Artist> parsedArtists = ArtistMapper.getTopArtistsFromJson(responseToJson);
            // response has two albums
            Assert.assertEquals(2, parsedArtists.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    //  not implemented
    @Test
    public void should_not_convert_it_to_artists() throws JSONException {
        String responseString = MockJsonData.topArtistsEmpty;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Artist> parsedArtists = ArtistMapper.getTopArtistsFromJson(responseToJson);
            // response has ZERO artists
            Assert.assertEquals(0, parsedArtists.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_to_convert_it_to_artist() throws Exception {
        String responseString = MockJsonData.artistInfoFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            Artist parsedArtist = ArtistMapper.getArtistFromJson(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for artists", e.getMessage());
        }

    }


    @Test
    public void should_get_artists_tracks() throws JSONException {
        String responseString = MockJsonData.artistTracksPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Track> parsedArtistTracks = ArtistMapper.getArtistTracksFromJson(responseToJson);

            Assert.assertNotEquals(parsedArtistTracks.isEmpty(), true);
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_get_artists_tracks() throws JSONException {
        String responseString = MockJsonData.artistTracksFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Track> parsedArtistTracks = ArtistMapper.getArtistTracksFromJson(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            Assert.assertEquals("No value for mvids", e.getMessage());
        }
    }

    @Test
    public void should_get_artists_albums() throws JSONException {
        String responseString = MockJsonData.artistAlbumsPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Album> parsedArtistAlbums = ArtistMapper.getArtistAlbumsFromJson(responseToJson);

            Assert.assertNotEquals(parsedArtistAlbums.isEmpty(), true);
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_get_artists_albums() throws JSONException {
        String responseString = MockJsonData.artistAlbumsFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Album> parsedArtistAlbums = ArtistMapper.getArtistAlbumsFromJson(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            Assert.assertEquals("No value for topalbums", e.getMessage());
        }
    }

    @Test
    public void should_get_image_url() throws JSONException {
        String responseString = MockJsonData.artistImagePass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            String imageURL = ArtistMapper.getArtistImage(responseToJson);
            Assert.assertNotEquals(imageURL, null);
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_get_image_url() throws JSONException {
        String responseString = MockJsonData.artistImageFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            String imageURL = ArtistMapper.getArtistImage(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            Assert.assertEquals("No value for artistthumb", e.getMessage());
        }
    }

}
