package com.myapp.sporify.mappers;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.mappers.mocks.MockJsonData;
import com.myapp.sporify.models.Track;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrackMapperTest {

    @Test
    public void should_convert_it_to_track() throws JSONException {
        String responseString = MockJsonData.trackInfoPass;
        JSONObject responseToJson = new JSONObject(responseString);

        Track parsedTrack = TrackMapper.getTrackFromJson(responseToJson);
        Track actualTrack = new Track("f14638e1-fb36-358c-aba2-39b084864b13","Queen", 355000);

        Assert.assertEquals(parsedTrack, actualTrack);
    }

    @Test
    public void should_convert_it_to_tracks() throws JSONException {
        String responseString = MockJsonData.topTracksPass;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Track> parseTracks = TrackMapper.getTopTracksFromJson(responseToJson);

            // response has two albums
            Assert.assertEquals(2, parseTracks.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }

    }

    @Test
    public void should_not_convert_it_to_tracks() throws JSONException {
        String responseString = MockJsonData.topTracksEmpty;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            List<Track> parsedAlbums = TrackMapper.getTopTracksFromJson(responseToJson);
            // response has ZERO albums
            Assert.assertEquals(0, parsedAlbums.size());
        }
        catch (JSONException e){
            Assert.fail("Should not fail");
        }
    }

    @Test
    public void should_fail_to_convert_it_to_track() throws JSONException {
        String responseString = MockJsonData.trackInfoFail;
        JSONObject responseToJson = new JSONObject(responseString);

        try{
            Track parsedTrack = TrackMapper.getTrackFromJson(responseToJson);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for track", e.getMessage());
        }

    }
}
