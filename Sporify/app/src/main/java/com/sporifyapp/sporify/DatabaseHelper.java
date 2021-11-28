package com.sporifyapp.sporify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import Models.UserModel;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_LASTNAME = "LASTNAME";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    SQLiteDatabase database;

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, "sporifyUsers.db", null, 1);
    }

    //This is called the first time a database is accessed.
    //There should be code in here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " + COLUMN_FIRSTNAME + " TEXT, " + COLUMN_LASTNAME + " TEXT,  " + COLUMN_USERNAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableStatement);
    }

    //This is called if the version number of the database is changed.
    //It prevents previous user apps from breaking when the database design changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase openDatabase()
    {
        String path = "//data/data/com.sporifyapp.sporify/databases/sporifyUsers.db";
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }


    //Add a new user during sign up
    public boolean addOne(UserModel userModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME, userModel.getFirstName());
        cv.put(COLUMN_LASTNAME, userModel.getLastName());
        cv.put(COLUMN_USERNAME, userModel.getUsername());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, userModel.getPassword());

        long insert = db.insert(USER_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }else return true;
    }

    public boolean checkUserExist(String username, String password)
    {
        String[] columns = {"username"};
        database = openDatabase();

        String selection = "username=? and password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = database.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public String fetch()
    {
        String emailResult = null;
        String selectQuery = "SELECT EMAIL FROM USER_TABLE";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            if(cursor.moveToFirst())
            do {
                emailResult = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return emailResult;
    }

    public boolean checkEmailExists(String email) {
        String[] columns = {"email"};
        database = openDatabase();

        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = database.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPasswordExist(String password)
    {
        String[] columns = {"password"};
        database = openDatabase();

        String selection = "password = ?";
        String[] selectionArgs = {password};

        Cursor cursor = database.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

}