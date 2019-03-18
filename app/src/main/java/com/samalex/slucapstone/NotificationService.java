package com.samalex.slucapstone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by AlexL on 7/10/2017.
 */

public class NotificationService extends Service {
    final public static int MORNING_QUESTIONNAIRE_NOTIFICATION_ID = 5;
    final public static int THIRTY_MINUTE_NOTIFICATION_ID = 1;
    final public static int SIXTY_MINUTE_NOTIFICATION_ID = 2;
    final public static int NINETY_MINUTE_NOTIFICATION_ID = 3;
    final public static int HUNDRED_TWENTY_MINUTE_NOTIFICATION_ID = 4;

    private Integer counterInt = 0;
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";
    private String group;
    private NotificationCompat.Builder builder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("We are in notification", "yay");
        String initialTimeStr = intent.getStringExtra("initial time");
        String broadcastId = intent.getStringExtra("broadcast Int");
        int notificationId = Integer.parseInt(broadcastId);

        switch (notificationId) {
            case MORNING_QUESTIONNAIRE_NOTIFICATION_ID:
                notifyTimeForMorningQuestionnaire(notificationId, broadcastId);
                break;

            case THIRTY_MINUTE_NOTIFICATION_ID:
            case SIXTY_MINUTE_NOTIFICATION_ID:
            case NINETY_MINUTE_NOTIFICATION_ID:
            case HUNDRED_TWENTY_MINUTE_NOTIFICATION_ID:
                notify30minutesPass(notificationId, initialTimeStr, broadcastId);
                break;
        }

        return START_NOT_STICKY; //START_REDELIVER_INTENT
    }

    //creates notification that appears when the morning alarm goes off
    private void notifyTimeForMorningQuestionnaire(int NOTIFICATION_ID, String broadcastId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        SharedPreferences groupSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        group = groupSharedPreferences.getString("Group", "none");

        Intent morningIntent = new Intent(this, MorningQS.class);
        morningIntent.putExtra("broadcast Int", broadcastId);
        PendingIntent morningIntent1 = PendingIntent.getActivity(this, 0, morningIntent, PendingIntent.FLAG_ONE_SHOT);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_statusbar_icon)
                .setContentTitle("Boozymeter")
                .setContentText("Time for morning questionnaire")
                .setAutoCancel(true);
        builder.setContentIntent(morningIntent1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, builder.build());
        } else {
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    //creates a notification for the alarms that occur during the night after 30 minutes has gone by
    private void notify30minutesPass(int NOTIFICATION_ID, String initialTimeStr, String broadcastId) {
        Intent yesIntent = new Intent(this, Main2Activity.class);
        yesIntent.putExtra("notificationBool", 100);
        yesIntent.putExtra("initial time", initialTimeStr);
        yesIntent.putExtra("broadcast Int", broadcastId);
        PendingIntent yesIntent1 = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.e("yesIntent1", "" + yesIntent1);

        Intent noIntent = new Intent(this, ButtonReceiver.class);
        noIntent.putExtra("notificationBool", 200);
        noIntent.putExtra("broadcast Int", broadcastId);
        PendingIntent noIntent1 = PendingIntent.getBroadcast(this, 0, noIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                    .setContentTitle("Boozymeter")
                    .setContentText("It's been 30 minutes! Have you had a drink?")
                    .addAction(R.drawable.check_small, "Yes", yesIntent1)
                    .addAction(R.drawable.cancel_small, "No", noIntent1)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            startForeground(NOTIFICATION_ID, builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.small_statusbar_icon)
                    .setContentTitle("Boozymeter")
                    .setContentText("It's been 30 minutes! Have you had a drink?")
                    .addAction(R.drawable.check_small, "Yes", yesIntent1)
                    .addAction(R.drawable.cancel_small, "No", noIntent1)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);


            NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
            nManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

}