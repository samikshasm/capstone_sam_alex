package com.samalex.slucapstone;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by AlexL on 7/19/2017.
 */

public class BroadcastService extends Service {
    public static final String STARTED_TIME_IN_MILLIS = "STARTED_TIME_IN_MILLIS";
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
            long startedTimeInMillis = intent.getLongExtra(STARTED_TIME_IN_MILLIS, System.currentTimeMillis());
            long elapsedSeconds = (System.currentTimeMillis() - startedTimeInMillis) / 1000L;
            if (elapsedSeconds < 1800) {
                milliSeconds -= elapsedSeconds;
            }
        }
        handler.removeCallbacks(sendUpdatesToUI);
        handler.post(sendUpdatesToUI);
        return START_REDELIVER_INTENT;
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