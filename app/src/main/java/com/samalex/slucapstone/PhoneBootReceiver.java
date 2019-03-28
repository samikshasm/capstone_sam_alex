package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class PhoneBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        //  execute when Boot Completed (after rebooting)

        /**Schedule your Alarm Here**/
        long eveningReminderTimeInMillis = getPendingEveningReminderTimeInMillis(context);
        if(eveningReminderTimeInMillis != 0) {
            createEveningDailyReminderAlarm(context, eveningReminderTimeInMillis);
        }

        long morningSurveyTimeInMillis = getPendingMorningAlarmTimeInMillis(context);
        if(morningSurveyTimeInMillis != 0) {
            createMorningAlarm(context, morningSurveyTimeInMillis);
        }

        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();
    }

    private void createEveningDailyReminderAlarm(Context context, long timeInMillis) {
        // Set an alarm to go off daily
        Intent alertIntent = new Intent(context, TimerReceiver.class);
        alertIntent.putExtra("broadcast Int", NotificationService.EVENING_REMINDER_NOTIFICATION_ID + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationService.EVENING_REMINDER_NOTIFICATION_ID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void createMorningAlarm(Context context, long timeInMillis) {
        Intent alertIntent = new Intent(context, TimerReceiver.class);
        AlarmManager morningAlarmMan = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alertIntent.putExtra("broadcast Int", NotificationService.MORNING_QUESTIONNAIRE_NOTIFICATION_ID + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 5, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        morningAlarmMan.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    private long getPendingEveningReminderTimeInMillis(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("boozymeter", MODE_PRIVATE);
        long timeInMillis = mSharedPreferences.getLong("pendingEveningReminderTimeInMillis", 0);
        return timeInMillis;
    }
    private long getPendingMorningAlarmTimeInMillis(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("boozymeter", MODE_PRIVATE);
        long timeInMillis = mSharedPreferences.getLong("pendingMorningAlarmTimeInMillis", 0);
        return timeInMillis;
    }
}
