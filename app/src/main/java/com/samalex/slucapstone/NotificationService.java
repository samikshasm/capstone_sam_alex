package com.samalex.slucapstone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by AlexL on 7/10/2017.
 */

public class NotificationService extends Service {


    private Integer counterInt=0;
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";
    private String group;
    private NotificationCompat.Builder builder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("We are in notification", "yay");
        int NOTIFICATION_ID;
                String initialTimeStr = intent.getStringExtra("initial time");
        String broadcastId = intent.getStringExtra("broadcast Int");
        NOTIFICATION_ID = Integer.parseInt(broadcastId);


        //creates a notification for the alarms that occur during the night after 30 minutes has gone by
        if (NOTIFICATION_ID != 5) {

            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent mainPI = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent yesIntent = new Intent(this, Main2Activity.class);
            yesIntent.putExtra("notificationBool", 100);
            yesIntent.putExtra("initial time", initialTimeStr);
            yesIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent yesIntent1 = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Log.e("yesIntent1", ""+yesIntent1);

            Intent noIntent = new Intent(this, ButtonReceiver.class);
            noIntent.putExtra("notificationBool", 200);
            noIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent noIntent1 = PendingIntent.getBroadcast(this, 0, noIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableLights(true);
                channel.enableVibration(true);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small_statusbar_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                        .setContentTitle("Boozymeter")
                        .setContentText("It's been 30 minutes! Have you had a drink?")
                        .addAction(R.drawable.check_small, "Yes", yesIntent1)
                        .addAction(R.drawable.cancel_small, "No", noIntent1)
                        //.setFullScreenIntent(yesIntent1, true)
                        //.setFullScreenIntent(mainPI, true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                builder.setDefaults(Notification.DEFAULT_SOUND);


                //builder.setContentIntent(mainPI);
                //NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
                //nManager.notify(NOTIFICATION_ID, builder.build());
                startForeground(NOTIFICATION_ID, builder.build());

            }else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small_statusbar_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                        .setContentTitle("Boozymeter")
                        .setContentText("It's been 30 minutes! Have you had a drink?")
                        .addAction(R.drawable.check_small, "Yes", yesIntent1)
                        .addAction(R.drawable.cancel_small, "No", noIntent1)
                        //.setFullScreenIntent(yesIntent1, true)
                        //.setFullScreenIntent(mainPI, true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                builder.setDefaults(Notification.DEFAULT_SOUND);


                //builder.setContentIntent(mainPI);
                NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
                nManager.notify(NOTIFICATION_ID, builder.build());
                //startForeground(NOTIFICATION_ID, builder.build());

            }


        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableLights(true);
                channel.enableVibration(true);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            SharedPreferences groupSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
            group = groupSharedPreferences.getString("Group","none");

            Intent morningIntent;

            /*if (group.equals("experimental")) {
                morningIntent = new Intent(this, MorningReport.class);
            }
            else {*/
                morningIntent = new Intent(this, MorningQS.class);

            //}
            //creates notification that appears when the morning alarm goes off
            morningIntent.putExtra("broadcast Int", broadcastId);
            PendingIntent morningIntent1 = PendingIntent.getActivity(this, 0, morningIntent, PendingIntent.FLAG_ONE_SHOT);

           /* if (group.equals("experimental")) {
                 builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small_statusbar_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                        .setContentTitle("Boozymeter")
                        .setContentText("View your Morning Report")
                        .setAutoCancel(true);
            }*/
            if (group.equals("control") | group.equals("none") | group.equals("experimental")) {
                builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small_statusbar_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                        .setContentTitle("Boozymeter")
                        .setContentText("Time for morning questionnaire")
                        .setAutoCancel(true);
            }


            builder.setContentIntent(morningIntent1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForeground(NOTIFICATION_ID, builder.build());
            }else{
                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(NOTIFICATION_ID, builder.build());
            }

        }
        return START_NOT_STICKY;
    }

}