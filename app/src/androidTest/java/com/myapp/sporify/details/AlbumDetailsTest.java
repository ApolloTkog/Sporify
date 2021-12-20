package com.myapp.sporify.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.activities.album.AlbumDetailsViewModel;
import com.myapp.sporify.fragments.home.TopAlbumsViewModel;
import com.myapp.sporify.models.Album;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AlbumDetailsTest {

    private AlbumDetailsViewModel albumDetailsViewModel;
    private List<Album> albumList;

    public Album albumInfo;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        albumDetailsViewModel = new AlbumDetailsViewModel();
    }

    @Test
    public void should_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String albumToBeFound = "f14638e1-fb36-358c-aba2-39b084864b13";

        albumDetailsViewModel.getAlbum(albumToBeFound).observeForever(new Observer<Album>() {
            @Override
            public void onChanged(Album album) {
                // if we don't have results return;
                // if our query exists in results
                AlbumDetailsTest.this.albumInfo = album;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNotEquals(null, albumInfo);
    }

    @Test
    public void should_fail_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String albumToBeFound = "";

        albumDetailsViewModel.getAlbum(albumToBeFound).observeForever(new Observer<Album>() {
            @Override
            public void onChanged(Album album) {
                // if we don't have results return;
                // if our query exists in results
                AlbumDetailsTest.this.albumInfo = album;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNull(albumInfo);
    }
}