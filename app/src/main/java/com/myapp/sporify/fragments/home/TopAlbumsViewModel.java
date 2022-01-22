package com.myapp.sporify.fragments.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.interfaces.VolleyCallBack;
import com.myapp.sporify.interfaces.VolleyCallBackAlt;
import com.myapp.sporify.mappers.AlbumMapper;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.UserModel; //will be uploaded later ( kinda worked together on them... I did mostly nothing on them...)
import com.myapp.sporify.utils.Const;  // same...
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopAlbumsViewModel extends ViewModel {

    private MutableLiveData<List<Album>> albums;
    private List<Album> albumList;

    private RequestQueue requestQueue;

    public TopAlbumsViewModel() {
        albumList = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();
    }

    public LiveData<List<Album>> getTopAlbums(String token, int limit){
        if (albums == null) {
            albums = new MutableLiveData<>();
            fetchAlbumsByGenre(token, limit);
        }
        return albums;
    }

    public LiveData<List<Album>> getTopAlbumsSingle(int limit, String genre){
        if (albums == null) {
            albums = new MutableLiveData<>();
            fetchTopAlbums(limit, genre);
        }
        return albums;
    }

    private void fetchUserGenre(String accessToken, VolleyCallBackAlt callBack) {
        String url = Const.DB_URL + "/api/user/me";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String genre = response.getString("genre");

                    callBack.onSuccess(genre);

                }
                catch (JSONException e){
                    Log.d("Parsing error: ", e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.d("VOLLEY ERROR: ", error.getMessage());
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
    }

    private void fetchTopAlbums(int limit, String genre) {

        String url =
                "http://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=" + genre + "&limit=" + limit +  "&api_key=8fc89f699e4ff45a21b968623a93ed52&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                albumList = AlbumMapper.getTopAlbumsFromJson(response);

                albums.setValue(albumList);
                albums.postValue(albumList);

            } catch (JSONException e) {
                Log.d("Parsing error: ", e.getMessage());
                // Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Request error: ", error.getMessage());
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchAlbumsByGenre(String token, int limit){
        fetchUserGenre(token, new VolleyCallBackAlt() {
            @Override
            public void onSuccess(String id) {
                fetchTopAlbums(limit, id);
            }
        });
    }
}
z