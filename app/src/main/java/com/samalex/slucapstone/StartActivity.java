package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by samikshasm on 7/17/17.
 */

public class StartActivity extends AppCompatActivity {

    public static final int ONE_MINUTE = 60 * 1000;
    public static final int ONE_HOUR = 60 * 60 * 1000;
    public static final int ONE_DAY = 24 * 60 * 60 * 1000; // 86,400,000 milliseconds in 1 day
    public static final int TWO_DAY = ONE_DAY * 2;

    private final InterventionDisplayData NO_INTERVENTION = new InterventionDisplayData(false, false);
    private final InterventionDisplayData INTERVENTION_A = new InterventionDisplayData(false, true);
    private final InterventionDisplayData INTERVENTION_B = new InterventionDisplayData(true, false);

    private final List<InterventionDisplayData> CONTROL_GROUP_INTERVENTIONS = Arrays.asList(NO_INTERVENTION, INTERVENTION_A, INTERVENTION_B);
    private final List<InterventionDisplayData> EXPERIMENTAL_GROUP_INTERVENTIONS = Arrays.asList(NO_INTERVENTION, INTERVENTION_B, INTERVENTION_A);
    private final List<InterventionDisplayData> NONE_GROUP_INTERVENTIONS = Arrays.asList(NO_INTERVENTION, NO_INTERVENTION, NO_INTERVENTION);

    private String userIDMA;
    private String currentUserFromLA;
    private String startActivity;
    private Integer id = 1;
    private String lastActivity;
    private String cancelButMain;
    private DatabaseReference mReference;


    //new location stuff
    static final Integer LOCATION = 0x1;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    static final Integer GPS_SETTINGS = 0x7;

    @Override
    protected void onRestart() {
        super.onRestart();
        incrementStartAttempts(); // when user go back to the start activity after exiting the app by hitting the home button (which does not trigger onCreate()


        Calendar calendar = Calendar.getInstance();
        updateCurrentCycle(calendar.getTimeInMillis());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        incrementStartAttempts();

        setContentView(R.layout.activity_start);

        initializeLocationServiceClient();

        if (isFirstLogin()) {
            // reset all count values
            storeCurrentCycle(0);
            storeNight(0); // count episodes in 1 cycle

            // Pre-compute a new table for this user
            long canonicalUserStartTime = calculateUserStartTime(
                    Calendar.getInstance(),
                    BoozymeterApplication.CYCLE_LENGTH,
                    BoozymeterApplication.CYCLE_OFFSET);
            Calendar cal = Calendar.getInstance();
            storeUserRawStartTime(cal.getTimeInMillis());
            storeUserStartTime(canonicalUserStartTime);
            precomputeInterventionLookupTable(canonicalUserStartTime);

            // Create an evening reminder alarm that goes of daily
            createEveningDailyReminderAlarm();
        } else {
            // check if map is null
        }

        Calendar calendar = Calendar.getInstance();
        updateCurrentCycle(calendar.getTimeInMillis());

        userIDMA = getIntent().getStringExtra("User ID");
        SharedPreferences userSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = userSharedPreferences.getString("user ID", "none");

        if (userIDMA != null) {
            Log.e("User ID Start", userIDMA);
        } else if (userIDMA == null | userIDMA.equals("none")) {
            storeScreen("login");
        }

        cancelButMain = getIntent().getStringExtra("Cancel main activity");
        if (cancelButMain == null) {
            Log.e("null String", "null");
        } else if (cancelButMain.equals("main")) {
            Log.e("Cancel main activity", cancelButMain);

        }

        registerUIEvents();

        SharedPreferences switchesSharedPreferences = getSharedPreferences("NumberSwitchesToMain2Activity", MODE_PRIVATE);
        SharedPreferences.Editor switchesEditor = switchesSharedPreferences.edit();
        switchesEditor.putInt("switches", 1);
        switchesEditor.apply();

        String selectedScreen = getCurrentScreen();
        if (selectedScreen.equals("main")) {
            Intent switchToMain = new Intent(StartActivity.this, MainActivity.class);
            startActivity(switchToMain);
            finish();
        } else if (selectedScreen.equals("morningReport")) {
            Log.e("is it experimental?", "hi it is");
            Intent switchToMorning = new Intent(StartActivity.this, MorningReport.class);
            startActivity(switchToMorning);
            finish();
        } else if (selectedScreen.equals("morningQS")) {
            Log.e("is it control?", "hi it is");
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            startActivity(switchToMorning);
            finish();
        } else if (selectedScreen.equals("login")) {
            Intent switchToLogin = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(switchToLogin);
            finish();
        }
    }


