package com.myapp.sporify.favorites;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.activities.auth.login.LoginViewModel;
import com.myapp.sporify.activities.track.FavoriteTrackViewModel;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Track;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class TrackFavoriteTest {

    LibraryViewModel libraryViewModel;
    FavoriteTrackViewModel favoriteTrackViewModel;
    LoginViewModel loginViewModel;
    String loginResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        libraryViewModel = new LibraryViewModel();
        favoriteTrackViewModel = new FavoriteTrackViewModel();
        loginViewModel = new LoginViewModel();

        loginViewModel.init("username", "12345678");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());
    }

    @Test
    public void should_get_favorite_track_list() throws InterruptedException {
        List<Track> favoriteTracks;

        libraryViewModel.init(loginResponse);
        favoriteTracks = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteTracks());

        Assert.assertNotNull(favoriteTracks);
    }

    @Test
    public void should_add_or_delete_favorite_artist() throws InterruptedException {
        List<Track> favoriteTracks;

        libraryViewModel.init(loginResponse);
        favoriteTracks = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteTracks());
        Assert.assertNotNull(favoriteTracks);

        int randomMbid = new Random().nextInt();
        Track mockTrack = Mockito.spy(Track.class);

        mockTrack.setName("test track name");
        Mockito.verify(mockTrack).setName("test track name");

        mockTrack.setArtistName("test artist track name");
        Mockito.verify(mockTrack).setArtistName("test artist track name");

        boolean exists = false;
        for(Track x: favoriteTracks){
            if(x.getName().equals(mockTrack.getName()) && x.getArtistName().equals(mockTrack.getArtistName())){
                exists = true;
                break;
            }
        }

        String response =
                LiveDataTestUtil.getOrAwaitValue(
                        favoriteTrackViewModel.toggleFavoriteTrack(String.valueOf(randomMbid), loginResponse, mockTrack));

        if(!exists)
            Assert.assertEquals("Successfully added!", response);
        //else
           // Assert.assertTrue(response.contains("successfully deleted!"));

    }
}
