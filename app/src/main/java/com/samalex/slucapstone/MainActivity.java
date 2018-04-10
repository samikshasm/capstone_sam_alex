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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//suna
import static com.samalex.slucapstone.BroadcastService.STARTED_TIME_IN_MILLIS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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
    public static final long PROGRESS_BAR_MAX = 60;
    private Integer nightCount;
    private String[] typeList;
    private String[] costList;
    private String date;
    private DatabaseReference mReference;
    private String[] sizeList;
    private Integer totalCalConsumed;
    private Double avgCost = 0.00;
    private String group;
    private TextView cost_txt;
    private TextView cal_text;
    private TextView num_drink_text;


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
        //progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        //textViewTime = (TextView) findViewById(R.id.textViewTime);
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

        cal_text = (TextView) findViewById(R.id.cal_text);
        num_drink_text = (TextView) findViewById(R.id.num_drinks_txt);
        cost_txt = (TextView) findViewById(R.id.cost_text);

        SharedPreferences mSharedPreferences2 = getSharedPreferences("Group", MODE_PRIVATE);
        group = mSharedPreferences2.getString("Group","none");
        if(group.equals("experimental")){
            LinearLayout lin1 = (LinearLayout) findViewById(R.id.lin_lay_1);
            lin1.setVisibility(View.VISIBLE);
            LinearLayout lin2 = (LinearLayout) findViewById(R.id.lin_layout_2);
            lin2.setVisibility(View.VISIBLE);
        }
        Log.e("Group", userIDMA+","+group);

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
                stopLocationUpdates();
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
        long milliseconds = morningCal.getTimeInMillis();
        morningCal.setTimeInMillis(milliseconds);
        int hour = morningCal.get(Calendar.HOUR_OF_DAY);
        int day;
        if (hour >= 0 && hour < 7){
            day = morningCal.get(Calendar.DAY_OF_WEEK);
        }else{
            day = morningCal.get(Calendar.DAY_OF_WEEK);
            if(day == 7){
                day = 0;
            }else{
                day = day+1;
            }
        }
        morningCal.set(Calendar.DAY_OF_WEEK, day);
        morningCal.set(Calendar.HOUR_OF_DAY, 7);
        morningCal.set(Calendar.MINUTE, 0);
        morningCal.set(Calendar.SECOND, 0);
        Log.e("morningCal set time:",morningCal.get(Calendar.DAY_OF_WEEK)+","+morningCal.get(Calendar.HOUR_OF_DAY));
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

