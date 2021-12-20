package com.myapp.sporify.fragments.home;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.mappers.ArtistMapper;
import com.myapp.sporify.utils.VolleySingleton;
import com.myapp.sporify.interfaces.ApiService;
import com.myapp.sporify.interfaces.VolleyCallBack;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Track>> tracks;


    private List<Track> trackList;

    private RequestQueue requestQueue;


    public HomeViewModel(Application application ) {
        super(application);

    }









}