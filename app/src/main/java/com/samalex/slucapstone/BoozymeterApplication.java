package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.TimeUnit;

import java.util.Calendar;

public class BoozymeterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Create an evening reminder alarm that goes of daily
        startEveningDailyReminderAlarm();
    }


    private void startEveningDailyReminderAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (!isDebug) {
            // Set the alarm to start at 6:00 p.m.
            calendar.set(Calendar.HOUR_OF_DAY, 18);
        } else {
            // Set the alarm to start 10 seconds later
            calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 10);
        }

        // Set an alarm to go off daily
        Intent alertIntent = new Intent(this, TimerReceiver.class);
        alertIntent.putExtra("broadcast Int", NotificationService.EVENING_REMINDER_NOTIFICATION_ID + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NotificationService.EVENING_REMINDER_NOTIFICATION_ID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (!isDebug) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES / 60, pendingIntent); // every 15 seconds
        }
    }

    // Official settings
    final public static int CYCLE_OFFSET = 5 * 60 * 60 * 1000; // 5 hours (from start of natural cycle)
    final public static int CYCLE_LENGTH = 24 * 60 * 60 * 1000; // 1 day
    final public static int NUM_CYCLES = 21; // 21 cycles
    final public static int SURVEY_OFFSET = 8 * 60 * 60 * 1000; // 8 hours (from start of natural cycle)
    final public static int EVENING_REMINDER_OFFSET = 18 * 60 * 60 * 1000; // 18 hours (from start of natural cycle)
    final public static int IN_EPISODE_REMINDER_INTERVAL = 30 * 60 * 1000; // 30 minutes
    final public static int NUM_IN_EPISODE_REMINDER = 4; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

//    // Testing settings
//    final public static int CYCLE_OFFSET = 5 * 60 * 1000;  // 5 minutes (from start of natural cycle)
//    final public static int CYCLE_LENGTH = 24 * 60 * 1000; // 24 minutes
//    final public static int NUM_CYCLES = 3; // 3 cycles
//    final public static int SURVEY_OFFSET = 8 * 60 * 1000; // 8 minutes (from start of natural cycle)
//    final public static int EVENING_REMINDER_OFFSET = 18 * 60 * 1000; // 18 minutes (from start of natural cycle)
//    final public static int IN_EPISODE_REMINDER_INTERVAL = 30 * 1000; // 30 seconds
//    final public static int NUM_IN_EPISODE_REMINDER = 4; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

    // Global settings
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }
    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
