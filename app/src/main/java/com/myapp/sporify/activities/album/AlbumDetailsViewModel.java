package com.myapp.sporify.activities.album;

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
import com.myapp.sporify.interfaces.VolleyCallBackAlt;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsViewModel extends AndroidViewModel {

    private MutableLiveData<Album> albumLiveData;
    private Album album;

    private RequestQueue requestQueue;

    public AlbumDetailsViewModel(@NonNull Application application) {
        super(application);
        album = new Album();
        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();
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
    public LiveData<Album> getAlbums(String mbid){
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
            JSONObject jsonObject;

            try {
                // from the json response get the album object
                jsonObject = response.getJSONObject("album");

                // from the album object get name, image, artist
                String name = jsonObject.getString("name");
                String image = jsonObject.getJSONArray("image").getJSONObject(3).getString("#text");
                String artistName = jsonObject.getString("artist");

                String artistMbid = "";

                List<Track> tracksList = new ArrayList<>();

                // from the object tracks get the json array called track
                JSONArray tracks = jsonObject.getJSONObject("tracks").getJSONArray("track");

                // parse the json array of tracks
                for (int i = 0; i < tracks.length(); i++) {
                    JSONObject trackObject = tracks.getJSONObject(i);

                    // get track's name
                    String trackName = trackObject.getString("name");
                    // get track's duration
                    int trackDuration = trackObject.getInt("duration");
                    //get artist's mbid
                    artistMbid = trackObject.getJSONObject("artist").getString("mbid");

                    // create track object
                    Track track = new Track(trackName, artistName, trackDuration);

                    // add it to list
                    tracksList.add(track);
                }

                // create album object
                album = new Album(mbid, name, artistName, image, tracksList);
                album.setArtistMbid(artistMbid);

                // call the callback function to know when we have fetched all the data that we want
                callBack.onSuccess(artistMbid);



            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            finally {
                // post && set the album's values
                albumLiveData.setValue(album);
                albumLiveData.postValue(album);
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d("Request error: ", error.getMessage());
            // on request error post the empty album object to show default values
            albumLiveData.setValue(album);
            albumLiveData.postValue(album);
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
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", "Error getting artist's image");
        });

        // add request to Volley queue to called
        requestQueue.add(jsonObjectRequest);

    }

}
