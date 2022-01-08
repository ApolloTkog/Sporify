package com.myapp.sporify.auth;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.activities.auth.signup.SignUpViewModel;
import com.myapp.sporify.fragments.home.TopAlbumsViewModel;
import com.myapp.sporify.models.Album;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SignUpTest {

    private SignUpViewModel signUpViewModel;

    private String signUpResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        signUpViewModel = new SignUpViewModel();
    }

    @Test
    public void should_fill_username() throws InterruptedException {

        signUpViewModel.init("","","","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Username field cannot be empty!", signUpResponse);
    }

    @Test
    public void should_fill_valid_username() throws InterruptedException {

        signUpViewModel.init("use","","","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Username size must be greater than 3 chars!", signUpResponse);
    }

    @Test
    public void should_fill_email() throws InterruptedException {

        signUpViewModel.init("user","","","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Email field cannot be empty!", signUpResponse);
    }

    @Test
    public void should_fill_valid_email() throws InterruptedException {

        signUpViewModel.init("user","email@example","","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("You must enter a valid email format!", signUpResponse);
    }

    @Test
    public void should_fill_password() throws InterruptedException {

        signUpViewModel.init("user","email@example.com","","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Password field cannot be empty!", signUpResponse);
    }

    @Test
    public void should_fill_valid_password() throws InterruptedException {

        signUpViewModel.init("user","email@example.com","12345","");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Password size must 6 chars or greater!", signUpResponse);
    }

    @Test
    public void should_fill_confirm_password() throws InterruptedException {

        signUpViewModel.init("user","email@example.com","123456","12345");
        signUpViewModel.getSignUpResponse().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String response) {
                signUpResponse = response;
            }
        });

        Thread.sleep(2000);

        Assert.assertEquals("Passwords doesn't match!", signUpResponse);
    }

}


