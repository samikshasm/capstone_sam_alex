package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;

import java.util.Calendar;

public class BoozymeterApplication extends Application {
//    // Official settings
//    final public static long CYCLE_OFFSET = 5 * 60 * 60 * 1000; // 5 hours (from start of natural cycle)
//    final public static long CYCLE_LENGTH = 24 * 60 * 60 * 1000; // 1 day
//    final public static int NUM_CYCLES = 21; // 21 cycles
//        final public static long SURVEY_OFFSET = 27 * 60 * 60 * 1000; // 8 am (27 hours from user start time that includes CYCLE_OFFSET)
//    final public static long EVENING_REMINDER_OFFSET = 13 * 60 * 60 * 1000; // 6pm (13 hours from user start time that includes CYCLE_OFFSET)
//    final public static long IN_EPISODE_REMINDER_INTERVAL = 30 * 60 * 1000; // 30 minutes
//    final public static int NUM_IN_EPISODE_REMINDER = 3; // 3 reminders every IN_EPISODE_REMINDER_INTERVAL

    // Testing settings
    final public static long CYCLE_OFFSET = 10 * 60 * 1000;    // 1 mins
    final public static long CYCLE_LENGTH = 20 * 60 * 1000;    //  20 mins
    final public static int NUM_CYCLES = 3;
    final public static long SURVEY_OFFSET = 1 * 60 * 1000;  //  25 mins after cycle start
    final public static long EVENING_REMINDER_OFFSET = 12 * 60 * 1000;    // 12 mins from cycle time which is 3 hours from cycle offset start
    final public static long IN_EPISODE_REMINDER_INTERVAL = 2 * 60 * 1000;   // 2 mins
    final public static int NUM_IN_EPISODE_REMINDER = 3; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

    //  Dr. Goldwasser test settings (03/22/2019)
//    final public static long CYCLE_OFFSET = 1 * 60 * 60 * 1000;    // 1 hour
//    final public static long CYCLE_LENGTH = 6 * 60 * 60 * 1000;    //  6 hours
//    final public static int NUM_CYCLES = 12;
//    final public static long SURVEY_OFFSET = 8 * 60 * 60 * 1000;  //  8 hours after cycle start = 2 hours after END of a cycle = 1 hour after OFFSET cycle end?
//    final public static long EVENING_REMINDER_OFFSET = 4 * 60 * 60 * 1000;    // 4 hours from cycle time which is 3 hours from cycle offset start
//    final public static long IN_EPISODE_REMINDER_INTERVAL = 15 * 60 * 1000;   // 15 minutes
//    final public static int NUM_IN_EPISODE_REMINDER = 3; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

    // Global settings
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static long getNextMorningSurveyTimeInMillis(long userStartTime, int currentCycle) {
        return userStartTime + (currentCycle * BoozymeterApplication.CYCLE_LENGTH) + BoozymeterApplication.SURVEY_OFFSET;
    }
}
