package com.myapp.sporify.activities.album;

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
import com.myapp.sporify.interfaces.VolleyCallBackAlt;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsViewModel extends ViewModel {

    private MutableLiveData<Album> albumLiveData;
    private Album album;

    private RequestQueue requestQueue;

    public AlbumDetailsViewModel() {
        album = new Album();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public LiveData<Album> getAlbum() {
        return albumLiveData;
    }

    /**
     *
     * Method that returns livedata with album's info
     *
     * @param mbid Album's MusicBrainz ID
     * @return LiveData with album's info
     */
    public LiveData<Album> getAlbum(String mbid){
        album = new Album();

        if (albumLiveData == null) {
            albumLiveData = new MutableLiveData<>();

            // getting the album info and when this is successful
            // fetch the artist's image given the id -> artistID
            getAlbumInfo(mbid, new VolleyCallBackAlt() {
                @Override
                public void onSuccess(String id) {
                    getArtistImage(id);
                }
            });
        }

        return albumLiveData;
    }

    /**
     *
     * Method that returns the API results with album's info
     *
     * @param mbid MusicBrainz's ID
     * @param callBack callback function to know when the data are fetched
     */
    private void getAlbumInfo(String mbid, final VolleyCallBackAlt callBack){

        final MutableLiveData<Album> albumData = new MutableLiveData<>();


        String url =
                "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&mbid=" + mbid+ "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//            JSONObject jsonObject;

            try {
                // from the json response get the album object
                album = AlbumMapper.getAlbumFromJson(response);

                // call the callback function to know when we have fetched all the data that we want
                callBack.onSuccess(album.getArtistMbid());

                albumLiveData.setValue(album);
                albumLiveData.postValue(album);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
            }
            finally {
                // post && set the album's values
                albumLiveData.setValue(null);
                albumLiveData.postValue(null);
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d("Request error: ", error.getMessage());
            // on request error post the empty album object to show default values
            albumLiveData.setValue(null);
            albumLiveData.postValue(null);
        });

        // add request to queue to called
        requestQueue.add(jsonObjectRequest);

    }

    /**
     *
     * Method that returns the artist's image given the artist's Mbid
     *
     * @param mbid MusicBrainz's id
     */
    private void getArtistImage(String mbid){
        final MutableLiveData<Album> albumData = new MutableLiveData<>();

        String url =
                "http://webservice.fanart.tv/v3/music/"+ mbid +"?api_key=1e325be5bfa7db0c79aa464a0a924a46";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                // get artisttumb object
                JSONArray artistThumbs = response.getJSONArray("artistthumb");

                // get the image's url
                String imageURL = artistThumbs.getJSONObject(0).getString("url");

                // if url is not empty set it to album object
                if(!imageURL.isEmpty()){
                    album.setArtistImageURL(imageURL);
                }

                // post && set the data we fetched
                albumLiveData.setValue(album);
                albumLiveData.postValue(album);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
            }
        }, error -> {
            Log.d("Request error: ", "Error getting artist's image");
        });

        // add request to Volley queue to called
        requestQueue.add(jsonObjectRequest);

    }

}