package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.samalex.slucapstone.dto.DrinkAnswer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // constants
    public static final String startActivity = "main";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final long ALARM_TIME = 1800000;

    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private DatabaseReference mReference;
    private AlarmManager alarmManager;
    private Intent resultIntent;
    private PendingIntent pIntent;

    private String userIDMA;
    private String mId;
    private long initialTime;
    private String initialTimeStr;
    private String startActivity1;
    private Integer nightCount;
    private String[] typeList;
    private String[] sizeList;
    private Integer totalCalConsumed;

    // UI
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewStartStop;
    private TextView cost_txt;
    private TextView cal_text;
    private TextView num_drink_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //set the screen orientation so that user cannot turn phone and screen will turn
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        //initialize layout and database
        setContentView(R.layout.activity_main);

        //initialize UI stuff
        //progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        //textViewTime = (TextView) findViewById(R.id.textViewTime);

        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);
/*
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
        }*/
        mReference = FirebaseDatabase.getInstance().getReference();

        nightCount = getNightCount();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //getIntents
        mId = getIntent().getStringExtra("coming from start");
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        if (userIDMA != null) {
            Log.e("User ID Start", userIDMA);
        } else if (userIDMA == null | userIDMA.equals("none")) {
            storeScreen("login");
        }

        cal_text = (TextView) findViewById(R.id.cal_text);
        num_drink_text = (TextView) findViewById(R.id.num_drinks_txt);
        cost_txt = (TextView) findViewById(R.id.cost_text);

        showHideLiveReportData();

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
                //startUIUpdateService(initialTime);
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates(userIDMA);
                }
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
                //stopUIUpdateService();
                //startUIUpdateService(notificationTime);
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
                //stopUIUpdateService();
                cancelAlarm(1);
                cancelAlarm(2);
                cancelAlarm(3);
                cancelAlarm(4);
                startActivity1 = "start";
                storeScreen(startActivity1);
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    stopLocationUpdates();
                }
                //unregisterReceiver(broadcastReceiver);
                Intent goToStart = new Intent(MainActivity.this, StartActivity.class);
                goToStart.putExtra("Cancel main activity", startActivity);
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
                //unregisterReceiver(broadcastReceiver);
                Intent switchToMain2Activity = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(switchToMain2Activity);
                finish();
            }
        });
        ImageView appHeaderBar = findViewById(R.id.main_app_header_bar);
        appHeaderBar.setOnClickListener(new View.OnClickListener() {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long userRawStartTime = getUserRawStartTime();
            String userRawStartDateStr = dateFormat.format(new Date(userRawStartTime));
            long userStartTime = getUserStartTime();
            String userStartDateStr = dateFormat.format(new Date(userStartTime));

            long morningSurveyTimeInMillis = BoozymeterApplication.getNextMorningSurveyTimeInMillis(getUserStartTime(), CalculationUtil.updateAndGetCurrentCycle(getApplicationContext()));
            String moringSurveyTime = dateFormat.format(new Date(morningSurveyTimeInMillis));
            String eveningReminderTime = dateFormat.format(new Date(calculateEveningReminderTime()));

            @Override
            public void onClick(View view) {
                int currentCycle = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext());
                InterventionDisplayData ui = CalculationUtil.getInterventionMap(getApplicationContext()).get(currentCycle);
                String liveReportFlag;
                String morningReportFlag;

                // TODO: investigate more which flow cause this null
                if (ui == null) {
                    liveReportFlag = "precomputed map is null";
                    morningReportFlag = "precomputed map is null";
                } else {
                    liveReportFlag = ui.isShowLiveReport() ? "Yes" : "No";
                    morningReportFlag = ui.isShowMorningReport() ? "Yes" : "No";
                }

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                builder.setTitle("Hidden Logs")
                        .setMessage("Username: " + userIDMA
                                + "\nUser group: " + getGroup()
                                + "\n"
                                + "\nRaw start time: " + userRawStartDateStr
                                + "\nCanonical start time (incl. cycle offset): " + userStartDateStr
                                + "\n"
                                + "\nCurrent Cycle: " + (currentCycle + 1)
                                + "\n"
                                + "\nLive report: " + liveReportFlag
                                + "\nMorning report: " + morningReportFlag
                                + "\n"
                                + "\nNumber of Episodes: " + getNightCount()
                                + "\n"
                                + "\nNext morning Survey alarm will go off: " + moringSurveyTime
                                + "\nNext evening reminder will go off: " + eveningReminderTime
                                + "\n\n--------- Settings ---------"
                                + "\nNumber of cycles: " + BoozymeterApplication.NUM_CYCLES
                                + "\nCycle length: " + BoozymeterApplication.CYCLE_LENGTH / 1000 / 60 + " minutes"
                                + "\nCycle offset: " + BoozymeterApplication.CYCLE_OFFSET / 1000 / 60 + " minutes"
                                + "\nEvening reminder offset: " + BoozymeterApplication.EVENING_REMINDER_OFFSET / 1000 / 60 + " minutes"
                                + "\nMorning survey offset: " + BoozymeterApplication.SURVEY_OFFSET / 1000 / 60 + " minutes"
                                + "\nIn-episode reminder offset: " + BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL / 1000 / 60 + " minutes"
                                + "\n"
                        )
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(R.drawable.white_small_icon)
                        .show();
            }
        });
    }

    //gets number of drinks from shared preferences
    private Integer getNumDrinks() {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks", 0);
        return numberDrinks;
    }

    private String getGroup() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        String group = mSharedPreferences.getString("Group", "none");
        return group;
    }

    private long calculateEveningReminderTime() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long userStartTime = getUserStartTime();


        long reminderTime = userStartTime + BoozymeterApplication.EVENING_REMINDER_OFFSET;
        if (reminderTime < now) {
            reminderTime += BoozymeterApplication.CYCLE_LENGTH;
        }

        return reminderTime;
    }

    private void showHideLiveReportData() {
        InterventionMap interventionMap = CalculationUtil.getInterventionMap(getApplicationContext());
        InterventionDisplayData display = interventionMap.get(CalculationUtil.updateAndGetCurrentCycle(getApplicationContext()));

        LinearLayout liveDataLayout = (LinearLayout) findViewById(R.id.live_report_layout);

        if (display.isShowLiveReport()) {
            liveDataLayout.setVisibility(View.VISIBLE);
        } else {
            liveDataLayout.setVisibility(View.GONE);
        }
    }

    //starts the location updates service which updates the database at given interval
    //starts BackgroundLocationService class
    private void startLocationUpdates(String userID) {
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        startService(intent);
    }

    //stops the location updates service
    private void stopLocationUpdates() {
        Intent intent = new Intent(MainActivity.this, BackgroundLocationService.class);
        stopService(intent);

    }

    //function to store the current screen to the shared preference screen variable
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }


    //function to create alarms at given time
    public void createAlarms(long currentTime) {
        long thirty = currentTime + BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL;
        startAlarm(thirty, NotificationService.FIRST_IN_EPISODE_REMINDER_NOTIFICATION_ID);

        long sixty = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 2);
        startAlarm(sixty, NotificationService.SECOND_IN_EPISODE_REMINDER_NOTIFICATION_ID);

        long ninety = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 3);
        startAlarm(ninety, NotificationService.THIRD_IN_EPISODE_REMINDER_NOTIFICATION_ID);

        long oneTwenty = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 4);
        startAlarm(oneTwenty, NotificationService.FOURTH_IN_EPISODE_REMINDER_NOTIFICATION_ID);
    }

    //function to start alarm manager at given time
    public void startAlarm(long time, int broadcastID) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        resultIntent = new Intent(MainActivity.this, TimerReceiver.class);
        //resultIntent.putExtra("User ID", userIDMA);
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
        super.onResume();
