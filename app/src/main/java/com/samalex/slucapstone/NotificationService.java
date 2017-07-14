package com.samalex.slucapstone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by AlexL on 7/10/2017.
 */

public class NotificationService extends Service {


    private Integer counter=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("We are in notification", "yay");
        int NOTIFICATION_ID = 12345;

        counter = counter + 1;

        Intent yesIntent = new Intent(this, MainActivity.class);
        yesIntent.putExtra("notificationBool",100);
        PendingIntent yesIntent1 = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);

       /* Intent noIntent = new Intent(this, MainActivity.class);
        noIntent.putExtra("notificationBool",200);
        PendingIntent noIntent1 = PendingIntent.getActivity(this, 0, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        // cancel intent
        Intent cancelIntent = new Intent(this, TimerReceiver.class);
        Bundle extras = new Bundle();
        extras.putInt("notification_id", NOTIFICATION_ID);
        cancelIntent.putExtras(extras);
        PendingIntent pendingCancelIntent =
                PendingIntent.getBroadcast(this, NOTIFICATION_ID, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT) ;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon_small)
                .setContentTitle("Boozymeter")
                .setContentText("It's been 30 minutes! Have you had a drink?")
                .addAction(0, "Yes", yesIntent1)
                .addAction(0, "No", pendingCancelIntent)
                .setFullScreenIntent(yesIntent1, true)
                //.setFullScreenIntent(noIntent1, true)
                .setAutoCancel(true);

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //builder.setContentIntent(yesIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());

       // Toast.makeText(this, counter, Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

}
