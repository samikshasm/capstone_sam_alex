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

        if (broadcastID < 4) {
            Intent service_intent = new Intent(context, NotificationService.class);
            service_intent.putExtra("initial time", initialTimeStr);
            service_intent.putExtra("broadcast Int", broadcastStr);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
            } else {
                context.startService(service_intent);
            }
        } else if (broadcastID == 4) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString("currentScreen", "start");
            mEditor.apply();
        } else if (broadcastID == 5) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString("currentScreen", "morningQS");
            mEditor.apply();
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
}