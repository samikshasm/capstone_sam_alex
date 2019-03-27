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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    private String[] costList;
    private String date;
    private String[] sizeList;
    private Integer totalCalConsumed;
    private Double avgCost = 0.00;

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
        getTime();

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
                createMorningAlarm();
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
//                cancelAlarm(4);
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
//                cancelAlarm(4);
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
            long userStartTime = getUserStartTime();
            String userStartDateStr = dateFormat.format(new Date(userStartTime));

            String moringSurveyTime = dateFormat.format(new Date(userStartTime + BoozymeterApplication.SURVEY_OFFSET));
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

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity .this, R.style.MyDialogTheme);
                builder.setTitle("Hidden Logs")
                        .setMessage("Username: " + userIDMA
                                + "\nUser group: " + getGroup()
                                + "\nCanonical start time: " + userStartDateStr
                                + "\nCycle: " + currentCycle + " (zero-based index)"
                                + "\nCycle length: " + BoozymeterApplication.CYCLE_LENGTH / 1000 / 60 + " minutes"
                                + "\nNumber of cycles: " + BoozymeterApplication.NUM_CYCLES
                                + "\nCycle offset: " + BoozymeterApplication.CYCLE_OFFSET / 1000 / 60 + " minutes"
                                + "\nLive report: " + liveReportFlag
                                + "\nMorning report: " + morningReportFlag
                                + "\nNumber of drinks: " + getNumDrinks()
                                + "\nNight count (old parameter): " + getNightCount()
                                + "\n\n"
                                + "\nMorning Survey alarm will go off: " + moringSurveyTime
                                + "\nFirst evening reminder will go off: " + eveningReminderTime
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
    private Integer getNumDrinks () {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks",0);
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
        if(reminderTime < now) {
            reminderTime += BoozymeterApplication.CYCLE_LENGTH;
        }

        return reminderTime;
    }

    private void showHideLiveReportData() {
        InterventionMap interventionMap = getInterventionMap();
        InterventionDisplayData display = interventionMap.get(getCurrentCycle());

        LinearLayout liveDataLayout = (LinearLayout) findViewById(R.id.live_report_layout);

        if(display.isShowLiveReport()) {
            liveDataLayout.setVisibility(View.VISIBLE);
        } else {
            liveDataLayout.setVisibility(View.GONE);
        }
    }

    //function to get the current time
    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(currentDate);
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

    //creates the morning alarm manager to go off the following morning
    //adapted code from Alarm Manager Android Developer page
    private void createMorningAlarm() {
        long userStartTime = getUserStartTime();
        long morningSurveyTimeInMillis = userStartTime + BoozymeterApplication.SURVEY_OFFSET;
        Calendar morningSurveyCalendar = Calendar.getInstance();
        morningSurveyCalendar.setTimeInMillis(morningSurveyTimeInMillis);

        Log.e("morning survey alarm:", morningSurveyCalendar.getTimeInMillis() + "");

        Intent alertIntent = new Intent(this, TimerReceiver.class);
        AlarmManager morningAlarmMan = (AlarmManager) getSystemService(ALARM_SERVICE);
        alertIntent.putExtra("broadcast Int", NotificationService.MORNING_QUESTIONNAIRE_NOTIFICATION_ID + "");
        alertIntent.putExtra("initial time", initialTimeStr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 5, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        morningAlarmMan.set(AlarmManager.RTC_WAKEUP, morningSurveyCalendar.getTimeInMillis(), pendingIntent);
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
        //1800000
        startAlarm(thirty, 1);

        long sixty = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 2);
        //3600000
        startAlarm(sixty, 2);

        long ninety = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 3);
        //5400000
        startAlarm(ninety, 3);

//        long one = currentTime + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL * 4);
//        //7200000
//        startAlarm(one, 4);
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

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                //gets information from database
                //makes sure that there is data at the given branch
                Object typeObject = ds.child("UID: " + userIDMA).child("Night Count: " + nightCount).child("Answers").child("Date: " + date).child("Type").getValue();
                // Log.e("TypeObject",""+ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Type").getValue());
                if (typeObject != null) {
                    //gets the specific string needed to analyze
                    String typeDrink = typeObject.toString();

                    String typeDrinkSub = typeDrink.substring(1, typeDrink.length() - 1);
                    String[] testType = typeDrinkSub.split(",");
                    typeList = new String[testType.length];
                    for (int i = 0; i < testType.length; i++) {
                        String[] tempList = testType[i].split("=");
                        typeList[i] = tempList[1];
                    }
//                Log.e("testing", ""+typeList);

                    Integer numWine = 0;
                    Integer numLiquor = 0;
                    Integer numBeer = 0;

                    for (int i = 0; i < testType.length; i++) {

                        String type = typeList[i];

                        if (type.equals("wine")) {
                            numWine++;
                        } else if (type.equals("liquor")) {
                            numLiquor++;
                        } else if (type.equals("beer")) {
                            numBeer++;
                        }
                    }

                    //gets number of drinks and gets percent of each type
                    int numberDrinks = numBeer + numWine + numLiquor;
                    num_drink_text.setText(numberDrinks + "");


                } else {
                    //if no data was entered from user, sets default value
                    //populates an empty pie chart if size of drink is null
                    TextView num_drink_text = (TextView) findViewById(R.id.num_drinks_txt);
                    num_drink_text.setText("0");
                }

                //checks to make sure data was actually entered
                Object sizeObject = ds.child("UID: " + userIDMA).child("Night Count: " + nightCount).child("Answers").child("Date: " + date).child("Size").getValue();
                if (sizeObject != null) {
                    //splits the string properly to get the necessary data
                    String sizeDrink = sizeObject.toString();
                    //Log.e("sizeDrink",sizeDrink);
                    String sizeDrinkSub = sizeDrink.substring(1, sizeDrink.length() - 1);
                    //Log.e("sizeDrnkSub", sizeDrinkSub);
                    String[] test = sizeDrinkSub.split(",");
                    //Log.e("test", ""+test);
                    sizeList = new String[test.length];
                    for (int i = 0; i < test.length; i++) {
                        String[] tempList = test[i].split("=");
                        sizeList[i] = tempList[1];
                    }

                    //initializes default values
                    totalCalConsumed = 0;
                    Integer totalOuncesConsumed = 0;

                    //samiksha can you comment this????????
                    for (int i = 0; i < test.length; i++) {
                        //totalCalConsumed+= i;

                        String type = typeList[i];
                        String size = sizeList[i];

                        if (!sizeList[i].equals("Shot")) {
                            Integer sizeOfDrink = Integer.parseInt(sizeList[i]);
                            totalOuncesConsumed = totalOuncesConsumed + sizeOfDrink;
                        } else {
                            totalOuncesConsumed = totalOuncesConsumed + 1;
                        }

                        int oneServingSize = 1;
                        int caloriePerOneServing = 100;

                        if (type.equals("wine")) {
                            oneServingSize = 4;
                        } else if (type.equals("beer")) {
                            oneServingSize = 12;
                        } else if (type.equals("liquor")) {
                            oneServingSize = 1;
                        }

                        totalCalConsumed += Integer.parseInt(size) * caloriePerOneServing / oneServingSize;
                    }


                    //sets the display calories textview
                    cal_text.setText("" + totalCalConsumed);

                } else {
                    //set values to null if size drink is null
                    cal_text.setText("0");
                }

                Object costObject = ds.child("UID: " + userIDMA).child("Night Count: " + nightCount).child("Answers").child("Date: " + date).child("Cost").getValue();
                if (costObject != null) {
                    //splits the string properly to get the necessary data
                    String costDrink = costObject.toString();
                    //Log.e("costDrink", costDrink);
                    String costDrinkSub = costDrink.substring(1, costDrink.length() - 1);
                    //Log.e("costDrinkSub", costDrinkSub);
                    String[] test = costDrinkSub.split(",");
                    //Log.e("test", "" + test);
                    costList = new String[test.length];
                    for (int i = 0; i < test.length; i++) {
                        String[] tempList = test[i].split("=");
                        costList[i] = tempList[1];
                        //Log.e("costList",costList[i]);
                    }
                    for (int i = 0; i < costList.length; i++) {
                        if (costList[i].contains("-")) {
                            String[] tempList = costList[i].split("-");
                            //Log.e("length",tempList.length+"");
                            for (int j = 0; j < tempList.length; j++) {
                                tempList[j] = tempList[j].substring(1, tempList[j].length());
                            }
                            Double minCost = 0.00;
                            Double maxCost = 0.00;
                            minCost = minCost + (Double.parseDouble(tempList[0]));
                            maxCost = maxCost + (Double.parseDouble(tempList[1]));
                            avgCost = avgCost + ((maxCost + minCost) / 2);
                        } else {
                            if (costList[i].contains("+")) {
                                //16+
                                String tempString = costList[i].substring(1, costList[i].length() - 1);
                                Double cost = Double.parseDouble(tempString);
                                avgCost = avgCost + cost;
                            } else {
                                //0
                                String tempString = costList[i].substring(1, costList[i].length());
                                Double cost = Double.parseDouble(tempString);
                                avgCost = avgCost + cost;
                            }
                        }


                    }
                    cost_txt.setText(String.format("%.2f", avgCost));
                } else {
                    //Log.e("null","cost object is null");
                }

            }


        }
    }

    private int getCurrentCycle() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        int cycle = mSharedPreferences.getInt("currentCycle", 0);
        return cycle;
    }

    private InterventionMap getInterventionMap() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("intervention_map", "");
        InterventionMap map = gson.fromJson(json, InterventionMap.class);
        return map;
    }

    private Long getUserStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userStartTime", 0);
        return time;
    }
}