package com.myapp.sporify.activities.album;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FavoriteAlbumViewModel extends ViewModel {

    private LiveData<String> favoriteAlbumResponse;


    private RequestQueue requestQueue;

    public FavoriteAlbumViewModel(){

        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String mbid,String accessToken, Album album){
        favoriteAlbumResponse = addFavoriteAlbum(mbid,accessToken, album);
    }

    public LiveData<String> getFavoriteAlbumResponse() {
        return favoriteAlbumResponse;
    }

    public LiveData<String> addFavoriteAlbum(String mbid,String accessToken, Album album){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = "http://192.168.1.2:8081/api/user/album";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", album.getName());
            postData.put("artist", album.getArtistName());
            postData.put("imageURL", album.getImageURL());
            postData.put("musicBrainzId", mbid);

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
                System.out.println(error.getMessage());
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
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            return data.getString("message");
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
