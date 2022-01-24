package com.myapp.sporify.details;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.activities.auth.login.LoginViewModel;
import com.myapp.sporify.activities.user.UserViewModel;
import com.myapp.sporify.fragments.library.LibraryViewModel;
import com.myapp.sporify.models.UserModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserTest {

    LibraryViewModel libraryViewModel;
    LoginViewModel loginViewModel;
    UserViewModel userViewModel;
    String loginResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        libraryViewModel = new LibraryViewModel();
        loginViewModel = new LoginViewModel();
        userViewModel = new UserViewModel();

        loginViewModel.init("test", "12345678");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());
    }

    // get user info

    @Test
    public void should_get_user_username() throws InterruptedException {

        userViewModel.init(loginResponse);
        UserModel userData = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserData());

        Assert.assertNotNull(userData);
        Assert.assertEquals("test", userData.getUsername());
    }

    @Test
    public void should_get_user_favorite_genre() throws InterruptedException {

        userViewModel.init(loginResponse);
        UserModel userData = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserData());

        Assert.assertNotNull(userData);
        Assert.assertFalse(userData.getGenre().isEmpty());
    }


    @Test
    public void should_get_user_current_mood() throws InterruptedException {

        userViewModel.init(loginResponse);
        UserModel userData = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserData());

        Assert.assertNotNull(userData);
        Assert.assertFalse(userData.getMood().isEmpty());
    }

    // get user info and check if data are changing

    @Test
    public void should_update_user_info() throws InterruptedException {

        userViewModel.init(loginResponse);
        UserModel userData = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserData());

        // assert that fetched are not empty
        Assert.assertNotNull(userData);
        Assert.assertFalse(userData.getGenre().isEmpty());
        Assert.assertFalse(userData.getMood().isEmpty());


        // update user's data
        userViewModel.update(loginResponse, "pop", "sad");
        String updateResponse = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserResponse());
        Assert.assertTrue(updateResponse.contains("successfully"));


        // fetch the updated data
        userViewModel.init(loginResponse);
        UserModel updatedUserData = LiveDataTestUtil.getOrAwaitValue(userViewModel.getUserData());


        // expected values
        Assert.assertEquals("pop", updatedUserData.getGenre());
        Assert.assertEquals("sad", updatedUserData.getMood());


        // revert to default values {genre: 'rock', mood: 'happy'}
        userViewModel.update(loginResponse, "rock", "happy");

    }



}
