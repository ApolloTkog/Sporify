package com.myapp.sporify.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.MockDataReader;
import com.myapp.sporify.activities.album.AlbumDetailsViewModel;
import com.myapp.sporify.fragments.home.TopAlbumsViewModel;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;


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
public class AlbumDetailsTest {

    private AlbumDetailsViewModel albumDetailsViewModel;
    private List<Album> albumList;

    public Album albumInfo;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        albumDetailsViewModel = new AlbumDetailsViewModel();
    }

    @Test
    public void should_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String albumToBeFound = "f14638e1-fb36-358c-aba2-39b084864b13";

//        albumDetailsViewModel.getAlbum(albumToBeFound).getOrAwaitValue();

        Album album = LiveDataTestUtil.getOrAwaitValue(albumDetailsViewModel.getAlbum(albumToBeFound));


        // wait 1s for items to be fetched
//        Thread.sleep(5000);

        Assert.assertNotEquals(null, album);
    }

    @Test
    public void should_fail_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String albumToBeFound = "";

        Album album = LiveDataTestUtil.getOrAwaitValue(albumDetailsViewModel.getAlbum(albumToBeFound));


        // wait 1s for items to be fetched
//        Thread.sleep(5000);

        Assert.assertNull(album);
    }

    @Spy
    Album albumMock;

    @Test
    public void mock_should_get_album_info() throws Exception {
        // get mock response from json file
        Album albumInfo = Mockito.spy(AlbumMapper.getAlbumFromJson(MockDataReader.getNetworkResponse("albums/albumPassTest.json")));

        // create mock object
        albumMock = Mockito.spy(Album.class);

        // set some values
        albumMock.setName("A Night at the Opera");
        Mockito.verify(albumMock).setName("A Night at the Opera");

        albumMock.setArtistName("Queen");
        Mockito.verify(albumMock).setArtistName("Queen");

        // check if values are the same with mock response
        Assert.assertEquals(albumMock.getArtistName(), albumInfo.getArtistName());
        Assert.assertEquals(albumMock.getName(), albumInfo.getName());
    }

    @Test
    public void mock_should_fail_get_album_info() {
        try{
            albumInfo = Mockito.spy(AlbumMapper.getAlbumFromJson(MockDataReader.getNetworkResponse("albums/albumFailTest.json")));
            albumMock = Mockito.spy(Album.class);
            Assert.fail("Should have thrown JSON parse exception");
        }
        catch (JSONException e){
            // success
            Assert.assertEquals("No value for album", e.getMessage());
            Assert.assertNull(albumInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}