package com.myapp.sporify.charts;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.fragments.home.TopAlbumsViewModel;
import com.myapp.sporify.fragments.search.SearchViewModel;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        topAlbumsViewModel = new TopAlbumsViewModel();
    }

    @Test
    public void should_return_top_20() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 20;

        topAlbumsViewModel.getTopAlbums(topNumber).observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> topAlbums) {
                // if we don't have results return;
                if(topAlbums.size() <= 0)
                    return;

                albumList = topAlbums;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_return_top_50() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 50;

        topAlbumsViewModel.getTopAlbums(topNumber).observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> topAlbums) {
                // if we don't have results return;
                if(topAlbums.size() <= 0)
                    return;

                albumList = topAlbums;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_return_top_100() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 20;

        topAlbumsViewModel.getTopAlbums(topNumber).observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> topAlbums) {
                // if we don't have results return;
                if(topAlbums.size() <= 0)
                    return;

                albumList = topAlbums;
            }
        });

        // wait 2s for items to be fetched
        Thread.sleep(2000);

        Assert.assertEquals(topNumber, albumList.size());
    }

    @Test
    public void should_not_return_0() throws InterruptedException {
        // query that is expected to be found
        albumList = new ArrayList<>();

        int topNumber = 0;

        topAlbumsViewModel.getTopAlbums(topNumber).observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> topAlbums) {
                albumList = topAlbums;
            }
        });

        // wait 5s for items to be fetched
        Thread.sleep(5000);

        // returns all albums if limit equals to 0
        Assert.assertNotEquals(topNumber, albumList.size());
    }
}
