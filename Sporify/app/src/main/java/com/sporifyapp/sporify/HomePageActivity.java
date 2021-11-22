package com.sporifyapp.sporify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
import Users.UserModel;

=======
>>>>>>> Stashed changes
public class HomePageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView settings = findViewById(R.id.iconSettings);


        TextView txtUsername = findViewById(R.id.txtUsrname);


        txtUsername.setText(getIntent().getStringExtra("name"));

        //SEGMENT - Settings -------------------------------------------------------------------------------------------------------
        //Settings icon will open up the Settings page when clicked:
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(HomePageActivity.this, SettingsActivity.class);
                startActivity(new Intent(HomePageActivity.this, SettingsActivity.class));
            }
        });
        //END SEGMENT - Settings ---------------------------------------------------------------------------------------------------
    }
}
