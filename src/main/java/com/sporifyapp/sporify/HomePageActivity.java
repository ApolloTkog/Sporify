package com.sporifyapp.sporify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView settings =(ImageView) findViewById(R.id.iconSettings);


        TextView txtUsername = findViewById(R.id.txtUsrname);


        txtUsername.setText(getIntent().getStringExtra("name"));

        //Settings icon will open up the Settings page when clicked:
        settings.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomePageActivity.this, SettingsActivity.class));
            }
        });
    }
}