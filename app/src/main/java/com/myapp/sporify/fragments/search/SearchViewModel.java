package com.myapp.sporify.fragments.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Type;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private LiveData<List<Searchable>> searchLiveData;
    private List<Searchable> albumList, artistList, trackList;

    private RequestQueue requestQueue;

    public SearchViewModel() {
        albumList = new ArrayList<>();
        artistList = new ArrayList<>();
        trackList = new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String query, Type type) {
        clearResults();

        switch (type){
            case ALBUM:
                searchLiveData = searchInAlbums(query);
                break;
            case ARTIST:
                searchLiveData = searchInArtists(query);
                break;
            case TRACK:
                searchLiveData = searchInTracks(query);
                break;
        }
    }

    public LiveData<List<Searchable>> getDataSearch() {
        return searchLiveData;
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

                    Searchable searchable = new Searchable(mbid, name, artistName, image, Type.ALBUM);
                    albumList.add(searchable);
                }

//                Toast.makeText(getApplication(), "Searching albums!", Toast.LENGTH_SHORT).show();

                searchData.postValue(albumList);
                searchData.setValue(albumList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.toString());
        });

        requestQueue.add(jsonObjectRequest).addMarker("a");

        return searchData;
    }

    public LiveData<List<Searchable>> searchInArtists(String query){
        final MutableLiveData<List<Searchable>> searchData = new MutableLiveData<>();

        String url =
                "https://www.theaudiodb.com/api/v1/json/2/search.php?s=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("artists");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    if(jsonObject.getString("mbid").isEmpty())
//                        continue;

                    String mbid = jsonObject.getString("strMusicBrainzID");
                    String id = jsonObject.getString("idArtist");
                    String name = jsonObject.getString("strArtist");
                    String image = jsonObject.getString("strArtistThumb").equals("null") ? "" : jsonObject.getString("strArtistThumb");

                    Searchable searchable = new Searchable(id, name, "Artist", image, Type.ARTIST);
                    searchable.setExtraId(mbid);
                    artistList.add(searchable);
                }


//                searchData.postValue(artistList);
//                searchData.setValue(artistList);

//                Toast.makeText(getApplication(), "Searching artists!", Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            finally {
                searchData.postValue(artistList);
                searchData.setValue(artistList);
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.toString());
            searchData.postValue(artistList);
            searchData.setValue(artistList);
        });

        requestQueue.add(jsonObjectRequest);

        return searchData;
    }

    public  LiveData<List<Searchable>> searchInTracks(String query){
        final MutableLiveData<List<Searchable>> searchData = new MutableLiveData<>();

        String url =
                "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + query + "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject results = response.getJSONObject("results").getJSONObject("trackmatches");
                JSONArray jsonArray = results.getJSONArray("track");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("mbid").isEmpty())
                        continue;

                    String mbid = jsonObject.getString("mbid");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
                    String artistName = jsonObject.getString("artist");

                    Searchable searchable = new Searchable(mbid, name, artistName, image, Type.TRACK);
                    trackList.add(searchable);
                }

//                Toast.makeText(getApplication(), "Searching tracks!", Toast.LENGTH_SHORT).show();

                searchData.postValue(trackList);
                searchData.setValue(trackList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.toString());
        });

        requestQueue.add(jsonObjectRequest).addMarker("a");

        return searchData;
    }

    private void clearResults(){
        albumList.clear();
        artistList.clear();
        trackList.clear();
    }
}