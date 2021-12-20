package com.myapp.sporify.activities.auth.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.sporify.MainActivity;
import com.myapp.sporify.R;
import com.myapp.sporify.activities.auth.login.LoginActivity;
import com.myapp.sporify.activities.auth.login.LoginViewModel;

public class SignUpActivity extends AppCompatActivity {
    private EditText username,email, password, confirmPassword;
    private Button signUp, signIn;

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        email = findViewById(R.id.et_email);
        confirmPassword = findViewById(R.id.et_confirm_password);
        signUp = findViewById(R.id.sign_up);
        signIn = findViewById(R.id.button_signIn);



        // sign in if you already have an account
        signIn();

        signUp();


    }

    private void signUp() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if(message == null ){
                    Toast.makeText(getApplicationContext(), "Error: Server error occurred!", Toast.LENGTH_SHORT).show();
                    signUpViewModel.getSignUpResponse().removeObservers(SignUpActivity.this);
                    return;
                }

                if(!message.equals("User registered successfully!") ){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    signUpViewModel.getSignUpResponse().removeObservers(SignUpActivity.this);
                    return;
                }

                Toast.makeText(getApplicationContext(), "Message: " + message, Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                signUpViewModel.getSignUpResponse().removeObservers(SignUpActivity.this);

            }
        };


        signUp.setOnClickListener(view -> {
            String usernameText = username.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String confirmPasswordText = confirmPassword.getText().toString();

            signUpViewModel.init(usernameText,emailText, passwordText, confirmPasswordText);
            signUpViewModel.getSignUpResponse().observe(this, observer);
        });
    }

    private void signIn() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}