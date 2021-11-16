package com.sporifyapp.sporify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.mlkit.common.sdkinternal.SharedPrefManager;

public class SettingsActivity extends AppCompatActivity
{

    private Object HomePageActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button logoutBtn = findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SettingsActivity.this, LoginController.class));
                Toast.makeText(SettingsActivity.this, "Signed out.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}