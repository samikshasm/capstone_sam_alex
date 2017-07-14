package com.samalex.slucapstone;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationUpdates extends IntentService {

    private String TAG = this.getClass().getSimpleName();
    private DatabaseReference mDatabase;
    private String userIDMA;
    private String test;
    private String time;
    private String text;
    private double latitude;
    private double longitude;

    public LocationUpdates() {
        super("Fused Location");
    }

    public LocationUpdates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();

            if(location !=null) {
                userIDMA = BackgroundLocationService.userID;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i(TAG, "onHandleIntent " + location.getLatitude() + "," + location.getLongitude());

                long currentDateTime = System.currentTimeMillis();
                Date currentDate = new Date(currentDateTime);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = dateFormat.format(currentDate);
                text = latitude + "," + longitude;

                writeToDB(text);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder noti = new NotificationCompat.Builder(this);
                noti.setContentTitle("Fused Location");
                noti.setContentText(location.getLatitude() + "," + location.getLongitude()+","+test);
                noti.setSmallIcon(R.mipmap.ic_launcher);

                notificationManager.notify(1234, noti.build());
            }
        }
    }

    public void writeToDB(String text1) {
        DatabaseReference mRef = mDatabase.child("Users").child(userIDMA).child(time);
        mRef.setValue(text1);

    }
}



