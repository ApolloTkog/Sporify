package com.myapp.sporify.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.activities.album.AlbumDetailsViewModel;
import com.myapp.sporify.activities.track.TrackDetailsViewModel;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        trackDetailsViewModel.getTrackInfo(trackToBeFound).observeForever(new Observer<Track>() {
            @Override
            public void onChanged(Track track) {
                // if we don't have results return;
                // if our query exists in results
                trackInfo = track;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNotEquals(null, trackInfo);
    }

    @Test
    public void should_fail_get_album_info() throws InterruptedException {
        // query that is expected to be found
        String trackToBeNotFound = "";

        trackDetailsViewModel.getTrackInfo(trackToBeNotFound).observeForever(new Observer<Track>() {
            @Override
            public void onChanged(Track track) {
                // if we don't have results return;
                // if our query exists in results
                trackInfo = track;
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(5000);

        Assert.assertNull(trackInfo);
    }

}
