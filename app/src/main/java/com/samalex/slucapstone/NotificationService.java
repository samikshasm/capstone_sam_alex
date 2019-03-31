package com.samalex.slucapstone;

import android.annotation.TargetApi;
import android.app.AlarmManager;
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
    final public static int THIRTY_MINUTE_NOTIFICATION_ID = 1;
    final public static int SIXTY_MINUTE_NOTIFICATION_ID = 2;
    final public static int NINETY_MINUTE_NOTIFICATION_ID = 3;
    final public static int HUNDRED_TWENTY_MINUTE_NOTIFICATION_ID = 4;
    final public static int MORNING_QUESTIONNAIRE_NOTIFICATION_ID = 5;
    final public static int EVENING_REMINDER_NOTIFICATION_ID = 6;

    private Integer counterInt = 0;
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";
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
            case EVENING_REMINDER_NOTIFICATION_ID:
//                if(getNightCount() == 0) {
                notifyEveningReminder(notificationId, broadcastId);
//                }
                break;
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

    public static void dismissNotification(Context context, int notificationId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
    }

    //creates notification that appears when the morning alarm goes off
    private void notifyTimeForMorningQuestionnaire(int notificationId, String broadcastId) {
        Intent morningIntent = new Intent(this, MorningQS.class);
        morningIntent.putExtra("broadcast Int", broadcastId);
        PendingIntent morningIntent1 = PendingIntent.getActivity(this, 0, morningIntent, PendingIntent.FLAG_ONE_SHOT);

        if (isAndroid8OrLater()) {
            NotificationChannel channel = getNotificationChannel();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = createNotificationBuilder(morningIntent1, "Time for morning questionnaire");
            startForeground(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = createNotificationBuilder(morningIntent1, "Time for morning questionnaire");
            NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
            nManager.notify(notificationId, builder.build());
        }
    }

    //creates a notification for the alarms that occur during the night after 30 minutes has gone by
    private void notify30minutesPass(int notificationId, String initialTimeStr, String broadcastId) {
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

        if (isAndroid8OrLater()) {
            NotificationChannel channel = getNotificationChannel();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = createNotificationBuilder(yesIntent1, noIntent1, "It's been 30 minutes! Have you had a drink?");
            startForeground(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = createNotificationBuilder(yesIntent1, noIntent1, "It's been 30 minutes! Have you had a drink?");
            NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
            nManager.notify(notificationId, builder.build());
        }
    }

    private boolean isAndroid8OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private void notifyEveningReminder(int notificationId, String broadcastId) {
        Intent eveningReminderIntent = new Intent(this, StartActivity.class);
        eveningReminderIntent.putExtra("broadcast Int", broadcastId);
        PendingIntent eveningReminderPendingIntent = PendingIntent.getActivity(this, 0, eveningReminderIntent, PendingIntent.FLAG_ONE_SHOT);

        if (isAndroid8OrLater()) {
            NotificationChannel channel = getNotificationChannel();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = createNotificationBuilder(eveningReminderPendingIntent, "Are you going to have a drink today?");
            startForeground(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = createNotificationBuilder(eveningReminderPendingIntent, "Are you going to have a drink today?");
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(notificationId, builder.build());
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannel getNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(getString(R.string.channel_description));
        channel.enableLights(true);
        channel.enableVibration(true);
        return channel;
    }

    private NotificationCompat.Builder createNotificationBuilder(PendingIntent morningIntent1, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_statusbar_icon)
                .setContentTitle("Boozymeter")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(morningIntent1);

        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        return builder;
    }

    private NotificationCompat.Builder createNotificationBuilder(PendingIntent yesIntent1, PendingIntent noIntent1, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_statusbar_icon)
                .setContentTitle("Boozymeter")
                .setContentText(contentText)
                .addAction(R.drawable.check_small, "Yes", yesIntent1)
                .addAction(R.drawable.cancel_small, "No", noIntent1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        return builder;
    }

//    private Integer getNightCount() {
//        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
//        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
//        return nightCount;
//    }
}