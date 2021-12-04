package com.example.sporify.artist;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sporify.R;
import com.example.sporify.artist.ui.main.SectionsPagerAdapter;
import com.example.sporify.audiodb_functions.AudioDbToJsonParser;
import com.example.sporify.databinding.ActivityArtistDisplayBinding;
import com.example.sporify.jsonfunctions.JsonToDataFactory;
import com.example.sporify.jsonfunctions.StringToJson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ArtistDisplay extends AppCompatActivity {

    private ActivityArtistDisplayBinding binding;
    TextView tempDisplay;
    EditText artistInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArtistDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        tempDisplay = (TextView) findViewById(R.id.tempTextView);
        artistInput = (EditText) findViewById(R.id.artistInput);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void artistDataDisplay(View view) throws JSONException {

        //will chance. It will work with functions. For now this works.

        String artistName = null, tempString = null;
        //UrlGenerator urlgen;
        String url = null;
        int timeout = 1000;
        AudioDbToJsonParser parser = null;
        JSONObject tempJson = null;
        StringToJson jsonsaver = null;
        JsonToDataFactory data = null;
        Artist currentArtist = null;


        artistName = artistInput.getText().toString();
        //url = urlgen.generateUrl(artistName);
        url = "theaudiodb.com/api/v1/json/2/search.php?s=" + artistName;

       tempString = parser.getJSONFromURL(url,timeout);
       tempJson = jsonsaver.stringToJson(tempString);
       currentArtist = data.getJsonData(tempJson,currentArtist);

       tempDisplay.setText("Artist name: " + currentArtist.getName() + " Genre: " + currentArtist.getGenre() + "\nBio: " + currentArtist.getBio());





    }

}