    private void createEveningDailyReminderAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calculateEveningReminderTime());

        // Set an alarm to go off daily
        Intent alertIntent = new Intent(this, TimerReceiver.class);
        alertIntent.putExtra("broadcast Int", NotificationService.EVENING_REMINDER_NOTIFICATION_ID + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NotificationService.EVENING_REMINDER_NOTIFICATION_ID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.e("evening reminder alarm:", calendar.getTimeInMillis() + "");

        storePendingEveningReminderTimeInMillis(calendar.getTimeInMillis());
    }

    private void storePendingEveningReminderTimeInMillis(long timeInMillis) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("pendingEveningReminderTimeInMillis", timeInMillis);
        mEditor.apply();
    }

    private void updateCurrentCycle(long currentTimeInMillis) {
        List<Long> cycleStartTimeList = getCycleStartTimeList();

        int cycle = 0;
        for (int i = 0, len = cycleStartTimeList.size(); i < len; i++) {
            if (currentTimeInMillis >= cycleStartTimeList.get(i)) {
                cycle = i;
            } else {
                break;
            }
        }
        storeCurrentCycle(cycle);
    }

    private void initializeLocationServiceClient() {
        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();
    }

    private void registerUIEvents() {
        ImageView appHeaderBar = findViewById(R.id.app_header_bar);
        appHeaderBar.setOnClickListener(new View.OnClickListener() {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long userRawStartTime = getUserRawStartTime();
            String userRawStartDateStr = dateFormat.format(new Date(userRawStartTime));
            long userStartTime = getUserStartTime();
            String userStartDateStr = dateFormat.format(new Date(userStartTime));

            long morningSurveyTimeInMillis = BoozymeterApplication.getNextMorningSurveyTimeInMillis(getUserStartTime(), getCurrentCycle());
            String moringSurveyTime = dateFormat.format(new Date(morningSurveyTimeInMillis));
            String eveningReminderTime = dateFormat.format(new Date(calculateEveningReminderTime()));

            @Override
            public void onClick(View view) {
                int currentCycle = getCurrentCycle();
                InterventionDisplayData ui = getInterventionMap().get(currentCycle);
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

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(StartActivity.this, R.style.MyDialogTheme);
                builder.setTitle("Hidden Logs")
                        .setMessage("Number of cycles: " + BoozymeterApplication.NUM_CYCLES
                                + "\nCycle length: " + BoozymeterApplication.CYCLE_LENGTH / 1000 / 60 + " minutes"
                                + "\nCycle offset: " + BoozymeterApplication.CYCLE_OFFSET / 1000 / 60 + " minutes"
                                + "\nMorning survey offset: " + BoozymeterApplication.SURVEY_OFFSET / 1000 / 60 + " minutes"
                                + "\nEvening reminder offset: " + BoozymeterApplication.EVENING_REMINDER_OFFSET / 1000 / 60 + " minutes"
                                + "\n"
                                + "\nUsername: " + userIDMA
                                + "\nUser group: " + getGroup()
                                + "\n"
                                + "\nRaw start time: " + userRawStartDateStr
                                + "\nCanonical start time (incl. cycle offset): " + userStartDateStr
                                + "\nCycle (1-based index): " + (currentCycle + 1)
                                + "\n"
                                + "\nLive report: " + liveReportFlag
                                + "\nMorning report: " + morningReportFlag
                                + "\n"
                                + "\nNumber of drinks: " + getNumDrinks()
                                + "\nNight count (old parameter): " + getNightCount()
                                + "\n"
                                + "\nNext morning Survey alarm will go off: " + moringSurveyTime
                                + "\nNext evening reminder will go off: " + eveningReminderTime
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

        ImageButton startDrinking = (ImageButton) findViewById(R.id.start_drinking);
        startDrinking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                precomputeInterventionLookupTable(getUserStartTime()); // TODO: This is a temporary way to fix getting group from database is slower than when precomputeInterventionLookupTable method is called in onCreate()

                int nightCount = getNightCount();
                nightCount++;
                storeNight(nightCount);
                id = 2;
                startActivity = "start";
                Intent switchToMainActivity = new Intent(StartActivity.this, MainActivity.class);
                switchToMainActivity.putExtra("coming from start", "start");
                switchToMainActivity.putExtra("Start Activity", startActivity);
                startActivity(switchToMainActivity);
                finish();
            }
        });

        TextView infoIcon = (TextView) findViewById(R.id.info_icon);
        infoIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(StartActivity.this, R.style.MyAlertDialogStyle)
                        //.setTitle("Contact Dr. Shacham")
                        .setMessage("Contact: eshacham@slu.edu\nYour logged in username: " + getUserID())
                        // .setNegativeButton("Ok",null)
                        .setPositiveButton("Ok", null).create().show();
            }
        });

        infoIcon.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(StartActivity.this, R.style.MyAlertDialogStyle)
