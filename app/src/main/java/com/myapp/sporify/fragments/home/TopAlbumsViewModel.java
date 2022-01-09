package com.myapp.sporify.fragments.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TopAlbumsViewModel extends ViewModel {

    private MutableLiveData<List<Album>> albums;
    private List<Album> albumList;

    private RequestQueue requestQueue;

    public TopAlbumsViewModel() {
        albumList = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public LiveData<List<Album>> getTopAlbums(int limit){
        if (albums == null) {
            albums = new MutableLiveData<>();
            fetchTopAlbums(limit);
        }
        return albums;
    }

    private void fetchTopAlbums(int limit) {

        String url =
                "http://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=rock&limit=" + limit +  "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                albumList = AlbumMapper.getTopAlbumsFromJson(response);

                albums.setValue(albumList);
                albums.postValue(albumList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                // Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);
    }
}
