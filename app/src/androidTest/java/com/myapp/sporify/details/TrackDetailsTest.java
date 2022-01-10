package com.myapp.sporify.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.MockDataReader;
import com.myapp.sporify.activities.track.TrackDetailsViewModel;
import com.myapp.sporify.mappers.TrackMapper;
import com.myapp.sporify.models.Track;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrackDetailsTest {


    private TrackDetailsViewModel trackDetailsViewModel;
    private List<Track> trackList;

    public Track trackInfo;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        trackDetailsViewModel = new TrackDetailsViewModel();
    }

    @Test
    public void should_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String trackToBeFound = "ecfa6746-7bc4-4088-ace6-d209477bd63f";

        trackInfo = LiveDataTestUtil.getOrAwaitValue(trackDetailsViewModel.getTrackInfo(trackToBeFound));

        Assert.assertNotEquals(null, trackInfo);
    }

    @Test
    public void should_fail_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String trackToBeNotFound = "";

        trackInfo = LiveDataTestUtil.getOrAwaitValue(trackDetailsViewModel.getTrackInfo(trackToBeNotFound));

        Assert.assertNull(trackInfo.getMbid());
        Assert.assertEquals("No info" ,trackInfo.getName());
        Assert.assertNull(trackInfo.getArtistName());

    }

    @Spy
    Track trackMock;

    @Test
    public void mock_should_get_album_info() throws Exception {
        Track albumInfo = Mockito.spy(TrackMapper.getTrackFromJson(MockDataReader.getNetworkResponse("tracks/trackPassTest.json")));
        trackMock = Mockito.spy(Track.class);

        trackMock.setName("D.D.");
        Mockito.verify(trackMock).setName("D.D.");

        trackMock.setArtistName("The Weeknd");
        Mockito.verify(trackMock).setArtistName("The Weeknd");

        Assert.assertEquals(trackMock.getArtistName(), albumInfo.getArtistName());
        Assert.assertEquals(trackMock.getName(), albumInfo.getName());
    }

    @Test
    public void mock_should_fail_get_album_info() {
        try{
            trackInfo = Mockito.spy(TrackMapper.getTrackFromJson(MockDataReader.getNetworkResponse("tracks/trackFailTest.json")));
            trackMock = Mockito.spy(Track.class);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for track", e.getMessage());
            Assert.assertNull(trackInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
