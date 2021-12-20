package com.myapp.sporify.charts;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.myapp.sporify.fragments.home.TopArtistsViewModel;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TopArtistsTest {

    private TopArtistsViewModel topArtistsViewModel;
    private List<Artist> artistList;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp(){
        topArtistsViewModel = new TopArtistsViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 20;

        topArtistsViewModel.getTopArtists(topNumber).observeForever(new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> topArtists) {
                // if we don't have results return;
                if(topArtists.size() <= 0)
                    return;

                artistList = topArtists;
            }
        });

        // wait 5s for items to be fetched
        Thread.sleep(5000);

        Assert.assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 50;

        topArtistsViewModel.getTopArtists(topNumber).observeForever(new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> topArtists) {
                // if we don't have results return;
                if(topArtists.size() <= 0)
                    return;

                artistList = topArtists;
            }
        });

        // wait 5s for items to be fetched
        Thread.sleep(5000);

        Assert.assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_top_100() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 20;

        topArtistsViewModel.getTopArtists(topNumber).observeForever(new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> topArtists) {
                // if we don't have results return;
                if(topArtists.size() <= 0)
                    return;

                artistList = topArtists;
            }
        });

        // wait 5s for items to be fetched
        Thread.sleep(5000);

        Assert.assertEquals(topNumber, artistList.size());
    }

    @Test
    public void should_return_0() throws InterruptedException {
        // query that is expected to be found
        artistList = new ArrayList<>();

        int topNumber = 0;

        topArtistsViewModel.getTopArtists(topNumber).observeForever(new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> topArtists) {
                artistList = topArtists;
            }
        });

        // wait 5s for items to be fetched
        Thread.sleep(5000);

        Assert.assertEquals(topNumber, artistList.size());
    }
}
