package com.sporifyapp.sporify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(ResetPassword.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        TextView email = findViewById(R.id.edTxt_emailConfirm);
        Button button = findViewById(R.id.confirmBtn);

        Intent intent = new Intent(ResetPassword.this, RecoverPassword.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = databaseHelper.checkEmailExists(email.getText().toString());
                if(isExist)
                {
                    if(!email.getText().toString().isEmpty()) {
                        Toast.makeText(ResetPassword.this, "Successfull Email",Toast.LENGTH_SHORT).show();
                        String mail = email.getText().toString();
                        intent.putExtra("mail",mail);
                        startActivity(intent);
                    } else Toast.makeText(ResetPassword.this, "Please type your email!",Toast.LENGTH_SHORT).show();}
                    else{
                        Toast.makeText(ResetPassword.this, "Invalid",Toast.LENGTH_SHORT).show();
                        }
                }

        });
    }
}
