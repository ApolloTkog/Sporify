package com.myapp.sporify.details;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.activities.album.AlbumDetailsViewModel;
import com.myapp.sporify.activities.artist.ArtistDetailsViewModel;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ArtistDetailsTest {


    private ArtistDetailsViewModel artistDetailsViewModel;
    private List<Album> albumList;
    private List<Track> trackList;

    private Artist artistInfo;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        albumList = new ArrayList<>();
        trackList = new ArrayList<>();
        artistDetailsViewModel = new ArtistDetailsViewModel();
    }

    @Test
    public void should_get_artist_info() throws InterruptedException {
        // query that is expected to be found
        String artistToBeFound = "420ca290-76c5-41af-999e-564d7c71f1a7";

        artistDetailsViewModel.init(artistToBeFound);
        artistDetailsViewModel.getArtist().observeForever(new Observer<Artist>() {
            @Override
            public void onChanged(Artist artist) {
                // if we don't have results return;
                // if our query exists in results
                artistInfo = artist;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNotEquals(null, artistInfo);
    }

    @Test
    public void should_fail_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String albumToBeFound = "";

        artistDetailsViewModel.init(albumToBeFound);
        artistDetailsViewModel.getArtist().observeForever(new Observer<Artist>() {
            @Override
            public void onChanged(Artist artist) {
                // if we don't have results return;
                // if our query exists in results
                artistInfo = artist;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNull(artistInfo);
    }

    @Test
    public void should_get_artist_albums() throws  InterruptedException{
        // query that is expected to be found
        String artistToBeFound = "420ca290-76c5-41af-999e-564d7c71f1a7";

        artistDetailsViewModel.init(artistToBeFound);
        artistDetailsViewModel.getArtistAlbums().observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                // if we don't have results return;
                // if our query exists in results
                albumList = albums;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertFalse(albumList.isEmpty());
    }

    @Test
    public void should_fail_get_artist_albums() throws  InterruptedException{
        // query that is expected to be found
        String artistToBeFound = "";

        artistDetailsViewModel.init(artistToBeFound);
        artistDetailsViewModel.getArtistAlbums().observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                // if we don't have results return;
                // if our query exists in results
                albumList = albums;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertTrue(albumList.isEmpty());
    }


    @Test
    public void should_get_artist_tracks() throws  InterruptedException{
        // query that is expected to be found

        // audio db id
        String artistToBeFound = "111238";

        artistDetailsViewModel.init(artistToBeFound);
        artistDetailsViewModel.getArtistTracks().observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                // if we don't have results return;
                // if our query exists in results
                trackList = tracks;
            }
        });

        // wait 10s for items to be fetched
        // because audio db API is slow
        Thread.sleep(10000);

        // this artist has 39 tracks in audiodb
        Assert.assertEquals(39, trackList.size());
    }

    @Test
    public void should_fail_get_artist_tracks() throws  InterruptedException{
        // query that is expected to be found
        String artistToBeFound = "";

        artistDetailsViewModel.init(artistToBeFound);
        artistDetailsViewModel.getArtistTracks().observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                // if we don't have results return;
                // if our query exists in results
                trackList = tracks;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertTrue(trackList.isEmpty());
    }
}
