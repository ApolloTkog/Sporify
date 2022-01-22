package com.myapp.sporify.activities.auth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.sporify.MainActivity;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.auth.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button signIn, signUp;

    private LoginViewModel loginViewModel;

    private SharedPreferences sharedPref;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        login();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void login(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String token) {
                if(token.equals("Bad credentials!") ){
                    Toast.makeText(getApplicationContext(), "Bad credentials!", Toast.LENGTH_SHORT).show();
                    loginViewModel.getLoginResponse().removeObservers(LoginActivity.this);
                    return;
                }

                if(token.equals("Timeout error")){
                    Toast.makeText(getApplicationContext(), "Server is down!", Toast.LENGTH_SHORT).show();
                    loginViewModel.getLoginResponse().removeObservers(LoginActivity.this);
                    return;
                }

//                Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();


                sharedPref.edit().putString("token", token).apply();
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                loginViewModel.getLoginResponse().removeObservers(LoginActivity.this);

            }
        };


        signIn.setOnClickListener(view -> {
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            loginViewModel.init(usernameText, passwordText);


            loginViewModel.getLoginResponse().observe(this, observer);
        });
    }
}