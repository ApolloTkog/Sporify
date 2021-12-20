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
import com.myapp.sporify.interfaces.VolleyCallBack;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class HomeViewModel extends AndroidViewModel implements ApiService {

    public MutableLiveData<List<Artist>> artists;
    private MutableLiveData<List<Track>> tracks;

    private List<Artist> artistList;
    private List<Track> trackList;

    private RequestQueue requestQueue;

    public HomeViewModel(Application application ) {
        super(application);
        artistList = new ArrayList<>();
        trackList = new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();
    }

    @Override
    public LiveData<List<Artist>> getTopArtists() {
        if (artists == null) {
            artists = new MutableLiveData<>();
            fetchTopArtistsImage();
        }
        return artists;
    }

    private void fetchTopArtistsInfo(final VolleyCallBack callBack) {

        String url = "http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit=20&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject artists_json = response.getJSONObject("artists");
                JSONArray jsonArray = artists_json.getJSONArray("artist");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("mbid").isEmpty())
                        continue;

                    String mbid = jsonObject.getString("mbid");
                    String name = jsonObject.getString("name");
                    long playcount = jsonObject.getLong("playcount");
                    long listeners = jsonObject.getLong("listeners");

                    Artist artist = new Artist(mbid, name, "", playcount, listeners);
                    artistList.add(artist);
                }

                callBack.onSuccess();

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

    private void fetchArtistImage() {

        for(int i = 0; i <= artistList.size() - 1; i++){
            String url =
                    "http://webservice.fanart.tv/v3/music/"+ artistList.get(i).getMbid() +"?api_key=1e325be5bfa7db0c79aa464a0a924a46";

            int finalI = i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    JSONArray artistThumbs = response.getJSONArray("artistthumb");
                    String imageURL = artistThumbs.getJSONObject(0).getString("url");

                    artistList.get(finalI).setImageURL(imageURL);

                    if(finalI >= artistList.size() - 1){
                        //artists.postValue(artistList);
                        artists.setValue(artistList);
                    }


                } catch (JSONException e) {
                    Log.d("Parsing error: ", e.getMessage());
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, error -> {
//                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Request error: ", "Error getting artist's image");
            });

            requestQueue.add(jsonObjectRequest);

        }

    }

    private void fetchTopArtistsImage(){
        fetchTopArtistsInfo(this::fetchArtistImage);
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