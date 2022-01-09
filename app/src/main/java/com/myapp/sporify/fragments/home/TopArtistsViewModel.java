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
import com.myapp.sporify.interfaces.VolleyCallBack;
import com.myapp.sporify.mappers.ArtistMapper;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TopArtistsViewModel extends ViewModel {

    private List<Artist> artistList;
    public MutableLiveData<List<Artist>> artists;

    private RequestQueue requestQueue;

    public TopArtistsViewModel() {

        artistList = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();

    }

    public LiveData<List<Artist>> getTopArtists(int limit) {
        if (artists == null) {
            artists = new MutableLiveData<>();
            fetchTopArtistsImage(limit);
        }
        return artists;
    }

    private void fetchTopArtistsInfo(int limit, final VolleyCallBack callBack) {

        String url = "http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit="+ limit +"&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                artistList = ArtistMapper.getTopArtistsFromJson(response);

                callBack.onSuccess();

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                // Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                artists.setValue(artistList);
                artists.postValue(artistList);
            }
        }, error -> {
            // Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchArtistImage() {

        for(int i = 0; i < artistList.size(); i++){
            String url =
                    "http://webservice.fanart.tv/v3/music/"+ artistList.get(i).getMbid() +"?api_key=1e325be5bfa7db0c79aa464a0a924a46";

            int finalI = i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {

                    String imageURL = ArtistMapper.getArtistImage(response);

                    artistList.get(finalI).setImageURL(imageURL);

                    if(finalI >= artistList.size() - 1){
                        artists.setValue(artistList);
                        artists.postValue(artistList);
                    }

                } catch (JSONException e) {
                    Log.d("Parsing error: ", e.getMessage());
                    // Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, error -> {
//                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Request error: ", "Error getting artist's image");
                if(finalI >= artistList.size() - 1){
                    artists.setValue(artistList);
                    artists.postValue(artistList);
                }
            });

            requestQueue.add(jsonObjectRequest);

        }

    }

    private void fetchTopArtistsImage(int limit){
        fetchTopArtistsInfo(limit,this::fetchArtistImage);
    }
}
