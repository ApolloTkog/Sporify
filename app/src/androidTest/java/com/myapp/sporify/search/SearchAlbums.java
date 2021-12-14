package com.myapp.sporify.search;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
    public void setUP(){
        searchViewModel = new SearchViewModel();
    }

    @Test
    public void searchAlbumFound() throws InterruptedException {
        // query that is expected to be found
        String queryToBeFound = "Nevermind";

        searchViewModel.init(queryToBeFound, Type.ALBUM);
        searchViewModel.getDataSearch().observeForever(new Observer<List<Searchable>>() {
            @Override
            public void onChanged(List<Searchable> searchables) {
                // if we don't have results return;
                if(searchables.size() <= 0){
                    return;
                }
                // if our query exists in results
                for(Searchable x: searchables){
                    if(x.getName().equals(queryToBeFound))
                        searchable = x;
                }
            }
        });

        // wait 1s for items to be fetched
        Thread.sleep(1000);

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
        searchViewModel.getDataSearch().observeForever(new Observer<List<Searchable>>() {
            @Override
            public void onChanged(List<Searchable> searchables) {
                if(searchables.size() <= 0){
                    return;
                }

                // if album that user wants to search is in the list
                for(Searchable x: searchables){
                    if(x.getName().equals(albumIWantToSearch))
                        searchable = x;
                }


                // we can place assert here if we want to avoid null exception
                // Assert.assertEquals(albumIWantToSearch, searchable.getName());


            }
        });

        // wait 1s for results to be fetched
        Thread.sleep(1000);

        Assert.assertEquals(albumIWantToSearch, searchable.getName());

    }

    @Test
    public void searchAlbumSpellWrong() throws InterruptedException {

        // album that user wants to search
        String albumIWantToSearch = "A Night at the Opera";

        // query that user types in search input
        String userQuery = "a might at opera";

        searchViewModel.init(userQuery, Type.ALBUM);
        searchViewModel.getDataSearch().observeForever(new Observer<List<Searchable>>() {
            @Override
            public void onChanged(List<Searchable> searchables) {
                if(searchables.size() <= 0){
                    return;
                }

                // if album that user wants to search is in the list
                for(Searchable x: searchables){
                    if(x.getName().equals(albumIWantToSearch))
                        searchable = x;
                }


                // we can place assert here if we want to avoid null exception
                // Assert.assertEquals(albumIWantToSearch, searchable.getName());


            }
        });

        // wait 1s for results to be fetched
        Thread.sleep(1000);

        Assert.assertNotEquals(albumIWantToSearch, searchable.getName());

    }

    @Test
    public void searchAlbumNotFound() throws InterruptedException {

        String queryToBeFound = "Nothing to be found";

        searchViewModel.init(queryToBeFound, Type.ALBUM);
        searchViewModel.getDataSearch().observeForever(new Observer<List<Searchable>>() {
            @Override
            public void onChanged(List<Searchable> searchables) {
                if(searchables.size() <= 0){
                    return;
                }
                for(Searchable x: searchables){
                    if(x.getName().equals(queryToBeFound))
                        searchable = x;
                }
            }
        });

        Thread.sleep(1000);

        Assert.assertNotEquals(queryToBeFound, searchable.getName());
    }

}
