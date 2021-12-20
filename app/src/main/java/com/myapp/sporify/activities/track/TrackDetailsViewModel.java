package com.myapp.sporify.activities.track;

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
import com.myapp.sporify.mappers.ArtistMapper;
import com.myapp.sporify.mappers.TrackMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.models.Track;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackDetailsViewModel extends ViewModel {


    private MutableLiveData<Track> trackLiveData;
    private Track track;

    private RequestQueue requestQueue;

    public TrackDetailsViewModel() {
        track = new Track();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();

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


        String url =
                "http://ws.audioscrobbler.com/2.0/?method=track.getinfo&mbid=" + mbid+ "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONObject jsonObject = new JSONObject();

            try {

                track = TrackMapper.getTrackFromJson(response);

                callBack.onSuccess(track.getArtistMbid());

                trackLiveData.setValue(track);
                trackLiveData.postValue(track);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                try {
                    reFetchTrackInfo(jsonObject, callBack);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

        }, error -> {
            trackLiveData.setValue(null);
            trackLiveData.postValue(null);
        });

        requestQueue.add(jsonObjectRequest).addMarker("a");

        return TrackData;

    }

    private void reFetchTrackInfo(JSONObject jsonObject, VolleyCallBackAlt callBack) throws JSONException {
        String name = jsonObject.getString("name");
        String image = jsonObject.getJSONObject("album").getJSONArray("image").getJSONObject(3).getString("#text");
        String artistName = jsonObject.getJSONObject("artist").getString("name");
        String artistMbid = jsonObject.getJSONObject("artist").getString("mbid");

        track = new Track(name, image, artistName, artistMbid, "No summary for this track", "No more content for this track");

        callBack.onSuccess(artistMbid);
    }

    private LiveData<Track> getArtistImage(String mbid){
        final MutableLiveData<Track> trackData = new MutableLiveData<>();

        String url =
                "http://webservice.fanart.tv/v3/music/"+ mbid +"?api_key=1e325be5bfa7db0c79aa464a0a924a46";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String imageURL = ArtistMapper.getArtistImage(response);

                if(!imageURL.isEmpty()){
                    track.setArtistImageURL(imageURL);
                }

                trackLiveData.setValue(track);
                trackLiveData.postValue(track);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
//                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
//                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", "Error getting artist's image");
        });

        requestQueue.add(jsonObjectRequest);

        return trackData;
    }
}
