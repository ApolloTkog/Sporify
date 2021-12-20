package com.myapp.sporify.charts;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.myapp.sporify.fragments.home.TopTracksViewModel;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TopTracksTest {

    private TopTracksViewModel topTracksViewModel;
    private List<Track> trackList;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp(){
        topTracksViewModel = new TopTracksViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 20;

        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> topTracks) {
                // if we don't have results return;
                if(topTracks.size() <= 0)
                    return;

                trackList = topTracks;
            }
        });

        // wait 2s for items to be fetched
        //comment 2.0
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 50;

        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> topTracks) {
                // if we don't have results return;
                if(topTracks.size() <= 0)
                    return;

                trackList = topTracks;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_top_100() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 20;

        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> topTracks) {
                // if we don't have results return;
                if(topTracks.size() <= 0)
                    return;

                trackList = topTracks;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }

    @Test
    public void should_return_0() throws InterruptedException {
        // query that is expected to be found
        trackList = new ArrayList<>();

        int topNumber = 0;

        topTracksViewModel.getTopTracks(topNumber).observeForever(new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> topTracks) {
                trackList = topTracks;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, trackList.size());
    }
}