//                        .setTitle("Are you sure to sign out?")
                        .setMessage("Are you sure to sign out?")
                        // .setNegativeButton("Ok",null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                signOut();
                            }
                        })
                        .setNegativeButton("Cancel", null).create().show();
                return false;
            }
        });
    }

    private void signOut() {
        storeUserID("none");
        currentUserFromLA = "signed out";
        storeStartAttempts(0);
        Intent switchToLogin = new Intent(StartActivity.this, LoginActivity.class);
        switchToLogin.putExtra("sign out", currentUserFromLA);
        startActivity(switchToLogin);
        storeGroup("none");
        storeNumDrinks(0);
        storePendingEveningReminderTimeInMillis(0);
        storePendingMorningAlarmTimeInMillis(0);
        finish();
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

    private String getCurrentScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        return mSharedPreferences.getString("currentScreen", "none");
    }

    private void incrementStartAttempts() {
        int startAttempts = getStartAttempts();
        startAttempts++;
        storeStartAttempts(startAttempts);
    }

    public long calculateUserStartTime(Calendar currentDateTime, long cycleLength, long cycleOffset) {
        long now = currentDateTime.getTimeInMillis();

        Calendar naturalCycleStartTimeCalendar = (Calendar) currentDateTime.clone();
        naturalCycleStartTimeCalendar.set(
                currentDateTime.get(Calendar.YEAR),
                currentDateTime.get(Calendar.MONTH),
                currentDateTime.get(Calendar.DAY_OF_MONTH));
        naturalCycleStartTimeCalendar.set(Calendar.HOUR_OF_DAY, 0);
        naturalCycleStartTimeCalendar.set(Calendar.MINUTE, 0);
        naturalCycleStartTimeCalendar.set(Calendar.SECOND, 0);
        naturalCycleStartTimeCalendar.set(Calendar.MILLISECOND, 0);

        long naturalCycleStartTime = naturalCycleStartTimeCalendar.getTimeInMillis();

        long startTime = (((now - naturalCycleStartTime) / cycleLength) * cycleLength) + cycleOffset + naturalCycleStartTime;

        if (now - naturalCycleStartTime < cycleOffset) {
            startTime -= cycleLength; // support when the user login time is between midnight and CYCLE_OFFSET
        }
        return startTime;
    }

    private void storeUserRawStartTime(long rawStartTime){
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("userRawStartTime", rawStartTime);
        mEditor.apply();
    }

    private Long getUserRawStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userRawStartTime", 0);
        return time;
    }

    private Long getUserStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userStartTime", 0);
        return time;
    }

    public void storeUserStartTime(long time) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("userStartTime", time);
        mEditor.apply();
    }

    private int getCurrentCycle() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        int cycle = mSharedPreferences.getInt("currentCycle", 0);
        return cycle;
    }

    private void storeCurrentCycle(int cycle) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("currentCycle", cycle);
        mEditor.apply();
    }

    private InterventionMap getInterventionMap() {
        Gson gson = new Gson();

        // prevent NullPointerException
        InterventionMap emptyInterventionMap = new InterventionMap();
        String emptyInterventionMapStr = gson.toJson(emptyInterventionMap);

        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        String json = mSharedPreferences.getString("intervention_map", emptyInterventionMapStr);
        InterventionMap map = gson.fromJson(json, InterventionMap.class);
        return map;
    }

    private void storeInterventionMap(InterventionMap map) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(map); // myObject - instance of MyObject
        mEditor.putString("intervention_map", json);
        mEditor.apply();
    }

    private LongList getCycleStartTimeList() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("cycle_start_time_list", "[]");
        LongList list = gson.fromJson(json, LongList.class);
        return list;
    }

    private void storeCycleStartTimeList(LongList list) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list); // myObject - instance of MyObject
        mEditor.putString("cycle_start_time_list", json);
        mEditor.apply();
    }

    private void precomputeInterventionLookupTable(long userStartTime) {
        // clear old intervention map in shared preferences
        storeInterventionMap(new InterventionMap());

        List<InterventionDisplayData> interventionOrder = getInterventionOrder();
        int eachInterventionLength = BoozymeterApplication.NUM_CYCLES / 3;

        LongList cycleStartTimeList = new LongList();
        InterventionMap interventionLookupTable = new InterventionMap();

        for (int day = 0, len = BoozymeterApplication.NUM_CYCLES; day < len; day++) {

            // Store start time of each cycle
            cycleStartTimeList.add(day, userStartTime + (day * BoozymeterApplication.CYCLE_LENGTH));

            // Official phases: Day 1-7 = phase 0, Day 8-14 = phase 1 Day 15-21 = phase 2
            int phase = day / eachInterventionLength;

            // Store how UI looks like in each cycle
            interventionLookupTable.put(day, interventionOrder.get(phase));
        }

        // store the a new map in shared preferences
        storeInterventionMap(interventionLookupTable);
        storeCycleStartTimeList(cycleStartTimeList);
    }

    private List<InterventionDisplayData> getInterventionOrder() {
        switch (getGroup()) {
            case "control":
                return CONTROL_GROUP_INTERVENTIONS;
            case "experimental":
                return EXPERIMENTAL_GROUP_INTERVENTIONS;
        }
        return NONE_GROUP_INTERVENTIONS;
    }

    private boolean isFirstLogin() {
        return getLoginAttempts() == 1 && getStartAttempts() == 1;
    }

