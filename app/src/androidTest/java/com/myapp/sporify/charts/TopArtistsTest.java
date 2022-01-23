package com.myapp.sporify.charts;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.MockDataReader;
import com.myapp.sporify.fragments.home.TopArtistsViewModel;
import com.myapp.sporify.mappers.ArtistMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TopArtistsTest {

    private TopArtistsViewModel topArtistsViewModel;
    private List<Artist> artistList;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        artistList = new ArrayList<>();
        topArtistsViewModel = new TopArtistsViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 20;
        artistList = LiveDataTestUtil.getOrAwaitValue(topArtistsViewModel.getTopArtists(topNumber));

        // wait 5s for items to be fetched
        // Thread.sleep(5000);

        assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 50;

        artistList = LiveDataTestUtil.getOrAwaitValue(topArtistsViewModel.getTopArtists(topNumber));

        // wait 5s for items to be fetched
        // Thread.sleep(5000);

        assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_top_100() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 100;


        artistList = LiveDataTestUtil.getOrAwaitValue(topArtistsViewModel.getTopArtists(topNumber));


        // wait 5s for items to be fetched
        // Thread.sleep(5000);

        assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_0() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 0;
        artistList = LiveDataTestUtil.getOrAwaitValue(topArtistsViewModel.getTopArtists(topNumber));


        // wait 5s for items to be fetched
        // Thread.sleep(5000);

        assertEquals(topNumber, artistList.size());
    }

    /* MOCK TESTS */

    @Test
    public void mock_should_return_top_20() throws Exception {
        int topNumber = 20;
        List<Artist> spyArtistList = Mockito.spy(new ArrayList<Artist>());

        artistList = ArtistMapper.getTopArtistsFromJson(MockDataReader.getNetworkResponse("artists/artistsTop20.json"));

        spyArtistList.addAll(artistList);
        Mockito.verify(spyArtistList).addAll(artistList);

        assertEquals(topNumber, spyArtistList.size());

        Mockito.doReturn(topNumber * 2).when(spyArtistList).size();
        assertEquals(topNumber * 2, spyArtistList.size());
    }

    @Test
    public void mock_should_return_top_50() throws Exception {
        int topNumber = 50;
        List<Artist> spyArtistList = Mockito.spy(new ArrayList<Artist>());

        artistList = ArtistMapper.getTopArtistsFromJson(MockDataReader.getNetworkResponse("artists/artistsTop50.json"));

        spyArtistList.addAll(artistList);
        Mockito.verify(spyArtistList).addAll(artistList);

        assertEquals(topNumber, spyArtistList.size());

        Mockito.doReturn(topNumber * 2).when(spyArtistList).size();
        assertEquals(topNumber * 2, spyArtistList.size());
    }


    @Test
    public void mock_should_return_0() {
        try{
            artistList = Mockito.spy(ArtistMapper.getTopArtistsFromJson(MockDataReader.getNetworkResponse("artists/artistsTop0.json")));
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            assertEquals("No value for artists", e.getMessage());
            assertEquals(0, artistList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
