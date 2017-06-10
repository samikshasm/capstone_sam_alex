package com.example.samikshasm.slucapstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by samikshasm on 6/6/17.
 */

public class LoginDatabaseHelper {
    // Need versioning in case database schema changes
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Login.db";

    public LoginDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // Create the database table
        String command = "CREATE TABLE " + LoginDBContract.LoginDBEntry.TABLE_NAME + " (";
        command += LoginDBContract.LoginDBEntry._ID + " INTEGER PRIMARY KEY,";
        command += LoginDBContract.LoginDBEntry.COLUMN_NAME_USERNAME + " TEXT,";
        command += LoginDBContract.LoginDBEntry.COLUMN_NAME_PASSWORD + " TEXT,";
        command += ")";

        db.execSQL(command);

        // Temporarily populate the database
        ContentValues values = new ContentValues();
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_USERNAME, "Wayne");
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_PASSWORD, "Bruce");
        db.insert(LoginDBContract.LoginDBEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_USERNAME, "Kent");
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_PASSWORD, "Clark");
        db.insert(LoginDBContract.LoginDBEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_USERNAME, "Kent1");
        values.put(LoginDBContract.LoginDBEntry.COLUMN_NAME_PASSWORD, "Clark1");
        db.insert(LoginDBContract.LoginDBEntry.TABLE_NAME, null, values);

        Log.v("Contact Manager", "Populating database");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete the database and the recreate it
        String command = "DROP TABLE IF EXISTS " + LoginDBContract.LoginDBEntry.TABLE_NAME;
        db.execSQL(command);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}