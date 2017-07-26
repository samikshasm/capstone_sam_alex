package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AlexL on 7/25/2017.
 */

public class ButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        Log.e("no intent", "yay");

        String broadcastID = intent.getStringExtra("broadcast Int");
        int notificationId = Integer.parseInt(broadcastID);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
