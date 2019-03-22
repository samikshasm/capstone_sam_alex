package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
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
//    final public static int NUM_IN_EPISODE_REMINDER = 4; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

    // Testing settings
    final public static int CYCLE_OFFSET = 1 * 60 * 1000;  // 1 minutes (from start of natural cycle)
    final public static int CYCLE_LENGTH = 10 * 60 * 1000; // 10 minutes
    final public static int NUM_CYCLES = 3; // 3 cycles
    final public static int SURVEY_OFFSET = 14 * 60 * 1000; // at minute 15 (14 minutes from user start time that includes CYCLE_OFFSET)
    final public static int EVENING_REMINDER_OFFSET = 4 * 60 * 1000; // at minute 5 (4 minutes from user start time that includes CYCLE_OFFSET)
    final public static int IN_EPISODE_REMINDER_INTERVAL = 30 * 1000; // 30 seconds
    final public static int NUM_IN_EPISODE_REMINDER = 4; // 4 reminders every IN_EPISODE_REMINDER_INTERVAL

    // Global settings
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }
    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
