package com.sporifyapp.sporify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecoverPassword extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(RecoverPassword.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password);

        TextView new_password = findViewById(R.id.edTxt_newPassword);
        TextView conf_password = findViewById(R.id.edTxt_confirmPassword);
        Button button = findViewById(R.id.recoverBtn);

        Intent intent = new Intent(RecoverPassword.this, LoginController.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isExist = databaseHelper.checkPasswordExist(new_password.getText().toString());
                if(isExist)
                {
                    if(!new_password.getText().toString().isEmpty() && !conf_password.getText().toString().isEmpty() && new_password.equals(conf_password)) {
                        Toast.makeText(RecoverPassword.this, "Password Recovered", Toast.LENGTH_SHORT).show();
                        String npass = new_password.getText().toString();
                        String conpass = conf_password.getText().toString();
                        intent.putExtra("npass",npass);
                        intent.putExtra("conpass",conpass);
                        startActivity(intent);
                    } else Toast.makeText(RecoverPassword.this, "Please type your password",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecoverPassword.this, "Invalid Email",Toast.LENGTH_SHORT).show();
                }


                startActivity(new Intent(RecoverPassword.this, LoginController.class));
            }
        });
    }
}
