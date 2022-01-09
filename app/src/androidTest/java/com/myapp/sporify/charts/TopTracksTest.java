package com.myapp.sporify.charts;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.MockDataReader;
import com.myapp.sporify.fragments.home.TopTracksViewModel;
import com.myapp.sporify.mappers.TrackMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.models.Track;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TopTracksTest {

    private TopTracksViewModel topTracksViewModel;
    private List<Track> trackList;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp(){
        trackList = new ArrayList<>();
        topTracksViewModel = new TopTracksViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 20;

//        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
//            @Override
//            public void onChanged(List<Track> topTracks) {
//                // if we don't have results return;
//                if(topTracks.size() <= 0)
//                    return;
//
//                trackList = topTracks;
//            }
//        });
//
        trackList = LiveDataTestUtil.getOrAwaitValue(topTracksViewModel.getTopTracks(topNumber));

        // wait 2s for items to be fetched
        // Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 50;

//        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
//            @Override
//            public void onChanged(List<Track> topTracks) {
//                // if we don't have results return;
//                if(topTracks.size() <= 0)
//                    return;
//
//                trackList = topTracks;
//            }
//        });

        trackList = LiveDataTestUtil.getOrAwaitValue(topTracksViewModel.getTopTracks(topNumber));


        // wait 2s for items to be fetched
        //Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_top_10() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 10;

        trackList = LiveDataTestUtil.getOrAwaitValue(topTracksViewModel.getTopTracks(topNumber));

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_0() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 0;

//        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
//            @Override
//            public void onChanged(List<Track> topTracks) {
//                trackList = topTracks;
//            }
//        });


        trackList = LiveDataTestUtil.getOrAwaitValue(topTracksViewModel.getTopTracks(topNumber));

        // wait 2s for items to be fetched
        //Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }


    /* MOCK TESTS */

    @Test
    public void mock_should_return_top_20() throws Exception {
        int topNumber = 20;
        List<Track> spyTrackList = Mockito.spy(new ArrayList<Track>());

        trackList = TrackMapper.getTopTracksFromJson(MockDataReader.getNetworkResponse("tracks/tracksTop20.json"));

        spyTrackList.addAll(trackList);
        Mockito.verify(spyTrackList).addAll(trackList);

        assertEquals(topNumber, spyTrackList.size());

        Mockito.doReturn(topNumber * 2).when(spyTrackList).size();
        assertEquals(topNumber * 2, spyTrackList.size());
    }

    @Test
    public void mock_should_return_top_50() throws Exception {
        int topNumber = 50;
        List<Track> spyTrackList = Mockito.spy(new ArrayList<Track>());

        trackList = TrackMapper.getTopTracksFromJson(MockDataReader.getNetworkResponse("tracks/tracksTop50.json"));

        spyTrackList.addAll(trackList);
        Mockito.verify(spyTrackList).addAll(trackList);

        assertEquals(topNumber, spyTrackList.size());

        Mockito.doReturn(topNumber * 2).when(spyTrackList).size();
        assertEquals(topNumber * 2, spyTrackList.size());
    }

    @Test
    public void mock_should_return_top_100() throws Exception {
        int topNumber = 60;
        List<Track> spyTrackList = Mockito.spy(new ArrayList<Track>());

        trackList = TrackMapper.getTopTracksFromJson(MockDataReader.getNetworkResponse("tracks/tracksTop100.json"));

        spyTrackList.addAll(trackList);
        Mockito.verify(spyTrackList).addAll(trackList);

        assertEquals(topNumber, spyTrackList.size());

        Mockito.doReturn(topNumber * 2).when(spyTrackList).size();
        assertEquals(topNumber * 2, spyTrackList.size());
    }

    @Test
    public void mock_should_return_0() {
        try{
            trackList = Mockito.spy(TrackMapper.getTopTracksFromJson(MockDataReader.getNetworkResponse("tracks/tracksTop0.json")));
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for tracks", e.getMessage());
            Assert.assertEquals(0, trackList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
