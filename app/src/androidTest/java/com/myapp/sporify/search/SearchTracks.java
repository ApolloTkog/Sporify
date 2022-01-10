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
 * Test the SearchTracks functionality
 *
 */
@RunWith(AndroidJUnit4.class)
public class SearchTracks {

    private SearchViewModel searchViewModel;
    private Searchable searchable = new Searchable();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUP(){
        searchViewModel = new SearchViewModel();
    }

    @Test
    public void searchTrackFound() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Smells Like Teen Spirit";

        searchViewModel.init(queryToBeFound, Type.TRACK);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());
        // if our query exists in results
        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        Assert.assertEquals(queryToBeFound, searchable.getName());

    }

    @Test
    public void searchTrackCapitalCase() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Smells Like Teen Spirit";
        String searchedQuery = "SMELLS LIKE TEEN SPIRIT";

        searchViewModel.init(searchedQuery, Type.TRACK);

        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());


        // if our query exists in results
        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }
        Assert.assertEquals(queryToBeFound, searchable.getName());
    }

    @Test
    public void searchTrackLowerCase() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Smells Like Teen Spirit";
        String searchedQuery = "smells like teen spirit";

        searchViewModel.init(searchedQuery, Type.TRACK);

        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if our query exists in results
        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        Assert.assertEquals(queryToBeFound, searchable.getName());
    }

    @Test
    public void searchTrackCorrection() throws InterruptedException {
        // track that user wants to search
        String trackIWantToSearch = "Bohemian Rhapsody";

        // query that user types in search input
        String userQuery = "bohemian";

        searchViewModel.init(userQuery, Type.TRACK);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());
        // if track that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(trackIWantToSearch))
                searchable = x;
        }


        Assert.assertEquals(trackIWantToSearch, searchable.getName());

    }

    @Test
    public void searchTrackAndArtist() throws InterruptedException {
        // track that user wants to search
        String trackIWantToSearch = "Bohemian Rhapsody";
        String trackArtist = "Queen";

        // query that user types in search input
        String userQuery = "bohemian rhapsody queen";

        searchViewModel.init(userQuery, Type.TRACK);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if track that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(trackIWantToSearch) && x.getArtistName().equals(trackArtist))
                searchable = x;
        }

        Assert.assertEquals(trackIWantToSearch, searchable.getName());

    }

    @Test
    public void searchTrackSpellWrong() throws InterruptedException {

        // track that user wants to search
        String trackIWantToSearch = "Smells Like Teen Spirit";

        // query that user types in search input
        String userQuery = "smell like teen spirit";

        searchViewModel.init(userQuery, Type.TRACK);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        // if track that user wants to search is in the list
        for(Searchable x: searchables){
            if(x.getName().equals(trackIWantToSearch))
                searchable = x;
        }


        Assert.assertNotEquals(trackIWantToSearch, searchable.getName());

    }

    @Test
    public void searchTrackNotFound() throws InterruptedException {

        String queryToBeFound = "Nothing to be found";

        searchViewModel.init(queryToBeFound, Type.TRACK);
        List<Searchable> searchables = LiveDataTestUtil.getOrAwaitValue(searchViewModel.getDataSearch());

        for(Searchable x: searchables){
            if(x.getName().equals(queryToBeFound))
                searchable = x;
        }

        Assert.assertNotEquals(queryToBeFound, searchable.getName());
    }


}
