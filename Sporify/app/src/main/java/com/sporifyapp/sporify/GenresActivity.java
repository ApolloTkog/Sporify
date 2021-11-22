package com.sporifyapp.sporify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GenresActivity extends AppCompatActivity {

    CheckBox mPop,mRock,mRap,mMetal,mHiphop,mJazz,mElectronic,mCountry;
    Button mResultBtn;
    TextView textView;
    private int i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        mPop = findViewById(R.id.pop);
        mRock = findViewById(R.id.rock);
        mMetal = findViewById(R.id.metal);
        mRap = findViewById(R.id.rap);
        mJazz = findViewById(R.id.jazz);
        mCountry = findViewById(R.id.country);
        mHiphop = findViewById(R.id.hiphop);
        mElectronic= findViewById(R.id.electronic);

        mResultBtn = findViewById(R.id.ChooseBtn);
        textView = findViewById(R.id.selectGenre);

        mResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                if(mPop.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mRock.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mMetal.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mRap.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mHiphop.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mJazz.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mCountry.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }

                if(mElectronic.isChecked()){
                    i++;
                    //Update the database making this genre favorite
                }


                textView.setText(i +" Genres Selected");
            }
        });



    }

}