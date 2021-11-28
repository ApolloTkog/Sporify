package com.sporifyapp.sporify;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

import static com.sporifyapp.sporify.DatabaseHelper.USER_TABLE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity
{
    SQLiteDatabase database;

    private Object HomePageActivity;
    private static final int GALLERY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        Button editEmailBtn = findViewById(R.id.editEmailBtn);
        Button editPassBtn = findViewById(R.id.editPassBtn);
        Button logoutBtn = findViewById(R.id.logoutBtn);

        ImageView profileImage = findViewById(R.id.imgProfile);
        Button editPhoto = findViewById(R.id.editPhotoBtn);

        DatabaseHelper databaseHelper = new DatabaseHelper(SettingsActivity.this);
        databaseHelper.openDatabase();

        TextView accMail = findViewById(R.id.txtEmailData);
        String data = databaseHelper.fetch();
        databaseHelper.close();
        accMail.setText(data);



    //SEGMENT - Edit Email -------------------------------------------------------------------------------------------------------------
        editEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmailDialog();
                }

                private void showEmailDialog()
                {
                    final Dialog mailDialog = new Dialog(SettingsActivity.this);
                    //We have added a title with a TextView, there is no need for a title.
                    mailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //The user will be able to cancel it by clicking anywhere outside of the dialog.
                    mailDialog.setCancelable(true);
                    //Mention the name of the layout of your custom dialog.
                    mailDialog.setContentView(R.layout.maildialog);
                    mailDialog.show();

                    EditText emailNew = mailDialog.findViewById(R.id.emailNew);
                    Button confirmInfo = mailDialog.findViewById(R.id.confirmBtn);

                    confirmInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(SettingsActivity.this, "Email changed successfully!", Toast.LENGTH_SHORT).show();
                            mailDialog.dismiss();
                        }
                    });
                }
            });
    //END SEGMENT - Edit Email ---------------------------------------------------------------------------------------------------------

    //SEGMENT - Edit Password ----------------------------------------------------------------------------------------------------------
        editPassBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPassDialog();
                    }
                    private void showPassDialog() {
                        final Dialog passdialog = new Dialog(SettingsActivity.this);
                        //We have added a title with a TextView, there is no need for a title.
                        passdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //The user will be able to cancel it by clicking anywhere outside of the dialog.
                        passdialog.setCancelable(true);
                        //Mention the name of the layout of your custom dialog.
                        passdialog.setContentView(R.layout.passdialog);
                        passdialog.show();

                        EditText passNew = passdialog.findViewById(R.id.passNew);
                        EditText passConfirm = passdialog.findViewById(R.id.passConfirm);
                        Button confirmInfo = passdialog.findViewById(R.id.confirmBtn);

                        confirmInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(SettingsActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                passdialog.dismiss();
                            }
                        });
                    }
                });
    //END SEGMENT - Edit Password ------------------------------------------------------------------------------------------------------

    //SEGMENT - Log Out ----------------------------------------------------------------------------------------------------------------
        logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingsActivity.this, LoginController.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SettingsActivity.this, "Signed out.", Toast.LENGTH_SHORT).show();
                    }
                });
    //END SEGMENT - Log Out ------------------------------------------------------------------------------------------------------------

    //SEGMENT - Profile Photo ----------------------------------------------------------------------------------------------------------
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }

            private void selectImage() {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder chooseBuilder = new AlertDialog.Builder(SettingsActivity.this);
                chooseBuilder.setTitle("Add Photo!");
                chooseBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Pick an Image"), GALLERY_REQUEST_CODE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                chooseBuilder.show();
            }

            private void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                onActivityResult(requestCode, resultCode, data);
                if (resultCode == GALLERY_REQUEST_CODE && requestCode == RESULT_OK && data != null) {
                    Uri imageData = data.getData();
                    profileImage.setImageURI(imageData);
                }
            }
        });
    //END SEGMENT - Profile Photo ------------------------------------------------------------------------------------------------------
    }
}
