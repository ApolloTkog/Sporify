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
import com.myapp.sporify.interfaces.VolleyCallBack;
import com.myapp.sporify.mappers.FavoriteMapper;
import com.myapp.sporify.mappers.PlaylistMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Playlist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.Const;
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
    private LiveData<List<Playlist>> playlists;

    private LiveData<String> playlistTrackResponse;
    private LiveData<String> playlistCreateResponse;
    private LiveData<String> playlistDeleteResponse;
    private LiveData<String> playlistTrackDeleteResponse;

    private List<Album> albumList;
    private List<Artist> artistList;
    private List<Track> trackList;
    private List<Playlist> playlistList;


    private RequestQueue requestQueue;


    public LibraryViewModel() {
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String accessToken){
        albumList = new ArrayList<>();
        artistList = new ArrayList<>();
        trackList = new ArrayList<>();
        playlistList = new ArrayList<>();

        favoriteAlbums = getFavoriteAlbums(accessToken);
        favoriteArtists = getFavoriteArtists(accessToken);
        favoriteTracks = getFavoriteTracks(accessToken);
        playlists = getPlaylists(accessToken);
    }

    public void send(String playlistId, String mbid, String accessToken, Track track){
        playlistTrackResponse = addTrackToPlaylist(playlistId, mbid, accessToken, track);
    }

    public void create(String accessToken, String name){
        playlistCreateResponse = createPlaylist(accessToken, name, () -> {
            playlists = getPlaylists(accessToken);
        });
    }

    public void delete(String accessToken, String id){
        playlistDeleteResponse = deletePlaylist(accessToken, id);
    }

    public void deleteTrack(String accessToken, String playlistId, String trackId){
        playlistTrackDeleteResponse = deleteTrackFromPlaylist(playlistId, trackId, accessToken);
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

    public LiveData<List<Playlist>> getPlaylists() {
        return playlists;
    }

    public LiveData<String> getPlaylistCreateResponse() {
        return playlistCreateResponse;
    }

    public LiveData<String> getPlaylistDeleteResponse() {
        return playlistDeleteResponse;
    }

    public LiveData<String> getPlaylistTrackDeleteResponse() {
        return playlistTrackDeleteResponse;
    }

    public LiveData<String> getPlaylistTrackResponse() {
        return playlistTrackResponse;
    }

    public LiveData<List<Album>> getFavoriteAlbums(String accessToken){
        final MutableLiveData<List<Album>> responseData = new MutableLiveData<>();

        String url = Const.DB_URL + "/api/user/albums";
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
                System.out.println(error.toString());
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

    public LiveData<List<Artist>> getFavoriteArtists(String accessToken){
        final MutableLiveData<List<Artist>> responseData = new MutableLiveData<>();

        String url = Const.DB_URL + "/api/user/artists";
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
//                System.out.println(error.toString());
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

        String url = Const.DB_URL + "/api/user/tracks";
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
                System.out.println(error.toString());
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

    public LiveData<String> createPlaylist(String accessToken, String name, VolleyCallBack callBack){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/playlist";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());

                try{
                    String accessToken = response.getString("message");
                    responseData.postValue(accessToken);
                    responseData.setValue(accessToken);

                    callBack.onSuccess();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
                String message = parseVolleyError(error);
                responseData.postValue(message);
                responseData.setValue(message);
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

    public LiveData<String> deletePlaylist(String accessToken, String id){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/playlist/" + id;
        JSONObject postData = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());

                try{
                    String accessToken = response.getString("message");
                    responseData.postValue(accessToken);
                    responseData.setValue(accessToken);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
                String message = parseVolleyError(error);
                responseData.postValue(message);
                responseData.setValue(message);
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

    public LiveData<List<Playlist>> getPlaylists(String accessToken){
        final MutableLiveData<List<Playlist>> responseData = new MutableLiveData<>();

        String url = Const.DB_URL + "/api/user/playlists";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    playlistList = PlaylistMapper.getPlaylistsFromJson(response);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(playlistList);
                responseData.setValue(playlistList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
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

    public LiveData<String> addTrackToPlaylist(String playlistId, String mbid, String accessToken, Track track){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/playlist/" + playlistId;
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", track.getName());
            postData.put("artist", track.getArtistName());
            postData.put("imageURL", track.getImageURL());
            postData.put("youtubeUrl", track.getYoutubeURL());
            postData.put("musicBrainzId",mbid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());

                try{
                    String accessToken = response.getString("message");
                    responseData.postValue(accessToken);
                    responseData.setValue(accessToken);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
                String message = parseVolleyError(error);
                responseData.postValue(message);
                responseData.setValue(message);
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

    public LiveData<String> deleteTrackFromPlaylist(String playlistId, String trackId, String accessToken){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/playlist/track/" + trackId;
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", playlistId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());

                try{
                    String accessToken = response.getString("message");
                    responseData.postValue(accessToken);
                    responseData.setValue(accessToken);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
                String message = parseVolleyError(error);
                responseData.postValue(message);
                responseData.setValue(message);
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