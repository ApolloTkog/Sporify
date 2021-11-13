package com.sporifyapp.sporify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView password =(TextView) findViewById(R.id.registerPass);
        TextView confirmPass =(TextView) findViewById(R.id.registerConPass);
        CheckBox termsBox =(CheckBox) findViewById(R.id.checkTerms);

        TextView txtMatchingPass =(TextView) findViewById(R.id.txtMatchingPass);

        Button signupBtn = (Button) findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirmPass.getText().toString()) && termsBox.isChecked())
                {
                    Toast.makeText(SignupActivity.this, "Signup Successful!",Toast.LENGTH_SHORT).show();
                    finish();
                } else if(!password.getText().toString().equals(confirmPass.getText().toString())) txtMatchingPass.setVisibility(View.VISIBLE);
                else if(!termsBox.isChecked()) Toast.makeText(SignupActivity.this,"You must agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show();
                else
                    {
                    txtMatchingPass.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "You must agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show();
                }
                }
        });


    }
}