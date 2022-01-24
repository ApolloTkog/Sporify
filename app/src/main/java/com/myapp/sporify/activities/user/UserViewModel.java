package com.myapp.sporify.activities.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.mappers.FavoriteMapper;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.UserModel;
import com.myapp.sporify.utils.Const;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {


    private LiveData<UserModel> userLiveData;
    private LiveData<String> userResponse;
    private LiveData<List<Artist>> userArtistsByMood;

    private RequestQueue requestQueue;

    List<Artist>  userTracksByMood;

    public UserViewModel(){
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();

        userTracksByMood = new ArrayList<>();
    }

    public void init(String token){
        userLiveData = getUser(token);
    }

    public void update(String token, String genre, String mood){
        userResponse = updateUser(token, genre, mood);
    }

    public void artists(String token){
        userArtistsByMood = getArtistsByMood(token);
    }

    public LiveData<UserModel> getUserData() {
        return userLiveData;
    }

    public LiveData<List<Artist>> getUserTracksLiveData() {
        return userArtistsByMood;
    }

    public LiveData<String> getUserResponse() {
        return userResponse;
    }

    public LiveData<String> updateUser(String accessToken, String genre, String mood){
        final MutableLiveData<String> responseData = new MutableLiveData<>();

        String postUrl = Const.DB_URL + "/api/user/me";
        JSONObject postData = new JSONObject();
        try {
            postData.put("genre", genre);
            postData.put("mood", mood);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, postUrl, postData, new Response.Listener<JSONObject>() {
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
//                System.out.println(error.toString());
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

    public LiveData<UserModel> getUser(String accessToken){
        final MutableLiveData<UserModel> responseData = new MutableLiveData<>();

        String url = Const.DB_URL + "/api/user/me";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserModel user = new UserModel();
                try{
                    String username = response.getString("username");
                    String genre = response.getString("genre");
                    String mood = response.getString("mood");

                    user = new UserModel(username, genre, mood);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(user);
                responseData.setValue(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                Log.d("VOLLEY ERROR: ", error.toString());
//                String message = parseVolleyError(error);
//                responseData.postValue(message);
//                responseData.setValue(message);
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

    public LiveData<List<Artist>> getArtistsByMood(String accessToken){
        final MutableLiveData<List<Artist>> responseData = new MutableLiveData<>();

        String url = Const.DB_URL + "/api/user/artistsMood";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    userTracksByMood = FavoriteMapper.getFavoriteArtistsFromJson(response);
                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }

                responseData.postValue(userTracksByMood);
                responseData.setValue(userTracksByMood);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
                Log.d("VOLLEY ERROR: ", error.toString());
//                String message = parseVolleyError(error);
//                responseData.postValue(message);
//                responseData.setValue(message);
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
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            JSONObject data = new JSONObject(responseBody);
            return data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}