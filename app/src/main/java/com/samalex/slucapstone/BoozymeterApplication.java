package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;

public class BoozymeterApplication extends Application {
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

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

}
