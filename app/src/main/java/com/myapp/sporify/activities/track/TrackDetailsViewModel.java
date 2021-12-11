package com.myapp.sporify.activities.track;

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
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackDetailsViewModel extends AndroidViewModel {


    private MutableLiveData<Track> trackLiveData;
    private Track track;

    private RequestQueue requestQueue;

    public TrackDetailsViewModel(@NonNull Application application) {
        super(application);

        track = new Track();
        requestQueue = VolleySingleton.getmInstance(getApplication()).getRequestQueue();
    }

    public LiveData<Track> getTrack() {
        return trackLiveData;
    }

    public LiveData<Track> getTrackInfo(String mbid){
        track = new Track();

        if (trackLiveData == null) {
            trackLiveData = new MutableLiveData<>();
            getTrackInfo(mbid, new VolleyCallBackAlt() {
                @Override
                public void onSuccess(String id) {
                    getArtistImage(id);
                }
            });
        }
        return trackLiveData;
    }


    private LiveData<Track> getTrackInfo(String mbid, final VolleyCallBackAlt callBack){
        final MutableLiveData<Track> TrackData = new MutableLiveData<>();

        String url = "http://ws.audioscrobbler.com/2.0/?method=track.getinfo&mbid=" + mbid+ "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONObject jsonObject;
            try {
                jsonObject = response.getJSONObject("track");

                String name = jsonObject.getString("name");
                String image = jsonObject.getJSONObject("album").getJSONArray("image").getJSONObject(3).getString("#text");
                String artistName = jsonObject.getJSONObject("artist").getString("name");
                String artistMbid = jsonObject.getJSONObject("artist").getString("mbid");
                String summary = jsonObject.getJSONObject("wiki").getString("summary");
                String content = jsonObject.getJSONObject("wiki").getString("content");

                Log.d("ARTIST MBID", artistMbid);

                track = new Track(name, image, artistName, artistMbid, summary, content);

                callBack.onSuccess(artistMbid);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            finally {
                trackLiveData.setValue(track);
                trackLiveData.postValue(track);
            }

        }, error -> {
//            Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d("Request error: ", error.getMessage());
            trackLiveData.setValue(track);
            trackLiveData.postValue(track);
        });

        requestQueue.add(jsonObjectRequest).addMarker("a");

        return TrackData;

    }

    private LiveData<Track> getArtistImage(String mbid){
        final MutableLiveData<Track> trackData = new MutableLiveData<>();

        String url = "http://webservice.fanart.tv/v3/music/"+ mbid +"?api_key=1e325be5bfa7db0c79aa464a0a924a46";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray artistThumbs = response.getJSONArray("artistthumb");
                String imageURL = artistThumbs.getJSONObject(0).getString("url");

                if(!imageURL.isEmpty()){
                    track.setArtistImageURL(imageURL);
                }

                trackLiveData.setValue(track);
                trackLiveData.postValue(track);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", "Error getting artist's image");
        });

        requestQueue.add(jsonObjectRequest);

        return trackData;
    }
}