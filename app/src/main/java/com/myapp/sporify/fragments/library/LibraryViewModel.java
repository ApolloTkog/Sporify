package com.myapp.sporify.fragments.library;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.FavoriteMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryViewModel extends ViewModel {

    private LiveData<List<Album>> favoriteAlbums;
    private LiveData<List<Artist>> favoriteArtists;
    private LiveData<List<Track>> favoriteTracks;


    private List<Album> albumList;
    private List<Artist> artistList;
    private List<Track> trackList;


    private RequestQueue requestQueue;


    public LibraryViewModel() {
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String accessToken){
        albumList = new ArrayList<>();
        artistList = new ArrayList<>();
        trackList = new ArrayList<>();

        favoriteAlbums = getFavoriteAlbums(accessToken);
        favoriteArtists = getFavoriteArtists(accessToken);
        favoriteTracks = getFavoriteTracks(accessToken);
    }


    public LiveData<List<Album>> getFavoriteAlbums() {
        return favoriteAlbums;
    }

    public LiveData<List<Artist>> getFavoriteArtists() {
        return favoriteArtists;
    }

    public LiveData<List<Track>> getFavoriteTracks() {
        return favoriteTracks;
    }


    public LiveData<List<Album>> getFavoriteAlbums(String accessToken){
        final MutableLiveData<List<Album>> responseData = new MutableLiveData<>();

        String url = "http://192.168.2.5:8081/api/user/albums";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    albumList = FavoriteMapper.getFavoriteAlbumsFromJson(response);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(albumList);
                responseData.setValue(albumList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.d("VOLLEY ERROR: ", error.getMessage());
//                String message = parseVolleyError(error);
//                responseData.postValue(message);
//                responseData.setValue(message);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);

        return responseData;
    }

    public LiveData<List<Artist>> getFavoriteArtists(String accessToken){
        final MutableLiveData<List<Artist>> responseData = new MutableLiveData<>();

        String url = "http://192.168.2.5:8081/api/user/artists";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    artistList = FavoriteMapper.getFavoriteArtistsFromJson(response);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(artistList);
                responseData.setValue(artistList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.getMessage());
                Log.d("VOLLEY ERROR: ", error.toString());
//                String message = parseVolleyError(error);
//                responseData.postValue(message);
//                responseData.setValue(message);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);

        return responseData;
    }

    public LiveData<List<Track>> getFavoriteTracks(String accessToken){
        final MutableLiveData<List<Track>> responseData = new MutableLiveData<>();

        String url = "http://192.168.2.5:8081/api/user/tracks";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    trackList = FavoriteMapper.getFavoriteTracksFromJson(response);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(trackList);
                responseData.setValue(trackList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.d("VOLLEY ERROR: ", error.getMessage());
//                String message = parseVolleyError(error);
//                responseData.postValue(message);
//                responseData.setValue(message);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);

        return responseData;
    }

    public String parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            JSONObject data = new JSONObject(responseBody);
            return data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}