package com.myapp.sporify.fragments.search;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private LiveData<List<Searchable>> albumsLiveData;
    private List<Searchable> albumList;

    private RequestQueue requestQueue;

    public SearchViewModel(Application application) {
        super(application);
        albumList = new ArrayList<>();
       // artistList = new ArrayList<>();
       // trackList = new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();
    }

    public void init(String title, Type type) {
        albumList.clear();
      //  artistList.clear();
      //  trackList.clear();

        switch (type){
            case ALBUM:
                albumsLiveData = searchInAlbums(title);
                break;
            case ARTIST:
                albumsLiveData = searchInArtists(title);
                break;
            case TRACK:
                albumsLiveData = searchInTracks(title);
                break;
        }
    }

    public LiveData<List<Searchable>> getAlbumSearch() {
        return albumsLiveData;
    }

    public  LiveData<List<Searchable>> searchInAlbums(String query){
        final MutableLiveData<List<Searchable>> searchData = new MutableLiveData<>();

        String url =
                "http://ws.audioscrobbler.com/2.0/?method=album.search&album=" + query + "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject results = response.getJSONObject("results").getJSONObject("albummatches");
                JSONArray jsonArray = results.getJSONArray("album");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("mbid").isEmpty())
                        continue;

                    String mbid = jsonObject.getString("mbid");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
                    String artistName = jsonObject.getString("artist");

                    Searchable searchable = new Searchable(mbid, name, artistName, image, "album");
                    albumList.add(searchable);
                }

                Toast.makeText(getApplication(), "Searching albums!", Toast.LENGTH_SHORT).show();

                searchData.postValue(albumList);
                searchData.setValue(albumList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest).addMarker("a");

        return searchData;
    }

}