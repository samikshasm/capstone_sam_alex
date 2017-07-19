package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AlexL on 7/10/2017.
 */

public class TimerReceiver extends BroadcastReceiver {

    private Integer id;
    private String userIDMA;
    private String initialTimeStr;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "Yay");

        userIDMA = intent.getStringExtra("User ID");
        initialTimeStr = intent.getStringExtra("initial time");
        Intent service_intent = new Intent(context, NotificationService.class);
        service_intent.putExtra("User ID", userIDMA);
        service_intent.putExtra("initial time", initialTimeStr);
        context.startService(service_intent);

      /* id = intent.getIntExtra("notification_id", 1);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);*/
    }
}
