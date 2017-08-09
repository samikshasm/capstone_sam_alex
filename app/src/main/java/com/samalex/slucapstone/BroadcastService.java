package com.samalex.slucapstone;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by AlexL on 7/19/2017.
 */

public class BroadcastService extends Service {
    public static final String STARTED_TIME_IN_MILLIS = "STARTED_TIME_IN_MILLIS";
    public static final long NUMBER_OF_MINUTES = 1800;
    private static final String TAG = "BroadcastService";
    private final Handler handler = new Handler();
    public static final String BROADCAST_ACTION = "com.samalex.slucapstone.displayevent";
    Intent intent;
    long milliSeconds = 1800;


    @Override
    public void onCreate(){
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (intent != null) {
            // long startedTimeInMillis = intent.getLongExtra(STARTED_TIME_IN_MILLIS, System.currentTimeMillis());
            long startedTimeInMillis = getStartedTime();
            long elapsedSeconds = (System.currentTimeMillis() - startedTimeInMillis) / 1000L;
            if (elapsedSeconds < NUMBER_OF_MINUTES) {
                milliSeconds = NUMBER_OF_MINUTES - elapsedSeconds;
            }else if (elapsedSeconds >= NUMBER_OF_MINUTES && elapsedSeconds < (NUMBER_OF_MINUTES*2)){
                elapsedSeconds = elapsedSeconds - NUMBER_OF_MINUTES;
                milliSeconds = NUMBER_OF_MINUTES - elapsedSeconds;

            }
            else if(elapsedSeconds >= (NUMBER_OF_MINUTES*2) && elapsedSeconds < (NUMBER_OF_MINUTES*3)){
                elapsedSeconds = elapsedSeconds - (NUMBER_OF_MINUTES);
                milliSeconds = (NUMBER_OF_MINUTES*2) - elapsedSeconds;
            }
            else if(elapsedSeconds >= (NUMBER_OF_MINUTES*3) && elapsedSeconds < (NUMBER_OF_MINUTES*4)){
                elapsedSeconds = elapsedSeconds - (NUMBER_OF_MINUTES);
                milliSeconds = (NUMBER_OF_MINUTES*3) - elapsedSeconds;
            }
        }
        handler.removeCallbacks(sendUpdatesToUI);
        handler.post(sendUpdatesToUI);
        //handler.postDelayed(sendUpdatesToUI, 1000);
        //return START_NOT_STICKY;

        return START_REDELIVER_INTENT;
    }

    private long getStartedTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Started Time", MODE_PRIVATE);
        long startedTimeMillis = mSharedPreferences.getLong("Started Time", 0);
        return startedTimeMillis;
    }

    private Runnable sendUpdatesToUI = new Runnable(){
        public void run(){
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000);
        }
    };

    private void DisplayLoggingInfo(){
        if(milliSeconds < 0){
            milliSeconds = NUMBER_OF_MINUTES;
        }
        long millis = milliSeconds--;
        Log.e("DisplayingLog", ""+millis);
        intent.putExtra("milliseconds", String.valueOf(millis));
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(sendUpdatesToUI);
        //handler.postDelayed(sendUpdatesToUI, 1000);
        super.onDestroy();
    }


}