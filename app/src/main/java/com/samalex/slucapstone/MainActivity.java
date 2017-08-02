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
import android.os.Handler;
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
    private Integer notificationInt = 0;
    private AlarmManager alarmManager;
    private Intent resultIntent;
    private PendingIntent pIntent;
    private Integer numberDrinks = 0;
    private Integer notificationPops = 4;
    private String time;
    private long futureTimeDate;
    private Button btn_start, btn_cancel;
    private TextView tv_timer;
    String date_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    EditText et_hours;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private long initialTime;
    private String initialTimeStr;
    private Handler handler = new Handler();
    private Intent intent;
    public static final String MY_ACTION = "hello";
    private String last_activity;
    private String startActivity1;

    public static final String startActivity = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference();



/*        if (startActivity1.equals("start")) {
            Intent goToStart = new Intent(MainActivity.this, StartActivity.class);
            goToStart.putExtra("Start Activity", startActivity);
            startActivity(goToStart);
            finish();
        }*/

        //initialize UI stuff
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);

        //initialize location something
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
        //Toast.makeText(this, ""+mId, Toast.LENGTH_SHORT).show();
        /*userIDMA = getIntent().getStringExtra("User ID");
        if(userIDMA != null) {
            Log.e("User ID Main", userIDMA);
        }*/

        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        startActivity1 = getIntent().getStringExtra("Start Activity");
        startActivity1 = "main";
        storeScreen(startActivity1);


        //two different ways of coming into the main activity
        //came from start activity
        if (mId != null) {
            if (mId.equals("start")) {
                initialTime = System.currentTimeMillis();
                initialTimeStr = Long.toString(initialTime);
                createAlarms(initialTime);
                createMorningAlarm();
                startUIUpdateService();
                startLocationUpdates(userIDMA);
                mId = "come from qs";
            }

            //coming from notification
            else if (mId.equals("come from qs")) {
                long notificationTime = System.currentTimeMillis();
                cancelAlarm(1);
                cancelAlarm(2);
                cancelAlarm(3);
                cancelAlarm(4);
                stopUIUpdateService();
                startUIUpdateService();
                createAlarms(notificationTime);
            }
        }

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
                //goToStart.putExtra("User ID", userIDMA);
                startActivity(goToStart);
                finish();
            }
        };
        goToStart.setOnClickListener(handler1);


        // numberDrinks = getIntent().getIntExtra("number of drinks",0);
        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "what tf is going on", Toast.LENGTH_SHORT).show();
                Intent switchToMain2Activity = new Intent(MainActivity.this, Main2Activity.class);
                //switchToMain2Activity.putExtra("User ID", userIDMA);
                // switchToMain2Activity.putExtra("initial time", initialTimeStr);
                startActivity(switchToMain2Activity);
                finish();
                //setTimerValues();
                //setProgressBarValues();
            }
        });

    }

    private void startLocationUpdates(String userID){
        //Toast.makeText(this, "startingLocation", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,userID,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        //intent.putExtra("User ID", userID);
        startService(intent);

        //updateUI();
    }

    private void stopLocationUpdates(){
        //Toast.makeText(this, "stopping location", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        stopService(intent);

        //updateUI();
    }

    private void createMorningAlarm () {
        Calendar morningCal = Calendar.getInstance();
        int day = morningCal.get(Calendar.DAY_OF_WEEK);
        morningCal.set(Calendar.HOUR_OF_DAY, 12);
        morningCal.set(Calendar.MINUTE, 41);
        morningCal.set(Calendar.SECOND, 0);
        //int hour = morningCal.get(Calendar.HOUR);
        //int minute = morningCal.get(Calendar.MINUTE);
        //Toast.makeText(this, hour+":"+minute, Toast.LENGTH_SHORT).show();
        Intent alertIntent = new Intent(this, TimerReceiver.class);
        AlarmManager morningAlarmMan = (AlarmManager) getSystemService(ALARM_SERVICE);
        alertIntent.putExtra("broadcast Int", "5");
        //alertIntent.putExtra("User ID", userIDMA);
        alertIntent.putExtra("initial time", initialTimeStr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 5, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        morningAlarmMan.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }
    public void writeToDB(String text1) {
        DatabaseReference mRef = mDatabase.child("Users").child(userIDMA).child("Location").child(time);
        mRef.setValue(text1);

    }

    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }

    private String getScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("currentScreen", "none");
        return selectedScreen;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            testUpdateUI(intent);
        }
    };

    public void startUIUpdateService() {
        startService(new Intent(this, BroadcastService.class));
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    public void stopUIUpdateService() {
        stopService(new Intent(this, BroadcastService.class));
        //unregisterReceiver(broadcastReceiver);
    }


    private void testUpdateUI(Intent intent) {
        String secondsStr = intent.getStringExtra("milliseconds");
        int seconds = Integer.parseInt(secondsStr);
        int minutes = seconds / 60;
        int seconds2 = seconds % 60;
        String hms = String.format("%02d:%02d", minutes, seconds2);
        textViewTime.setText(hms);
        progressBarCircle.setMax(1800);
        progressBarCircle.setProgress(seconds);

    }

    public void createAlarms(long currentTime) {
        long thirty = currentTime + 10000;
        startAlarm(thirty, 1);

        long sixty = currentTime + 20000;
        startAlarm(sixty, 2);

        long ninety = currentTime + 30000;
        startAlarm(ninety, 3);

        long one = currentTime + 40000;
        startAlarm(one, 4);
    }

    public void startAlarm(long time, int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        //resultIntent.putExtra("User ID", userIDMA);
        resultIntent.putExtra("initial time", initialTimeStr);
        resultIntent.putExtra("broadcast Int", "" + broadcastID);
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


    @Override
    public void onResume() {
        startUIUpdateService();
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        SharedPreferences mSharedPreferences1 = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences1.getString("currentScreen", "none");
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

    private void updateUI() {
        if (Utils.isMyServiceRunning(this, BackgroundLocationService.class)) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

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