//        long startedTimeInMillis = getStartedTime();
        //start the ui update service when the app is resumed to update ui timer
        //start the ui update service when the app is resumed to update ui timer
        //startUIUpdateService(startedTimeInMillis);
        //checks to see which screen is set in the shared preferences screen variable
        String selectedScreen = getCurrentScreen();
        //if the screen has been set to start or morning qs, the activity changes
        //receivers and services are stopped and activity is switched back to the start screen
        if (selectedScreen.equals("start") | selectedScreen.equals("morningQS")) {
            //stopUIUpdateService();
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                stopLocationUpdates();
            }
            //unregisterReceiver(broadcastReceiver);
            Intent goToStart = new Intent(MainActivity.this, StartActivity.class);
            goToStart.putExtra("Start Activity", startActivity);
            startActivity(goToStart);
            finish();
        }

    }

    private String getCurrentScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        return mSharedPreferences.getString("currentScreen", "none");
    }

    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }

    private void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey();
            if (usersKey.equals("Users")) {
                int currentCycle = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext());

                List<DrinkAnswer> allDrinkAnswers = DatabaseQueryService.getAllDrinksAnswers(ds, userIDMA, currentCycle);

                LiveReportData liveReportData = CalculationUtil.getLiveData(allDrinkAnswers);
                num_drink_text.setText(liveReportData.getNumDrinks() + "");
                cal_text.setText(liveReportData.getCaloriesConsumed() + "");
                cost_txt.setText(String.format("%.2f", liveReportData.getAverageCost()));
            }
        }
    }

    private Long getUserStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userStartTime", 0);
        return time;
    }

    private Long getUserRawStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userRawStartTime", 0);
        return time;
    }
}