/*
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
*/

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
        //startUIUpdateService(startedTimeInMillis);
        //checks to see which screen is set in the shared preferences screen variable
        SharedPreferences mSharedPreferences1 = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences1.getString("currentScreen", "none");
        //if the screen has been set to start or morning qs, the activity changes
        //receivers and services are stopped and activity is switched back to the start screen
        if (selectedScreen.equals("start") | selectedScreen.equals("morningQS")) {
            //stopUIUpdateService();
            stopLocationUpdates();
            //unregisterReceiver(broadcastReceiver);
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

    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }



    private void getData(DataSnapshot dataSnapshot) {

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if(usersKey.equals("Users")){
                //gets information from database
                //makes sure that there is data at the given branch
                Object typeObject = ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Type").getValue();
                // Log.e("TypeObject",""+ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Type").getValue());
                if (typeObject != null){
                    //gets the specific string needed to analyze
                    String typeDrink = typeObject.toString();

                    String typeDrinkSub = typeDrink.substring(1, typeDrink.length()-1);
                    String[] testType = typeDrinkSub.split(",");
                    typeList = new String[testType.length];
                    for (int i =0; i<testType.length; i++) {
                        String[] tempList = testType[i].split("=");
                        typeList[i] = tempList[1];
                    }
//                Log.e("testing", ""+typeList);

                    Integer numWine = 0;
                    Integer numLiquor = 0;
                    Integer numBeer = 0;

                    for (int i = 0; i < testType.length; i++ ) {

                        String type = typeList[i];

                        if (type.equals("wine")) {
                            numWine++;
                        }

                        else if (type.equals("liquor")) {
                            numLiquor++;
                        }

                        else if (type.equals("beer")) {
                            numBeer++;
                        }
                    }

                    //gets number of drinks and gets percent of each type
                    int numberDrinks = numBeer+numWine+numLiquor;
                    num_drink_text.setText(numberDrinks+"");



                }else{
                    //if no data was entered from user, sets default value
                    //populates an empty pie chart if size of drink is null
                    TextView num_drink_text = (TextView) findViewById(R.id.num_drinks_txt);
                    num_drink_text.setText("0");
                }

                //checks to make sure data was actually entered
                Object sizeObject = ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Size").getValue();
                if (sizeObject != null){
                    //splits the string properly to get the necessary data
                    String sizeDrink = sizeObject.toString();
                    //Log.e("sizeDrink",sizeDrink);
                    String sizeDrinkSub = sizeDrink.substring(1, sizeDrink.length()-1);
                    //Log.e("sizeDrnkSub", sizeDrinkSub);
                    String[] test = sizeDrinkSub.split(",");
                    //Log.e("test", ""+test);
                    sizeList = new String[test.length];
                    for (int i =0; i<test.length; i++) {
                        String[] tempList = test[i].split("=");
                        sizeList[i] = tempList[1];
                    }

                    //initializes calorie values
                    Integer calorieWineShot = 100; //i made this up
                    Integer calorieWineEight = 188;
                    Integer calorieWineSixteen = 377;
                    Integer calorieWineTwentyFour = 565;

                    Integer calorieBeerShot = 58; //i don't actually know
                    Integer calorieBeerEight = 98;
                    Integer calorieBeerSixteen = 196;
                    Integer calorieBeerTwentyFour = 294;


                    //ALEX HELP ME PLS!!!!!
                    Integer calorieLiquorShot = 100; //i made this up
                    Integer calorieLiquorEight = 188;
                    Integer calorieLiquorSixteen = 377;
                    Integer calorieLiquorTwentyFour = 565;

                    //initializes default values
                    totalCalConsumed = 0;
                    Integer totalOuncesConsumed = 0;

                    //samiksha can you comment this????????
                    for (int i = 0; i < test.length; i++ ) {
                        //totalCalConsumed+= i;

                        String type = typeList[i];
                        String size = sizeList[i];

                        if (!sizeList[i].equals("Shot")) {
                            Integer sizeOfDrink = Integer.parseInt(sizeList[i]);
                            totalOuncesConsumed = totalOuncesConsumed + sizeOfDrink;
                        }

                        else {
                            totalOuncesConsumed = totalOuncesConsumed + 1;
                        }

                        if (type.equals("wine")) {

                            if (size.equals("Shot")) {
                                totalCalConsumed = totalCalConsumed+calorieWineShot;
                            }

                            else if (size.equals("8")) {
                                totalCalConsumed = totalCalConsumed+calorieWineEight;

                            }
                            else if (size.equals("16")) {
                                totalCalConsumed = totalCalConsumed+calorieWineSixteen;

                            }
                            else if (size.equals("24")) {
                                totalCalConsumed = totalCalConsumed+calorieWineTwentyFour;
                            }
                        }

                        else if (type.equals("liquor")) {

                            if (size.equals("Shot")) {
                                totalCalConsumed = totalCalConsumed+calorieLiquorShot;
                            }
                            else if (size.equals("8")) {
                                totalCalConsumed = totalCalConsumed+calorieLiquorEight;

                            }
                            else if (size.equals("16")) {
                                totalCalConsumed = totalCalConsumed+calorieLiquorSixteen;

                            }
                            else if (size.equals("24")) {
                                totalCalConsumed = totalCalConsumed+calorieLiquorTwentyFour;
                            }
                        }

                        else if (type.equals("beer")) {

                            if (size.equals("Shot")) {
                                totalCalConsumed = totalCalConsumed+calorieBeerShot;
                            }
                            else if (size.equals("8")) {
                                totalCalConsumed = totalCalConsumed+calorieBeerEight;

                            }
                            else if (size.equals("16")) {
                                totalCalConsumed = totalCalConsumed+calorieBeerSixteen;

                            }
                            else if (size.equals("24")) {
                                totalCalConsumed = totalCalConsumed+calorieBeerTwentyFour;
                            }
                        }
                    }


                    //sets the display calories textview
                    cal_text.setText(""+totalCalConsumed);

                }else{
                    //set values to null if size drink is null
                    cal_text.setText("0");
                }

                Object costObject = ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Cost").getValue();
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
                    for(int i = 0; i < costList.length; i++){
                        if(costList[i].contains("-")){
                            String[] tempList = costList[i].split("-");
                            //Log.e("length",tempList.length+"");
                            for(int j = 0; j < tempList.length; j++) {
                                tempList[j] = tempList[j].substring(1, tempList[j].length());
                            }
                            Double minCost = 0.00;
                            Double maxCost = 0.00;
                            minCost = minCost+(Double.parseDouble(tempList[0]));
                            maxCost = maxCost+(Double.parseDouble(tempList[1]));
                            avgCost = avgCost + ((maxCost+minCost)/2);
                        }else{
                            if(costList[i].contains("+")){
                                //16+
                                String tempString = costList[i].substring(1,costList[i].length()-1);
                                Double cost = Double.parseDouble(tempString);
                                avgCost = avgCost + cost;
                            }else{
                                //0
                                String tempString = costList[i].substring(1,costList[i].length());
                                Double cost = Double.parseDouble(tempString);
                                avgCost = avgCost + cost;
                            }
                        }


                    }
                    String totalCost = "$"+avgCost+"0";
                    cost_txt.setText(totalCost+"");
                }else{
                    //Log.e("null","cost object is null");
                }

            }



        }
    }
}