package com.sporifyapp.sporify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import Users.UserModel;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CheckBox termsBox = findViewById(R.id.checkTerms);
        EditText et_FirstName = findViewById(R.id.registerFN);
        EditText et_LastName = findViewById(R.id.registerLN);
        EditText et_Username = findViewById(R.id.registerUsername);
        TextView et_Email = findViewById(R.id.registerMail);
        TextView pw_Password = findViewById(R.id.registerPass);
        EditText pw_ConPass = findViewById(R.id.registerConPass);


        TextView txtMatchingPass = findViewById(R.id.txtMatchingPass);



        Button signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                UserModel userModel;

                try {
                    userModel = new UserModel(-1, et_FirstName.getText().toString(), et_LastName.getText().toString(), et_Username.getText().toString(), et_Email.getText().toString(), pw_Password.getText().toString());
                    Toast.makeText(SignupActivity.this, userModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(SignupActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                    userModel = new UserModel(-1, "ERROR", "ERROR", "ERROR", "", "");
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(SignupActivity.this);

                boolean success = databaseHelper.addOne(userModel);
                Toast.makeText(SignupActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                if(pw_Password.getText().toString().equals(pw_ConPass.getText().toString()) && termsBox.isChecked())
                {
                    Toast.makeText(SignupActivity.this, "Signup Successful!",Toast.LENGTH_SHORT).show();
                    finish();
                } else if(!pw_Password.getText().toString().equals(pw_ConPass.getText().toString())) txtMatchingPass.setVisibility(View.VISIBLE);
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