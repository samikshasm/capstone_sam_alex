package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AlexL on 7/10/2017.
 */

public class TimerReceiver extends BroadcastReceiver {

    private Integer id;
    private String userIDMA;
    private String initialTimeStr;
    private String counterStr;
    private String broadcastStr;
    private Integer broadcastID;

    @Override
    public void onReceive(Context context, Intent intent) {

        //userIDMA = intent.getStringExtra("User ID");
        SharedPreferences getUserID = context.getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = getUserID.getString("user ID", "none");

        initialTimeStr = intent.getStringExtra("initial time");
        //counterStr = intent.getStringExtra("counter");
        broadcastStr = intent.getStringExtra("broadcast Int");
        Log.e("We are in the receiver", broadcastStr);
        broadcastID = Integer.parseInt(broadcastStr);


        if(broadcastID < 4) {
            Intent service_intent = new Intent(context, NotificationService.class);
            //service_intent.putExtra("User ID", userIDMA);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);
            //This is where it crashed
            context.startForegroundService(service_intent);
        }

        else if (broadcastID == 4) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString("currentScreen", "start");
            mEditor.apply();

         /*   SharedPreferences mSharedPreferences1 = context.getSharedPreferences("screen", MODE_PRIVATE);
            String selectedScreen = mSharedPreferences1.getString("currentScreen","none");
            Log.e("shared preferences", selectedScreen);*/
        }

        else if (broadcastID == 5) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString("currentScreen", "morningQS");
            mEditor.apply();
            Intent service_intent = new Intent(context, NotificationService.class);
            //service_intent.putExtra("User ID", userIDMA);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);
            context.startService(service_intent);
        }


    }
}