//    private List<String> getUserListByGroup(DataSnapshot dataSnapshot, String s) {
//        List<String> userList = new ArrayList<>();
//        //iterates through the dataSnapshot
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            String usersKey = ds.getKey().toString();
//            if (usersKey.equals("Users")) {
//                //gets information from database
//                //makes sure that there is data at the given branch
//                Map<String, Map<String, String>> controlJSON = (Map<String, Map<String, String>>) ds.child(s).getValue();
//                if (controlJSON != null) {
//                    userList = new ArrayList<>(controlJSON.keySet());
//                }
//            }
//        }
//        return userList;
//    }
//
//    // check user's group from database and store in SharedPreferences
//    private void storeUserGroupFromDBToSharedPref() {
//        mReference = FirebaseDatabase.getInstance().getReference();
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String group = "";
//                List<String> controlUserList = getUserListByGroup(dataSnapshot, "Control Group");
//                List<String> experimentalUserList = getUserListByGroup(dataSnapshot, "Experimental Group");
//
//                for (int i = 0; i < experimentalUserList.size(); i++) {
//                    if (experimentalUserList.get(i).equals(userIDMA)) {
//                        group = "experimental";
//                        break;
//                    }
//                }
//
//                for (int i = 0; i < controlUserList.size(); i++) {
//                    if (controlUserList.get(i).equals(userIDMA)) {
//                        group = "control";
//                        break;
//                    }
//                }
//                storeGroup(group);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        Log.e("group after ", getGroup() + "");
//    }

    private String getGroup() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        String group = mSharedPreferences.getString("Group", "none");
        return group;
    }

    private void storeGroup(String group) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("Group", group);
        mEditor.apply();
    }

    private void storeUserID(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("user ID", string);
        mEditor.apply();
    }

    private void storeNight(Integer nightCount) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("night counter", nightCount);
        mEditor.apply();
    }

    private String getUserID() {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("user ID", "none");
        return selectedScreen;
    }

    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }


    //function to store the current screen to the shared preference screen variable
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }

    private Integer getLoginAttempts() {
        SharedPreferences mSharedPreferences = getSharedPreferences("LoginAttempts", MODE_PRIVATE);
        Integer loginAttempts = mSharedPreferences.getInt("login attempts", 0);
        return loginAttempts;
    }

    private void storeOneWeek(long integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("OneWeek", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("one week", integer);
        mEditor.apply();
    }

    private long getOneWeek() {
        SharedPreferences mSharedPreferences = getSharedPreferences("OneWeek", MODE_PRIVATE);
        return mSharedPreferences.getLong("one week", 0);
    }

    private void storeTwoWeeks(long integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("TwoWeeks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("two weeks", integer);
        mEditor.apply();
    }

    private long getTwoWeeks() {
        SharedPreferences mSharedPreferences = getSharedPreferences("TwoWeeks", MODE_PRIVATE);
        return mSharedPreferences.getLong("two weeks", 0);
    }

    private void storeStartAttempts(Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("StartAttempts", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("start attempts", integer);
        mEditor.apply();
    }

    private Integer getStartAttempts() {
        SharedPreferences mSharedPreferences = getSharedPreferences("StartAttempts", MODE_PRIVATE);
        Integer startAttempts = mSharedPreferences.getInt("start attempts", 0);
        return startAttempts;
    }

    @Override
    public void onResume() {
        String selectedScreen = getCurrentScreen();
        if (selectedScreen.equals("morningQS")) {
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            startActivity(switchToMorning);
            finish();
        }
        super.onResume();
    }

    //new location stuff
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(StartActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(StartActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(StartActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        ask();

    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(StartActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    public void ask() {
        /*switch (v.getId()){
            case R.id.location:*/
        askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                /*break;
            default:
                break;
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
                //Call
                /*
                case 2:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;
                //Accounts
                case 6:
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){
                        Log.e("Account "+i,""+list[i].name);
                    }*/
            }

            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //gets number of drinks from shared preferences
    private Integer getNumDrinks() {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks", 0);
        return numberDrinks;
    }
    //function to store the number of drinks
    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }
    private void storePendingMorningAlarmTimeInMillis(long timeInMillis) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("pendingMorningAlarmTimeInMillis", timeInMillis);
        mEditor.apply();
    }
}


