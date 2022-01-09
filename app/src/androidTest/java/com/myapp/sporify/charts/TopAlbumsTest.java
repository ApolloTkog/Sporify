package com.myapp.sporify.charts;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.MockDataReader;
import com.myapp.sporify.fragments.home.TopAlbumsViewModel;
import com.myapp.sporify.fragments.search.SearchViewModel;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TopAlbumsTest {

    private TopAlbumsViewModel topAlbumsViewModel;
    private List<Album> albumList;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp(){
        albumList = new ArrayList<>();
        topAlbumsViewModel = new TopAlbumsViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 20;

        albumList = LiveDataTestUtil.getOrAwaitValue(topAlbumsViewModel.getTopAlbums(topNumber));

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 50;

        albumList = LiveDataTestUtil.getOrAwaitValue(topAlbumsViewModel.getTopAlbums(topNumber));

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_return_top_60() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 60;

        albumList = LiveDataTestUtil.getOrAwaitValue(topAlbumsViewModel.getTopAlbums(topNumber));

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_not_return_0() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 0;

        albumList = LiveDataTestUtil.getOrAwaitValue(topAlbumsViewModel.getTopAlbums(topNumber));

        // returns all albums if limit equals to 0
        Assert.assertNotEquals(topNumber, albumList.size());
    }

    /* MOCK TESTS */

    @Test
    public void mock_should_return_top_20() throws Exception {
        int topNumber = 20;
        List<Album> spyAlbumList = Mockito.spy(new ArrayList<Album>());

        albumList = AlbumMapper.getTopAlbumsFromJson(MockDataReader.getNetworkResponse("albums/albumsTop20.json"));

        spyAlbumList.addAll(albumList);
        Mockito.verify(spyAlbumList).addAll(albumList);

        assertEquals(topNumber, spyAlbumList.size());

        Mockito.doReturn(topNumber * 2).when(spyAlbumList).size();
        assertEquals(topNumber * 2, spyAlbumList.size());
    }

    @Test
    public void mock_should_return_top_50() throws Exception {
        int topNumber = 50;
        List<Album> spyAlbumList = Mockito.spy(new ArrayList<Album>());

        albumList = AlbumMapper.getTopAlbumsFromJson(MockDataReader.getNetworkResponse("albums/albumsTop50.json"));

        spyAlbumList.addAll(albumList);
        Mockito.verify(spyAlbumList).addAll(albumList);

        assertEquals(topNumber, spyAlbumList.size());

        Mockito.doReturn(topNumber * 2).when(spyAlbumList).size();
        assertEquals(topNumber * 2, spyAlbumList.size());
    }

    @Test
    public void mock_should_return_top_60() throws Exception {
        int topNumber = 60;
        List<Album> spyAlbumList = Mockito.spy(new ArrayList<Album>());

        albumList = AlbumMapper.getTopAlbumsFromJson(MockDataReader.getNetworkResponse("albums/albumsTop100.json"));

        spyAlbumList.addAll(albumList);
        Mockito.verify(spyAlbumList).addAll(albumList);

        assertEquals(topNumber, spyAlbumList.size());

        Mockito.doReturn(topNumber * 2).when(spyAlbumList).size();
        assertEquals(topNumber * 2, spyAlbumList.size());
    }

    @Test
    public void mock_should_return_0() {
        try{
            albumList = Mockito.spy(AlbumMapper.getTopAlbumsFromJson(MockDataReader.getNetworkResponse("albums/albumsTop0.json")));
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for albums", e.getMessage());
            Assert.assertEquals(0, albumList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
