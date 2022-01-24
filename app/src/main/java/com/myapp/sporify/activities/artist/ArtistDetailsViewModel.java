package com.myapp.sporify.activities.artist;

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
import com.myapp.sporify.mappers.ArtistMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsViewModel extends ViewModel {

    private LiveData<Artist> artistLiveData;
    private LiveData<List<Track>> tracksLiveData;
    private LiveData<List<Album>> albumsLiveData;


    private Artist artist;
    private List<Track> tracks;
    private List<Album> albums;


    private MutableLiveData<List<Track>> tracksData;



    String id;

    private RequestQueue requestQueue;


    public ArtistDetailsViewModel() {
        artist = new Artist();
        albums= new ArrayList<>();
        tracks= new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String id){
        tracks.clear();
        albums.clear();
        this.id = id;

        artistLiveData = getArtistInfo(id);
        tracksLiveData = getTracks(id);
        albumsLiveData = getAlbums(id);
    }

    public LiveData<Artist> getArtist() {
        return artistLiveData;
    }

    public LiveData<List<Track>> getArtistTracks(){
        if (tracksLiveData == null) {
            tracksLiveData = new MutableLiveData<>();
            getTracks(id);
        }
        return tracksLiveData;
    }

    public LiveData<List<Album>> getArtistAlbums(){
        return albumsLiveData;
    }

    private LiveData<Artist> getArtistInfo(String id){
        final MutableLiveData<Artist> artistData = new MutableLiveData<>();

        String url =
                "https://theaudiodb.com/api/v1/json/2/artist.php?i=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                artist = ArtistMapper.getArtistFromJson(response);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                artistData.setValue(artist);
                artistData.postValue(artist);
            }

        }, error -> {
            Log.d("Request error: ", error.toString());
        });

        requestQueue.add(jsonObjectRequest);

        return artistData;
    }

    public LiveData<List<Track>> getTracks(String id){
        final MutableLiveData<List<Track>> artistTracks = new MutableLiveData<>();

        String url =
                "https://theaudiodb.com/api/v1/json/2/mvid.php?i=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                tracks = ArtistMapper.getArtistTracksFromJson(response);


            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                artistTracks.setValue(tracks);
                artistTracks.postValue(tracks);
            }

        }, error -> {
            Log.d("Request error: ", error.toString());
        });

        requestQueue.add(jsonObjectRequest);

        return artistTracks;
    }

    private LiveData<List<Album>> getAlbums(String id){
        final MutableLiveData<List<Album>> albumsData = new MutableLiveData<>();

        System.out.println("ID IS" + id);

        String url =
                "http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&mbid=" + id + "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                albums = ArtistMapper.getArtistAlbumsFromJson(response);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                // Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {

                albumsData.setValue(albums);
                albumsData.postValue(albums);
            }
        }, error -> {
            // Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.toString());
        });

        requestQueue.add(jsonObjectRequest);

        return albumsData;
    }


}