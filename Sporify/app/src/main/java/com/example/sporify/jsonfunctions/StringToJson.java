package com.example.sporify.jsonfunctions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class StringToJson {

public JSONObject stringToJson(String rawData) throws JSONException {

    JSONObject object = null;
    
            JSONArray array = new JSONArray(rawData);
            for(int i=0; i < array.length(); i++)
            {
                object = array.getJSONObject(i);
            }
    return object;
}
}
