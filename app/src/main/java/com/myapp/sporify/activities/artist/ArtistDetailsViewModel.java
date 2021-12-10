package com.myapp.sporify.activities.artist;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sporify.models.Album;
import com.example.sporify.models.Artist;
import com.example.sporify.models.Searchable;
import com.example.sporify.models.Track;
import com.example.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsViewModel extends AndroidViewModel {

    private LiveData<Artist> artistLiveData;
    private LiveData<List<Track>> tracksLiveData;

    private Artist artist;
    private List<Track> tracks;

    private RequestQueue requestQueue;


    public ArtistDetailsViewModel(@NonNull Application application) {
        super(application);

        artist = new Artist();
        tracks= new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();

    }

    public void init(String id){
        tracks.clear();

        artistLiveData = getArtistInfo(id);
        tracksLiveData = getTracks(id);
    }

    public LiveData<Artist> getArtist() {
        return artistLiveData;
    }

    private LiveData<Artist> getArtistInfo(String id){
        final MutableLiveData<Artist> artistData = new MutableLiveData<>();

        String url =
                "https://theaudiodb.com/api/v1/json/2/artist.php?i=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("artists");

                Artist artist = new Artist();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

                    String mbid = jsonObject.getString("strMusicBrainzID");
                    String name = jsonObject.getString("strArtist");
                    String image = jsonObject.getString("strArtistFanart2").equals("null") ? "" : jsonObject.getString("strArtistFanart2");
                    String biography = jsonObject.getString("strBiographyEN");
                    String genre = jsonObject.getString("strGenre");


                    artist = new Artist(mbid, name, image, genre, biography);

                }


                artistData.postValue(artist);
                artistData.setValue(artist);


            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            finally {
                artistData.postValue(artist);
                artistData.setValue(artist);
            }

        }, error -> {
            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);

        return artistData;
    }

    public LiveData<List<Track>> getArtistTracks(){
        return tracksLiveData;
    }

    private LiveData<List<Track>> getTracks(String id){
        final MutableLiveData<List<Track>> artistTracks = new MutableLiveData<>();

        String url =
                "https://theaudiodb.com/api/v1/json/2/mvid.php?i=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("mvids");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

                    String name = jsonObject.getString("strTrack");
                    String image = jsonObject.getString("strTrackThumb").equals("null") ? "" : jsonObject.getString("strTrackThumb");
                    String youtubeURL = jsonObject.getString("strMusicVid");

                    Track track = new Track(i + 1, name,image, youtubeURL, "");

                    tracks.add(track);

                }

                artistTracks.postValue(tracks);
                artistTracks.setValue(tracks);


            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, error -> {
            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);

        return artistTracks;
    }
}


