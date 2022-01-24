package com.myapp.sporify.activities.artist;

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
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.utils.Const;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FavoriteArtistViewModel extends ViewModel {


    private LiveData<String> favoriteArtistsResponse;


    private RequestQueue requestQueue;

    public FavoriteArtistViewModel(){

        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public void init(String mbid,String imageURL, String accessToken, Artist artist){
        favoriteArtistsResponse = toggleFavoriteArtist(mbid,imageURL, accessToken, artist);
    }

    public LiveData<String> getFavoriteArtistsResponse() {
        return favoriteArtistsResponse;
    }

    public LiveData<String> toggleFavoriteArtist(String mbid, String imageURL,String accessToken, Artist artist){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/artist";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", artist.getName());
            postData.put("imageURL", imageURL);
            postData.put("musicBrainzId", artist.getMbid());
            postData.put("mood", artist.getMood().toLowerCase(Locale.ROOT));


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
                System.out.println(error.toString());
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
