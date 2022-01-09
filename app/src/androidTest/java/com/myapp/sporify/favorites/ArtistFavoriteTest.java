package com.myapp.sporify.favorites;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.activities.artist.FavoriteArtistViewModel;
import com.myapp.sporify.activities.auth.login.LoginViewModel;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Artist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class ArtistFavoriteTest {

    LibraryViewModel libraryViewModel;
    FavoriteArtistViewModel favoriteArtistViewModel;
    LoginViewModel loginViewModel;
    String loginResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        libraryViewModel = new LibraryViewModel();
        favoriteArtistViewModel = new FavoriteArtistViewModel();
        loginViewModel = new LoginViewModel();

        loginViewModel.init("admin", "12345678");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());
    }

    @Test
    public void should_get_favorite_artist_list() throws InterruptedException {
        List<Artist> favoriteArtists;

        libraryViewModel.init(loginResponse);
        favoriteArtists = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteArtists());

        Assert.assertNotNull(favoriteArtists);
    }

    @Test
    public void should_add_or_delete_favorite_artist() throws InterruptedException {
        List<Artist> favoriteAlbums;

        libraryViewModel.init(loginResponse);
        favoriteAlbums = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteArtists());
        Assert.assertNotNull(favoriteAlbums);

        int randomMbid = new Random().nextInt();
        Artist mockArtist = Mockito.spy(Artist.class);

        mockArtist.setName("test artist name");
        Mockito.verify(mockArtist).setName("test artist name");


        boolean exists = false;
        for(Artist x: favoriteAlbums){
            if(x.getName().equals(mockArtist.getName())){
                exists = true;
                break;
            }
        }

        String response =
                LiveDataTestUtil.getOrAwaitValue(
                        favoriteArtistViewModel.toggleFavoriteArtist(String.valueOf(randomMbid), "", loginResponse, mockArtist));

        if(!exists)
            Assert.assertEquals("Successfully added!", response);
        else
            Assert.assertTrue(response.contains("successfully deleted!"));

    }
}
