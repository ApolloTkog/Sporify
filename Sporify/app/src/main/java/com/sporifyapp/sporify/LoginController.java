package com.sporifyapp.sporify;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginController extends AppCompatActivity
{
    DatabaseHelper databaseHelper = new DatabaseHelper(LoginController.this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView username = findViewById(R.id.usrnLoginField);
        TextView password = findViewById(R.id.pswdLoginField);
        TextView forpass = findViewById(R.id.txtForgotPass);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);

        Intent intent = new Intent(LoginController.this, HomePageActivity.class);

    //SEGMENT - Log In Controller -------------------------------------------------------------------------------------------
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = databaseHelper.checkUserExist(username.getText().toString(), password.getText().toString());
                if(isExist)
                {
                    if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                        Toast.makeText(LoginController.this, "Login Successful!",Toast.LENGTH_SHORT).show();
                        String name = username.getText().toString();
                        intent.putExtra("name",name);
                        startActivity(intent);
                    } else Toast.makeText(LoginController.this, "Login Failed!",Toast.LENGTH_SHORT).show();
                } else {
                    password.setText(null);
                    Toast.makeText(LoginController.this, "Invalid credentials!",Toast.LENGTH_SHORT).show();
                }

<<<<<<< Updated upstream
=======
            }
        });
    //END SEGMENT - Log In Controller ---------------------------------------------------------------------------------------

        forpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginController.this, ResetPassword.class));
>>>>>>> Stashed changes
            }
        });
    //END SEGMENT - Log In Controller ---------------------------------------------------------------------------------------

        TextView signupView =(TextView) findViewById(R.id.txtNewMember);
        String signupText = "New member? Sign up now!";
        SpannableString ss = new SpannableString(signupText);

        ClickableSpan clickableSpan1 = new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                startActivity(new Intent(LoginController.this, SignupActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds)
            {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan1, 20, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupView.setText(ss);
        signupView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}