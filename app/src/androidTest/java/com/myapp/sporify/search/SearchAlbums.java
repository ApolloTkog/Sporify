package com.myapp.sporify.search;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.fragments.search.SearchViewModel;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the SearchAlbums functionality
 *
 */

@RunWith(AndroidJUnit4.class)
public class SearchAlbums {

    private SearchViewModel searchViewModel;
    private Searchable searchable = new Searchable();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        searchViewModel = new SearchViewModel();
    }

    @Test
    public void searchAlbumFound() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Nevermind";

        searchViewModel.init(queryToBeFound, Type.ALBUM);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        Assert.assertEquals(queryToBeFound, searchable.getName());

    }

    @Test
    public void searchAlbumCapitalCase(){}

    @Test
    public void searchAlbumLowerCase(){}

    @Test
    public void searchAlbumCorrection() throws InterruptedException {
        // album that user wants to search
        String albumIWantToSearch = "A Night at the Opera";

        // query that user types in search input
        String userQuery = "night at the";

        searchViewModel.init(userQuery, Type.ALBUM);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if album that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(albumIWantToSearch))
                searchable = x;
        }


        Assert.assertEquals(albumIWantToSearch, searchable.getName());

    }

    @Test
    public void searchAlbumSpellWrong() throws InterruptedException {

        // album that user wants to search
        String albumIWantToSearch = "A Night at the Opera";

        // query that user types in search input
        String userQuery = "a might at opera";

        searchViewModel.init(userQuery, Type.ALBUM);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        // if album that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(albumIWantToSearch))
                searchable = x;
        }

        Assert.assertNotEquals(albumIWantToSearch, searchable.getName());

    }

    @Test
    public void searchAlbumNotFound() throws InterruptedException {

        String queryToBeFound = "Nothing to be found";

        searchViewModel.init(queryToBeFound, Type.ALBUM);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        Assert.assertNotEquals(queryToBeFound, searchable.getName());
    }

}

