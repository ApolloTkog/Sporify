package com.myapp.sporify.activities.auth.signup;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.sporify.utils.MyApplication;
import com.myapp.sporify.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class SignUpViewModel extends ViewModel {

    private LiveData<String> signUpResponse;

    private RequestQueue requestQueue;

    public SignUpViewModel() {

        requestQueue = VolleySingleton.getmInstance(MyApplication.getAppContext()).getRequestQueue();

    }

    public void init(String username,String email, String password, String confirmPassword){
        signUpResponse = attemptSignUp(username,email,password, confirmPassword);
    }

    public LiveData<String> getSignUpResponse() {
        return signUpResponse;
    }

    public LiveData<String> attemptSignUp(String username,String email, String password, String confirmPassword){
        final MutableLiveData<String> signUpData = new MutableLiveData<>();

        String postUrl = "http://192.168.2.5:8081/api/auth/signup";
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("confirmPassword", confirmPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Log.d("VOLLEY", response.toString());
                try{
                    String message = response.getString("message");
                    signUpData.postValue(message);
                    signUpData.setValue(message);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.e("VOLLEY", error.toString());

                String message = parseVolleyError(error);
                signUpData.postValue(message);
                signUpData.setValue(message);

            }
        });

        requestQueue.add(jsonObjectRequest);

        return signUpData;
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
