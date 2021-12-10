package com.myapp.sporify.fragments.home;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.interfaces.ApiService;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel implements ApiService {

    private MutableLiveData<List<Album>> albums;
    private MutableLiveData<List<Track>> tracks;
    private List<Album> albumList;
    private List<Track> trackList;

    private RequestQueue requestQueue;

    public HomeViewModel(Application application ) {
        super(application);
        trackList = new ArrayList<>();
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


    @Override
    public LiveData<List<Track>> getTopTracks(){
        if (tracks == null) {
            tracks = new MutableLiveData<>();
            fetchTopTracks();
        }
        return tracks;
    }

    private void fetchTopTracks() {
        String url =
                "http://ws.audioscrobbler.com/2.0/?format=json&method=chart.gettoptracks&api_key=8fc89f699e4ff45a21b968623a93ed52";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject artists_json = response.getJSONObject("tracks");
                JSONArray jsonArray = artists_json.getJSONArray("track");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

//                    String mbid = jsonObject.getString("mbid");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
                    String artistName = jsonObject.getJSONObject("artist").getString("name");

                    Track track = new Track(i + 1, name, artistName, image);
                    trackList.add(track);
                }

                tracks.postValue(trackList);

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
}