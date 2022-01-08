package com.myapp.sporify;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MockDataReader {

    public static String readFile(Context appContext, String filename) throws Exception
    {
        StringBuilder contents = new StringBuilder();
        try (InputStream in = appContext.getAssets().open(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                contents.append(line);
            }
            return contents.toString();
        } catch (IOException e) {
            throw  new Exception(e.getMessage());
        }
    }


    public static JSONObject getNetworkResponse(String filename) throws Exception {
        String response = MockDataReader.readFile(InstrumentationRegistry.getInstrumentation().getContext(), filename);
        return new JSONObject(response);
    }
}