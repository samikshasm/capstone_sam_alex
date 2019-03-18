package com.samalex.slucapstone;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//class for updating the database at a given interval
//adapted some code from vipulasri Location Updates open source github
public class LocationUpdates extends IntentService {


    //initialize variables
    private String TAG = this.getClass().getSimpleName();
    private DatabaseReference mDatabase;
    private String userIDMA;
    private String test;
    private String time;
    private String text;
    private double latitude;
    private double longitude;
    private Integer nightCount;


    //constructors
    public LocationUpdates() {
        super("Fused Location");
    }

    public LocationUpdates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //initializes database instance
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nightCount = getNightCount();

        //checks to see if a valid location was passed
        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();

            if(location !=null) {

                //gets latitude and longitude from location
                userIDMA = BackgroundLocationService.userID;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i(TAG, "onHandleIntent " + location.getLatitude() + "," + location.getLongitude());

                //gets current date and time to log location in database
                long currentDateTime = System.currentTimeMillis();
                Date currentDate = new Date(currentDateTime);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = dateFormat.format(currentDate);
                text = latitude + "&" + longitude;
                String selectedScreen = getCurrentScreen();

                Log.e("selectedScreen", selectedScreen);

                //only writes to the database if the current screen is main
                if (selectedScreen.equals("main")) {
                    writeToDB(text);
                }
            }
        }
    }

    private String getCurrentScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        return mSharedPreferences.getString("currentScreen","none");
    }

    //function to store the screen
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }

    //function to write to the database
    public void writeToDB(String text1) {
        Log.e("Location Service DB", "Boi");
        DatabaseReference mRef = mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Location").child("Time: "+time);
        mRef.setValue(text1);

    }

    //function to get the current night count
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }
}