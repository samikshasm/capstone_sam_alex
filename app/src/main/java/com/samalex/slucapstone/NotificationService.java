package com.samalex.slucapstone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by AlexL on 7/10/2017.
 */

public class NotificationService extends Service {


    private Integer counterInt=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("We are in notification", "yay");
        int NOTIFICATION_ID;


        //String userIDMA = intent.getStringExtra("User ID");
        String initialTimeStr = intent.getStringExtra("initial time");
        String broadcastId = intent.getStringExtra("broadcast Int");
        NOTIFICATION_ID = Integer.parseInt(broadcastId);

        if (NOTIFICATION_ID != 5) {

            Intent mainIntent = new Intent(this, MainActivity.class);
           // mainIntent.putExtra("User ID", userIDMA);
            mainIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent mainPI = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT);

            Intent yesIntent = new Intent(this, Main2Activity.class);
            yesIntent.putExtra("notificationBool", 100);
            //yesIntent.putExtra("User ID", userIDMA);
            yesIntent.putExtra("initial time", initialTimeStr);
            yesIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent yesIntent1 = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);

            Intent noIntent = new Intent(this, ButtonReceiver.class);
            noIntent.putExtra("notificationBool", 200);
            noIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent noIntent1 = PendingIntent.getBroadcast(this, 0, noIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.small_statusbar_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                    .setContentTitle("Boozymeter")
                    .setContentText("It's been 30 minutes! Have you had a drink?")
                    .addAction(0, "Yes", yesIntent1)
                    .addAction(0, "No", noIntent1)
                    //.setFullScreenIntent(yesIntent1, true)
                    .setFullScreenIntent(noIntent1, true)
                    .setAutoCancel(true);

            //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //builder.setContentIntent(mainPI);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());

            // Toast.makeText(this, counter, Toast.LENGTH_SHORT).show();
        }else{
            Intent morningIntent = new Intent(this, MorningQS.class);
            PendingIntent morningIntent1 = PendingIntent.getActivity(this, 0, morningIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.small_statusbar_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                    .setContentTitle("Boozymeter")
                    .setContentText("Time for morning questionnaire")
                    .setAutoCancel(true);

            builder.setContentIntent(morningIntent1);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());
        }
        return START_NOT_STICKY;
    }

}