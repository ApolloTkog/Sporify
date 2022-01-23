package com.myapp.sporify.auth;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.myapp.sporify.LiveDataTestUtil;
import com.myapp.sporify.activities.auth.login.LoginViewModel;
import com.myapp.sporify.activities.auth.signup.SignUpViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private LoginViewModel loginViewModel;

    private String loginResponse;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        loginViewModel = new LoginViewModel();
    }

    @Test
    public void should_not_login() throws InterruptedException {

        loginViewModel.init("","");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());

        Assert.assertEquals("Bad credentials!", loginResponse);
    }

    @Test
    public void should_not_login_random_credentials() throws InterruptedException {

        loginViewModel.init("user","12345");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());

        Assert.assertEquals("Bad credentials!", loginResponse);
    }

    @Test
    public void should_login() throws InterruptedException {

        loginViewModel.init("username","12345678");
        loginResponse = LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResponse());

        Assert.assertNotEquals("Bad credentials!", loginResponse);
    }
}
