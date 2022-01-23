package com.myapp.sporify.activities.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.myapp.sporify.R;
import com.myapp.sporify.activities.auth.login.LoginActivity;
import com.myapp.sporify.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class User extends AppCompatActivity {

    TextView username;
    Spinner moodSpinner, genreSpinner;

    Button updateBtn;

    List<String> genres, moods;

    UserViewModel userViewModel;

    String token;

    String currentMood = "", currentGenre = "";

    ArrayAdapter<String> genresAdapter, moodAdapter;

    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        // Getting user token  from shared prefs
        SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "token");

        username = findViewById(R.id.username);
        moodSpinner = findViewById(R.id.mood_spinner);
        genreSpinner = findViewById(R.id.genre_spinner);
        logout = findViewById(R.id.logout);

        updateBtn = findViewById(R.id.update_btn);

        genres = new ArrayList<>();
        moods = new ArrayList<>();

        initializeSpinners();

        genresAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_row, genres);

        moodAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_row, moods);


        genreSpinner.setAdapter(genresAdapter);
        moodSpinner.setAdapter(moodAdapter);

        fetchUserData();

        Observer<String> updateUser = s -> {
            if(s == null)
                return;

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            userViewModel.getUserResponse().removeObservers(User.this);
        };

        updateBtn.setOnClickListener(view -> {
            userViewModel.update(token,
                    genreSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT),
                    moodSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT));

            userViewModel.getUserResponse().observe(User.this, updateUser);
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove token from sharedprefs
                SharedPreferences mySPrefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("token");
                editor.apply();

                // redirect to login activity
                finish();
                startActivity(new Intent(User.this, LoginActivity.class));

            }
        });
    }

    public void initializeSpinners(){
        // add genres to list
        genres.add("rock");
        genres.add("pop");
        genres.add("electronic");
        genres.add("alternative");
        genres.add("indie");
        genres.add("metal");
        genres.add("jazz");
        genres.add("alternative rock");
        genres.add("folk");
        genres.add("punk");
        genres.add("Hip-Hop");
        genres.add("black metal");
        genres.add("hard rock");
        genres.add("instrumental");
        genres.add("blues");
        genres.add("House");
        genres.add("soul");
        genres.add("rap");




        // add moods to list
        moods.add("happy");
        moods.add("intense");
        moods.add("sad");
        moods.add("carefree");
        moods.add("energetic");
        moods.add("in love");
        moods.add("angry");
        moods.add("good natured");
        moods.add("confrontational");
        moods.add("philosophical");
        moods.add("dreamy");
    }

    public void fetchUserData(){
        userViewModel.init(token);

        Observer<UserModel> getUserData = user -> {
            if(user == null)
                return;

            username.setText(user.getUsername());
            genreSpinner.setSelection(genresAdapter.getPosition(user.getGenre()));
            moodSpinner.setSelection(moodAdapter.getPosition(user.getMood()));


            userViewModel.getUserData().removeObservers(User.this);
        };

        userViewModel.getUserData().observe(User.this, getUserData);
    }

}