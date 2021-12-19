package com.myapp.sporify.fragments.home;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.TrackMapper;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopTracksViewModel extends ViewModel {
    private MutableLiveData<List<Track>> tracks;


    private List<Track> trackList;

    private RequestQueue requestQueue;

    public TopTracksViewModel() {
        trackList = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }


    public LiveData<List<Track>> getTopTracks(int limit){
        if (tracks == null) {
            tracks = new MutableLiveData<>();
            fetchTopTracks(limit);
        }
        return tracks;
    }

    private void fetchTopTracks(int limit) {
        String url =
                "http://ws.audioscrobbler.com/2.0/?format=json&method=chart.gettoptracks&limit=" + limit +"&api_key=8fc89f699e4ff45a21b968623a93ed52";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                trackList = TrackMapper.getTopTracksFromJson(response);

                tracks.postValue(trackList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
            }
        }, error -> {
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);

    }
}