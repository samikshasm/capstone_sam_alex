package com.samalex.slucapstone;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String userIDMA;
    private String text;
    private EditText textbox;
    private int counter = 1;
    private String counterStr;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    protected Location mLastLocation;
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private String currentUserFromLA;
    private long timeCountInMilliSeconds = 1 * 60000;
    private ProgressBar progressBarCircle;
    private TextView editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private String mId;
    private Integer counterInt = 0;
    private Integer notificationInt =0;
    private AlarmManager alarmManager;
    private Intent resultIntent;
    private PendingIntent pIntent;
    private Integer numberDrinks = 0;
    private Integer notificationPops = 4;
    private String time;
    private long futureTimeDate;
    private Button btn_start,btn_cancel;
    private TextView tv_timer;
    String date_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    EditText et_hours;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private long initialTime;
    private String initialTimeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //initialize UI stuff
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        editTextMinute = (TextView) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);

        //initialize location something
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //getIntents
        mId = getIntent().getStringExtra("coming from start");
        Toast.makeText(this, ""+mId, Toast.LENGTH_SHORT).show();
        userIDMA = getIntent().getStringExtra("User ID");


        //two different ways of coming into the main activity
            //came from start activity
        if (mId != null) {
            if (mId.equals("start")) {
                initialTime = System.currentTimeMillis();
                initialTimeStr = Long.toString(initialTime);
                Toast.makeText(this, ""+initialTime, Toast.LENGTH_SHORT).show();

                createAlarms(initialTime);

                mId = "come from qs";
            }
            //coming from notification
            else if (mId.equals("come from qs")) {
                Toast.makeText(this, ""+userIDMA, Toast.LENGTH_SHORT).show();
                long notificationTime = System.currentTimeMillis();
                Toast.makeText(this, "notifc"+notificationTime, Toast.LENGTH_SHORT).show();
                cancelAlarm(1);
                cancelAlarm(2);
                cancelAlarm(3);
                cancelAlarm(4);

                //reset counter here!!@@!#fdidaj;fuadsfjadlsfj;klsadjf


                createAlarms(notificationTime);
            }
        }

        //if counter is greater than 4: stop Location updates and cancel alarms and switch to start activity

        //sign out button
        Button signOut = (Button) findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                currentUserFromLA = "signed out";
                //Toast.makeText(MainActivity.this, "main activity: "+currentUserFromLA,
                //     Toast.LENGTH_SHORT).show();
                Intent switchToLogin = new Intent(MainActivity.this, LoginActivity.class);
                switchToLogin.putExtra("sign out", currentUserFromLA);
                startActivity(switchToLogin);
                finish();
            }
        });


        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);

        updateUI();

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        mStartUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
                intent.putExtra("User ID", userIDMA);
                startService(intent);

                updateUI();

            }
        });



        mStopUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
                stopService(intent);

                updateUI();
            }
        });

       // numberDrinks = getIntent().getIntExtra("number of drinks",0);
        imageViewStartStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent switchToMain2Activity = new Intent(MainActivity.this, Main2Activity.class);
                switchToMain2Activity.putExtra("User ID", userIDMA);
                switchToMain2Activity.putExtra("initial time", initialTimeStr);
                startActivity(switchToMain2Activity);
                finish();
                //setTimerValues();
                //setProgressBarValues();
            }
        });

    }

    public void createAlarms(long currentTime) {
        long thirty = currentTime + 0;
        startAlarm(thirty, 1);

        long sixty = currentTime + 40000;
        startAlarm(sixty, 2);

        long ninety = currentTime + 50000;
        startAlarm(ninety,3);

        long one20 = currentTime + 60000;
        startAlarm(one20, 4);
    }

    public void startAlarm(long time, int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        resultIntent.putExtra("User ID", userIDMA);
        resultIntent.putExtra("initial time", initialTimeStr);

        //pass counterInt (STRINGGGGGG) to Notification SErvicea

        pIntent = PendingIntent.getBroadcast(MainActivity.this, broadcastID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
    }

    public void cancelAlarm(int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        pIntent = PendingIntent.getBroadcast(MainActivity.this, broadcastID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        pIntent.cancel();
    }

    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();

    }

    private void startStop() {

        imageViewStartStop.setImageResource(R.drawable.icon_start);

        setTimerValues();
        setProgressBarValues();
        startCountDownTimer();


        /*if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues();
            setProgressBarValues();
            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();
        }
        else {
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }*/
        /*if (timerStatus == TimerStatus.STOPPED) {
            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // making edit text not editable
            editTextMinute.setEnabled(false);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
        } else {
            // hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
            editTextMinute.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }*/

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int time = 20;
       /* if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }*/
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //long timeRemaining = futureTimeDate - System.currentTimeMillis();
                //Toast.makeText(MainActivity.this, ""+timeRemaining+ ","+ millisUntilFinished, Toast.LENGTH_SHORT).show();
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
                //imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
                //imageViewStartStop.setImageResource(R.drawable.icon_start);
                // making edit text editable
                //editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                //timerStatus = TimerStatus.STOPPED;

                if (counterInt < 4) {
                    /*
                    long currentDateTime = System.currentTimeMillis();
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
                    resultIntent.putExtra("number of drinks", numberDrinks);
                    pIntent = PendingIntent.getBroadcast(MainActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    need to cancel alarm at some point
                    alarmManager.set(AlarmManager.RTC_WAKEUP, currentDateTime, pIntent);
                    notificationPops = true;*/

                    Intent yesIntent = new Intent(MainActivity.this, Main2Activity.class);
                    //yesIntent.putExtra("notificationBool",100);
                   // yesIntent.putExtra("number of drinks", numberDrinks);
                    PendingIntent yesIntent1 = PendingIntent.getActivity(MainActivity.this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.app_icon_small)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small))
                            .setContentTitle("Boozymeter")
                            .setContentText("It's been 30 minutes! Have you had a drink?")
                            .addAction(0, "Yes", yesIntent1)
                            .setFullScreenIntent(yesIntent1, true)
                            //.setFullScreenIntent(noIntent1, true)
                            .setAutoCancel(true);

                    // builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    int NOTIFICATION_ID = 12345;

                    //builder.setContentIntent(yesIntent);
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nManager.notify(NOTIFICATION_ID, builder.build());

                    notificationPops--;
                    reset();
                }

                else {
                    notificationPops = 4;
                    stopCountDownTimer();
                    // alarmManager.cancel(pIntent);
                    // notificationInt = 0;
                    //counterInt = 0;
                }
            }

        }.start();
        countDownTimer.start();
        counterInt++;


    }

    /**
     * method to stop count down timer
     */
    public void stopCountDownTimer() {

        countDownTimer.cancel();

    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(){
        if (Utils.isMyServiceRunning(this, BackgroundLocationService.class)){
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        }else{
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        //insertDummyLocationWrapper();
        if (!checkPermissions()) {
            requestPermissions();
        }else{
            getLastLocation();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
       // AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //PendingIntent pIntent = PendingIntent.getBroadcast(MainActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       /* if (counter < 4) {
            for (int i= 0; i < 4-notificationPops; i++) {
                futureTimeDate = System.currentTimeMillis() + 5 * 1000;
                Intent resultIntent = new Intent(this, TimerReceiver.class);
                //need to cancel alarm at some point
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureTimeDate, pIntent);
            }
        }
        else {
            alarmManager.cancel(pIntent);
            counter = 0;
        }*/
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation(){
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>(){
            @Override
            public void onComplete(@NonNull Task<Location> task){
                if (task.isSuccessful() && task.getResult() != null){
                    mLastLocation = task.getResult();

                }else{
                    Log.w(TAG, "getLastLocation:exception", task.getException());
                    showSnackbar(getString(R.string.no_location_detected));
                }
            }
        });
    }

    private void showSnackbar(final String text){
        View container = findViewById(R.id.main_activity_container);
        if (container != null){
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener){
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE).setAction(getString(actionStringId),listener).show();
    }

    private boolean checkPermissions(){
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // Permission denied.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

}