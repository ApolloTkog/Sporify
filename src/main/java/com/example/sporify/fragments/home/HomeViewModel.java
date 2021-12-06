package com.example.sporify.fragments.home;

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
import com.example.sporify.utils.VolleySingleton;
import com.example.sporify.interfaces.ApiService;
import com.example.sporify.interfaces.VolleyCallBack;
import com.example.sporify.models.Album;
import com.example.sporify.models.Artist;
import com.example.sporify.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends AndroidViewModel implements ApiService {

    public MutableLiveData<List<Artist>> artists;
    private MutableLiveData<List<Album>> albums;
    private MutableLiveData<List<Track>> tracks;

    private List<Artist> artistList;
    private List<Album> albumList;
    private List<Track> trackList;

    private RequestQueue requestQueue;


    public HomeViewModel(Application application ) {
        super(application);
        
        albumList = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();
    }
    

    @Override
    public LiveData<List<Album>> getTopAlbums(){
        if (albums == null) {
            albums = new MutableLiveData<>();
            fetchTopAlbums();
        }
        return albums;
    }
    
    
    private void fetchTopAlbums() {

        String url = "http://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=rock&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json&limit=20";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject artists_json = response.getJSONObject("albums");
                JSONArray jsonArray = artists_json.getJSONArray("album");
                
                for (int i = 0; i < jsonArray.length(); i++) {
                    
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("mbid").isEmpty()) 
                        continue;

                    String mbid = jsonObject.getString("mbid");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
                    String artistName = jsonObject.getJSONObject("artist").getString("name");

                    Album album = new Album(mbid, name, artistName, image);
                    albumList.add(album);
                }

                albums.postValue(albumList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);
    }