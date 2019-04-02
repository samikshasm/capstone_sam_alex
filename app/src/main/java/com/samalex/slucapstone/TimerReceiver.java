package com.samalex.slucapstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AlexL on 7/10/2017.
 */

public class TimerReceiver extends BroadcastReceiver {

    private String initialTimeStr;
    private String broadcastStr;
    private Integer broadcastID;

    @Override
    public void onReceive(Context context, Intent intent) {

        initialTimeStr = intent.getStringExtra("initial time");
        broadcastStr = intent.getStringExtra("broadcast Int");
        Log.e("We are in the receiver", broadcastStr);
        broadcastID = Integer.parseInt(broadcastStr);

        if (broadcastID < BoozymeterApplication.NUM_EVENING_REMINDER+1) {
            Intent service_intent = new Intent(context, NotificationService.class);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
            } else {
                context.startService(service_intent);
            }
        } else if (broadcastID == BoozymeterApplication.NUM_EVENING_REMINDER+1) {
            storeScreen("start", context);
        } else if (broadcastID == 5) {
            storeScreen("morningQS", context);

            Intent service_intent = new Intent(context, NotificationService.class);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);
            service_intent.putExtra("this survey is for cycle", intent.getIntExtra("this survey is for cycle", 0));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
            } else {
                context.startService(service_intent);
            }
        } else if(broadcastID == NotificationService.EVENING_REMINDER_NOTIFICATION_ID) {
            Intent service_intent = new Intent(context, NotificationService.class);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
            } else {
                context.startService(service_intent);
            }
        }
    }

    private void storeScreen(String start, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", start);
        mEditor.apply();
    }
}