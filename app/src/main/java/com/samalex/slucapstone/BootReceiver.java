package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if(!(getCurrentScreen(context).equals("login"))) {
                // Set the alarm here.
                createAllDailyAlarms(context);
            }
        }
    }

    private void createAllDailyAlarms(Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        // evening reminder alarms
        long cycleEveningReminderTime = calculateEveningReminderTime(context);
        calendar.setTimeInMillis(cycleEveningReminderTime);

        Intent eveningAlertIntent = new Intent(context, TimerReceiver.class);
        eveningAlertIntent.putExtra("broadcast Int", NotificationService.EVENING_REMINDER_NOTIFICATION_ID + "");

        PendingIntent eveningPendingIntent = PendingIntent.getBroadcast(context, NotificationService.EVENING_REMINDER_NOTIFICATION_ID, eveningAlertIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), BoozymeterApplication.CYCLE_LENGTH, eveningPendingIntent);
        Log.e("evening reminder alarm", dateFormat.format(new Date(cycleEveningReminderTime)));

//         (next) morning survey alarms
        long cycleMorningSurveyTime = calculateMorningSurveyTime(context);
        calendar.setTimeInMillis(cycleMorningSurveyTime);

        Intent morningAlertIntent = new Intent(context, TimerReceiver.class);
        morningAlertIntent.putExtra("broadcast Int", NotificationService.MORNING_QUESTIONNAIRE_NOTIFICATION_ID + "");

        PendingIntent morningPendingIntent = PendingIntent.getBroadcast(context, NotificationService.MORNING_QUESTIONNAIRE_NOTIFICATION_ID, morningAlertIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager morningSurveyAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        morningSurveyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), BoozymeterApplication.CYCLE_LENGTH, morningPendingIntent);
        Log.e("morning survey alarm", dateFormat.format(new Date(cycleMorningSurveyTime)));
    }

    private long calculateEveningReminderTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long userStartTime = getUserStartTime(context);

        int currentCycle = CalculationUtil.updateAndGetCurrentCycle(context);
        long reminderTime = userStartTime + (currentCycle * BoozymeterApplication.CYCLE_LENGTH) + BoozymeterApplication.EVENING_REMINDER_OFFSET;
        if (reminderTime < now) {
            reminderTime += BoozymeterApplication.CYCLE_LENGTH;
        }

        return reminderTime;
    }

    private long calculateMorningSurveyTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long userStartTime = getUserStartTime(context);

        int currentCycle = CalculationUtil.updateAndGetCurrentCycle(context);
        long reminderTime = userStartTime + (currentCycle * BoozymeterApplication.CYCLE_LENGTH) +  + BoozymeterApplication.SURVEY_OFFSET;
        if (reminderTime < now) {
            reminderTime += BoozymeterApplication.CYCLE_LENGTH;
        }

        return reminderTime;
    }

    private Long getUserStartTime(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userStartTime", 0);
        return time;
    }

    private String getCurrentScreen(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("screen", MODE_PRIVATE);
        return mSharedPreferences.getString("currentScreen", "none");
    }
}
