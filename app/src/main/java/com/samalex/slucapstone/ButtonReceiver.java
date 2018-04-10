package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AlexL on 7/25/2017.
 */

//class that receives input from no button in notification
public class ButtonReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    @Override
    public void onReceive(Context context, Intent intent){

        String broadcastID = intent.getStringExtra("broadcast Int");
        int notificationId = Integer.parseInt(broadcastID);
        Log.e("canceling", broadcastID);


        //cancels notification with given broadcast id
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        manager.deleteNotificationChannel(CHANNEL_ID);
    }
}
