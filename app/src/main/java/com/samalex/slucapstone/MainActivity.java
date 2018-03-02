package com.samalex.slucapstone;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//suna
import static com.samalex.slucapstone.BroadcastService.STARTED_TIME_IN_MILLIS;

import java.util.Calendar;



public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String userIDMA;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    protected Location mLastLocation;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewStartStop;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String mId;
    private AlarmManager alarmManager;
    private Intent resultIntent;
    private PendingIntent pIntent;
    private long initialTime;
    private String initialTimeStr;
    private String startActivity1;
    public static final long ALARM_TIME = 1800000;
    public static final long PROGRESS_BAR_MAX = 1800;


    public static final String startActivity = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //set the screen orientation so that user cannot turn phone and screen will turn
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        //initialize layout and database
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //initialize UI stuff
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);

        //initializes location client
        //requests permission from phone to access users location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        //getIntents
        mId = getIntent().getStringExtra("coming from start");
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        //tells the app that the current screen is "main"
        //store the current screen in a shared preferences variable
        startActivity1 = getIntent().getStringExtra("Start Activity");
        startActivity1 = "main";
        storeScreen(startActivity1);


        //two different ways of coming into the main activity
        //came from start activity
        //initializes alarms and services
        if (mId != null) {
            if (mId.equals("start")) {
                initialTime = System.currentTimeMillis();
                initialTimeStr = Long.toString(initialTime);
                createAlarms(initialTime);
                createMorningAlarm();
                startUIUpdateService(initialTime);
                startLocationUpdates(userIDMA);
                mId = "come from qs";
            }

            //if user is coming from notification, cancel all previously set alarms and set new ones
            //resets ui update service
            else if (mId.equals("come from qs")) {
                long notificationTime = System.currentTimeMillis();
                cancelAlarm(1);
                cancelAlarm(2);
                cancelAlarm(3);
                cancelAlarm(4);
                stopUIUpdateService();
                startUIUpdateService(notificationTime);
                createAlarms(notificationTime);
            }
        }

        //creates a toolbar and click listener at the bottom of the screen to stop the main activity
        //stops all alarms except morning alarm
        //stops ui updates
        //stops location updates
        //stores the current screen variable to shared preferences
        //switches to start activity
        Toolbar goToStart = (Toolbar) findViewById(R.id.go_to_start);
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                stopUIUpdateService();
                cancelAlarm(1);
                cancelAlarm(2);
                cancelAlarm(3);
                cancelAlarm(4);
                startActivity1 = "start";
                storeScreen(startActivity1);
                stopLocationUpdates();
                unregisterReceiver(broadcastReceiver);
                Intent goToStart = new Intent(MainActivity.this, StartActivity.class);
                goToStart.putExtra("Start Activity", startActivity);
                startActivity(goToStart);
                finish();
            }
        };
        goToStart.setOnClickListener(handler1);

        //on click listener for add drink button
        //unregisters ui updates receiver
        //switches activity from main to main2
        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unregisterReceiver(broadcastReceiver);
                Intent switchToMain2Activity = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(switchToMain2Activity);
                finish();
            }
        });

    }

    //starts the location updates service which updates the database at given interval
    //starts BackgroundLocationService class
    private void startLocationUpdates(String userID){
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        startService(intent);
    }

    //stops the location updates service
    private void stopLocationUpdates(){
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        stopService(intent);

    }

    //creates the morning alarm manager to go off the following morning
    //adapted code from Alarm Manager Android Developer page
    private void createMorningAlarm () {
        Calendar morningCal = Calendar.getInstance();
        int hour = morningCal.get(Calendar.HOUR_OF_DAY);
        int day;
        if (hour >= 0 && hour < 7){
            day = morningCal.get(Calendar.DAY_OF_WEEK);
        }else{
            day = morningCal.get(Calendar.DAY_OF_WEEK);
            day = day+1;
        }
        morningCal.set(Calendar.DAY_OF_WEEK, day);
        morningCal.set(Calendar.HOUR_OF_DAY, 2);
        morningCal.set(Calendar.MINUTE, 12);
        morningCal.set(Calendar.SECOND, 0);

        Intent alertIntent = new Intent(this, TimerReceiver.class);
        AlarmManager morningAlarmMan = (AlarmManager) getSystemService(ALARM_SERVICE);
        alertIntent.putExtra("broadcast Int", "5");
        //alertIntent.putExtra("User ID", userIDMA);
        alertIntent.putExtra("initial time", initialTimeStr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 5, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        morningAlarmMan.set(AlarmManager.RTC_WAKEUP, morningCal.getTimeInMillis(), pendingIntent);
    }

    //function to store the current screen to the shared preference screen variable
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }


    //initializes broadcast receiver to run ui service
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            testUpdateUI(intent);
        }
    };


    //starts the ui update service in the background
    public void startUIUpdateService(long startedTime) {
        //make started time a shared preference
        Intent intent = new Intent(this, BroadcastService.class);
        storeStartedTime(startedTime);
        //intent.putExtra(STARTED_TIME_IN_MILLIS, startedTime);
        //startService(new Intent(this, BroadcastService.class));
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    //stops the timer ui update service
    public void stopUIUpdateService() {
        stopService(new Intent(this, BroadcastService.class));
    }

    private void storeStartedTime(long startedTimeInMillis) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Started Time", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("Started Time", startedTimeInMillis);
        mEditor.apply();
    }


    //updates the timer ui and sets the progress bar value
    private void testUpdateUI(Intent intent) {
        String secondsStr = intent.getStringExtra("milliseconds");
        int seconds = Integer.parseInt(secondsStr);
        int minutes = seconds / 60;
        int seconds2 = seconds % 60;
        String hms = String.format("%02d:%02d", minutes, seconds2);
        textViewTime.setText(hms);
        progressBarCircle.setMax((int)PROGRESS_BAR_MAX);
        progressBarCircle.setProgress(seconds);

    }

    //function to create alarms at given time
    public void createAlarms(long currentTime) {
        long thirty = currentTime + ALARM_TIME;
        //1800000
        startAlarm(thirty, 1);

        long sixty = currentTime + (ALARM_TIME*2);
        //3600000
        startAlarm(sixty, 2);

        long ninety = currentTime + (ALARM_TIME*3);
        //5400000
        startAlarm(ninety, 3);

        long one = currentTime + (ALARM_TIME*4);
        //7200000
        startAlarm(one, 4);
    }

    //function to start alarm manager at given time
    public void startAlarm(long time, int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        //resultIntent.putExtra("User ID", userIDMA);
        resultIntent.putExtra("initial time", initialTimeStr);
        resultIntent.putExtra("broadcast Int", "" + broadcastID);
        pIntent = PendingIntent.getBroadcast(MainActivity.this, broadcastID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);


    }

    //function to cancel alarm manager with the given broadcast id
    public void cancelAlarm(int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        pIntent = PendingIntent.getBroadcast(MainActivity.this, broadcastID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        pIntent.cancel();

    }


    //every time the app resumes, this function is called
    @Override
    public void onResume() {
        long startedTimeInMillis = getStartedTime();
        //start the ui update service when the app is resumed to update ui timer
        startUIUpdateService(startedTimeInMillis);
        //checks to see which screen is set in the shared preferences screen variable
        SharedPreferences mSharedPreferences1 = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences1.getString("currentScreen", "none");
        //if the screen has been set to start or morning qs, the activity changes
        //receivers and services are stopped and activity is switched back to the start screen
        if (selectedScreen.equals("start") | selectedScreen.equals("morningQS")) {
            stopUIUpdateService();
            stopLocationUpdates();
            unregisterReceiver(broadcastReceiver);
            Intent goToStart = new Intent(MainActivity.this, StartActivity.class);
            goToStart.putExtra("Start Activity", startActivity);
            startActivity(goToStart);
            finish();
        }

        super.onResume();
    }

    private long getStartedTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Started Time", MODE_PRIVATE);
        long startedTimeMillis = mSharedPreferences.getLong("Started Time", 0);
        return startedTimeMillis;
    }


    //LOOK HERE TO SEE LOCATION STUFF

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //checks if the users location is turned on every time the onstart is called
    @Override
    public void onStart() {
        super.onStart();

        //insertDummyLocationWrapper();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }

    }


    //code to check if the user has granted permission for the app to use location
    //shows snackbar if the user has not granted the phone access for location
    //adapted code from googleSamples android-play-location open source library

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();

                } else {
                    Log.w(TAG, "getLastLocation:exception", task.getException());
                    showSnackbar(getString(R.string.no_location_detected));
                }
            }
        });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE).setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
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