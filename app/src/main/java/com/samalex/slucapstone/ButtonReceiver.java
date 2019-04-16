package com.samalex.slucapstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AlexL on 7/25/2017.
 */

//class that receives input from no button in notification
public class ButtonReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    @Override
    public void onReceive(Context context, Intent intent){

        String broadcastID = intent.getStringExtra("broadcast Int");
        int notificationId = Integer.parseInt(broadcastID);

        switch(notificationId) {
            case NotificationService.FIRST_IN_EPISODE_REMINDER_NOTIFICATION_ID:
            case NotificationService.SECOND_IN_EPISODE_REMINDER_NOTIFICATION_ID:
            case NotificationService.THIRD_IN_EPISODE_REMINDER_NOTIFICATION_ID:
            case NotificationService.EVENING_REMINDER_NOTIFICATION_ID:

                int buttonId = intent.getIntExtra("notification action button id", -1);
                if(buttonId != -1) {
                    if (buttonId == NotificationService.NOTIFICATION_NO_BUTTON_ID) {
                        NotificationService.dismissNotification(context, notificationId);
                        Log.e("canceling", broadcastID);
                    }
                }
                break;
        }
    }

}
