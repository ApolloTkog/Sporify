package com.myapp.sporify.search;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.fragments.search.SearchViewModel;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;

import org.jetbrains.annotations.TestOnly;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the SearchArtists functionality
 *
 */
@RunWith(AndroidJUnit4.class)
public class SearchArtists {


    private SearchViewModel searchViewModel;
    private Searchable searchable = new Searchable();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUP(){
        searchViewModel = new SearchViewModel();
    }

    @Test
    public void searchArtistFound() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Queen";

        searchViewModel.init(queryToBeFound, Type.ARTIST);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if our query exists in results
        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }
        Assert.assertEquals(queryToBeFound, searchable.getName());

    }

    @Test
    public void searchArtistCorrection() throws InterruptedException {
        // correction doesn't work for search artist as audioDB API is feature limited

        // track that user wants to search
        String artistIWantToSearch = "Queen";

        // query that user types in search input
        String userQuery = "quee";

        searchViewModel.init(userQuery, Type.ARTIST);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if album that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(artistIWantToSearch))
                searchable = x;
        }



        Assert.assertNotEquals(artistIWantToSearch, searchable.getName());

    }

    @Test
    public void searchArtistNotFound() throws InterruptedException {
        // query that is expected to not be found
        String queryToBeFound = "Nothing to be found";

        searchViewModel.init(queryToBeFound, Type.ARTIST);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        // we expect searchable.getName() to be null
        Assert.assertNotEquals(queryToBeFound, searchable.getName());
    }

    @Test
    public void searchArtistCapitalCase() throws InterruptedException {
        // artist's name
        String artistToBeFound = "Queen";

        // user's query in CAPITAL
        String userQuery = "QUEEN";

        searchViewModel.init(userQuery, Type.ARTIST);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        for(Searchable x: searchables){
            if(x.getName().equals(artistToBeFound))
                searchable = x;
        }


        Assert.assertEquals(artistToBeFound, searchable.getName());
    }

    @Test
    public void searchArtistLowerCase() throws InterruptedException {
        String artistToBeFound = "Iron Maiden";

        String userQuery = "iron maiden";

        searchViewModel.init(userQuery, Type.ARTIST);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        for(Searchable x: searchables){
            if(x.getName().equals(artistToBeFound))
                searchable = x;
        }

        Assert.assertEquals(artistToBeFound, searchable.getName());
    }

}
