package com.samalex.slucapstone;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by AlexL on 7/19/2017.
 */

public class BroadcastService extends Service {
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
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
        return START_NOT_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable(){
        public void run(){
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000);
        }
    };

    private void DisplayLoggingInfo(){
        if(milliSeconds < 0){
            milliSeconds = 1800;
        }
        Log.d(TAG, "entered DisplayLoggingInfo");
        long millis = milliSeconds--;
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