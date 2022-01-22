package com.myapp.sporify.activities.auth.login;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Searchable;
import com.myapp.sporify.utils.Const;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.Type;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginViewModel  extends ViewModel {
    private LiveData<String> loginResponse;

    private RequestQueue requestQueue;


    public LoginViewModel() {

        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();

    }

    public void init(String username, String password){
        loginResponse = attemptLogin(username,password);
    }

    public LiveData<String> getLoginResponse() {
        return loginResponse;
    }

    public LiveData<String> attemptLogin(String username, String password){
        final MutableLiveData<String> loginData = new MutableLiveData<>();


        String postUrl = Const.DB_URL + "/api/auth/signin";
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());

                try{
                    String accessToken = response.getString("accessToken");
                    loginData.postValue(accessToken);
                    loginData.setValue(accessToken);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "Timeout error";
                if(error.networkResponse != null){
                    message = parseVolleyError(error);
                }
//                Log.d("Login: ", error.getMessage());
                loginData.postValue(message);
                loginData.setValue(message);
            }
        });

        requestQueue.add(jsonObjectRequest);

        return loginData;
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