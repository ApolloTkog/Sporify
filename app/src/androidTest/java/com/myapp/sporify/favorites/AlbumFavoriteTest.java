package com.myapp.sporify.favorites;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.activities.album.FavoriteAlbumViewModel;
import com.myapp.sporify.activities.auth.login.LoginViewModel;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.Album;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class AlbumFavoriteTest {

    LibraryViewModel libraryViewModel;
    FavoriteAlbumViewModel favoriteAlbumViewModel;
    LoginViewModel loginViewModel;
    String loginResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        libraryViewModel = new LibraryViewModel();
        favoriteAlbumViewModel = new FavoriteAlbumViewModel();
        loginViewModel = new LoginViewModel();

        loginViewModel.init("username", "12345678");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());
    }

    @Test
    public void should_get_favorite_album_list() throws InterruptedException {
        List<Album> favoriteAlbums;

        libraryViewModel.init(loginResponse);
        favoriteAlbums = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteAlbums());

        Assert.assertNotNull(favoriteAlbums);
    }

    @Test
    public void should_add_or_delete_favorite_album() throws InterruptedException {
        List<Album> favoriteAlbums;

        libraryViewModel.init(loginResponse);

        // get favorite albums and assert that is not null
        favoriteAlbums = LiveDataTestUtil.getOrAwaitValue(libraryViewModel.getFavoriteAlbums());
        Assert.assertNotNull(favoriteAlbums);

        // create mock album
        int randomMbid = new Random().nextInt();
        Album mockAlbum = Mockito.spy(Album.class);

        mockAlbum.setName("test name");
        Mockito.verify(mockAlbum).setName("test name");

        mockAlbum.setArtistName("test artist");
        Mockito.verify(mockAlbum).setArtistName("test artist");

        // check if this mock album exists inside favorite collection
        boolean exists = false;
        for(Album x: favoriteAlbums){
            if(x.getName().equals(mockAlbum.getName()) && x.getArtistName().equals(mockAlbum.getArtistName())){
                exists = true;
                break;
            }
        }

        // add this mock album to favorites
        String response =
                LiveDataTestUtil.getOrAwaitValue(favoriteAlbumViewModel.addFavoriteAlbum(String.valueOf(randomMbid), loginResponse, mockAlbum));

        // check if api manages correct to add or delete it
        if(!exists)
            Assert.assertEquals("Successfully added!", response);
        else
            Assert.assertTrue(response.contains("successfully deleted!"));

    }

}
