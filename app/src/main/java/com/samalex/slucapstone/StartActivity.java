package com.samalex.slucapstone;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by samikshasm on 7/17/17.
 */

public class StartActivity extends AppCompatActivity {

    public static final int ONE_MINUTE = 60 * 1000;
    public static final int ONE_HOUR = 60 * 60 * 1000;
    public static final int ONE_DAY = 24 * 60 * 60 * 1000; // 86,400,000 milliseconds in 1 day
    public static final int TWO_DAY = ONE_DAY * 2;

    private String userIDMA;
    private String currentUserFromLA;
    private String startActivity;
    private Integer id = 1;
    private String lastActivity;
    private Integer nightCount = 0;
    private String cancelButMain;
    private DatabaseReference mReference;
    private ArrayList<String> controlList;
    private ArrayList<String> expList;
    private String group;
    private Integer loginAttempts;
    private Integer startAttempts;
    private Number oneWeek;
    private Number twoWeeks;


    //new location stuff
    static final Integer LOCATION = 0x1;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    static final Integer GPS_SETTINGS = 0x7;

    @Override
    protected void onRestart() {
        super.onRestart();
        processInterventionGroup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //new location stuff
        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();

        // check if the user should be swapped ti another intervention group
        processInterventionGroup();


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
        } else if (cancelButMain == "main") {
            Log.e("Cancel main activity", cancelButMain);

        }

        ImageView appHeaderBar = findViewById(R.id.app_header_bar);
        appHeaderBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(StartActivity.this, R.style.MyDialogTheme);
                builder.setTitle("Logs for testing")
                        .setMessage("username: " +userIDMA
                        + "\ngroup: " + getGroup())
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
                nightCount = getNightCount();
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

        //sign out button
        ImageButton signOut = (ImageButton) findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeUserID("none");
                currentUserFromLA = "signed out";
                storeStartAttempts(0);
                Intent switchToLogin = new Intent(StartActivity.this, LoginActivity.class);
                switchToLogin.putExtra("sign out", currentUserFromLA);
                startActivity(switchToLogin);
                storeGroup("none");
                finish();
            }
        });

        ImageButton contact = (ImageButton) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(StartActivity.this, R.style.MyAlertDialogStyle)
                        //.setTitle("Contact Dr. Shacham")
                        .setMessage("Email eshacham@slu.edu\nYour logged in username: " + getUserID() + "\nOngoing status: " + getGroup())
                        // .setNegativeButton("Ok",null)
                        .setPositiveButton("Ok", null).create().show();
            }
        });

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


        //Log.e("user ID",userIDMA);

    }

    private String getCurrentScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        return mSharedPreferences.getString("currentScreen", "none");
    }

    private void processInterventionGroup() {
        BoozymeterApplication application = (BoozymeterApplication) getApplication();
        boolean isDebug = application.isDebug();

        loginAttempts = getLoginAttempts();
        startAttempts = getStartAttempts();
        startAttempts++;
        storeStartAttempts(startAttempts);
        Calendar cal = Calendar.getInstance();
        long currentTime = cal.getTimeInMillis();

        if (loginAttempts == 1 && startAttempts == 1) {
            Calendar morningCal = Calendar.getInstance();
            long currentMillis = morningCal.getTimeInMillis();

            if (isDebug) {
                oneWeek = ONE_MINUTE + currentMillis;
                twoWeeks = (2 * ONE_MINUTE) + currentMillis;
            } else {
                oneWeek = ONE_DAY + currentMillis;
                twoWeeks = TWO_DAY + currentMillis;
            }

            storeOneWeek(oneWeek.longValue());
            storeTwoWeeks(twoWeeks.longValue());
            storeGroup("none");
        }

       /* Log.e("start attempts", startAttempts+"");
        Log.e("login attempts", loginAttempts+"");
        Log.e("group before", group+"");
        Log.e("oneweek", oneWeek+"");
        Log.e("twoweeks", twoWeeks+"");*/

        long oneWeek = getOneWeek();
        long twoWeeks = getTwoWeeks();

        Log.e("oneWeek", oneWeek + "");
        Log.e("twoWeeks", twoWeeks + "");
        Log.e("calmillis", currentTime + "");

        boolean isDuringWeek1 = currentTime < oneWeek;
        boolean isDuringWeek2 = currentTime >= oneWeek && currentTime < twoWeeks;
        boolean isDuringWeek3OrAfterward = currentTime >= getTwoWeeks();

//        if(isDuringWeek1) {
//            // do nothing
//        }
        if (isDuringWeek2) {
            // assign group straightforward (control -> control, experimental -> experimental)
            mReference = FirebaseDatabase.getInstance().getReference();
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    getControlList(dataSnapshot);
                    getExperimentalList(dataSnapshot);
                    Log.e("expList", expList + "");
                    Log.e("controlList", controlList + "");

                    for (int i = 0; i < expList.size(); i++) {
                        if (expList.get(i).equals(userIDMA)) {
                            group = "experimental";
                            break;
                        }
                    }
                    for (int i = 0; i < controlList.size(); i++) {
                        if (controlList.get(i).equals(userIDMA)) {
                            group = "control";
                            break;
                        }
                    }
                    Log.e("group", group + "");
                    storeGroup(group);
                    //Log.e("expList",expList.get(4).substring(1,expList.get(4).length()).length()+"");
                    //Log.e("userID",userIDMA.length()+"");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.e("group after ", getGroup() + "");
        }
        if (isDuringWeek3OrAfterward) {
            // switch group (control -> experimental, experimental -> control)
            mReference = FirebaseDatabase.getInstance().getReference();
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    getControlList(dataSnapshot);
                    getExperimentalList(dataSnapshot);
                    for (int i = 0; i < expList.size(); i++) {
                        if (expList.get(i).equals(userIDMA)) {
                            group = "control";
                            break;
                        }
                    }
                    for (int i = 0; i < controlList.size(); i++) {
                        if (controlList.get(i).equals(userIDMA)) {
                            group = "experimental";
                            break;
                        }
                    }
                    storeGroup(group);
                    //Log.e("expList",expList.get(4).substring(1,expList.get(4).length()).length()+"");
                    //Log.e("userID",userIDMA.length()+"");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.e("group: ", getGroup() + "");

        }
    }

    //function to analyze data received from snapshot
    private void getControlList(DataSnapshot dataSnapshot) {

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                //gets information from database
                //makes sure that there is data at the given branch
                Map<String, Map<String, String>> controlJSON = (Map<String, Map<String, String>>) ds.child("Control Group").getValue();
                if (controlJSON == null) {
                    controlList = new ArrayList<>();
                } else {
                    controlList = new ArrayList<>(controlJSON.keySet());
                }
            }
        }
    }

    private void getExperimentalList(DataSnapshot dataSnapshot) {

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                //gets information from database
                //makes sure that there is data at the given branch
                Map<String, Map<String, String>> experimentalJSON = (Map<String, Map<String, String>>) ds.child("Experimental Group").getValue();
                if (experimentalJSON == null) {
                    expList = new ArrayList<>();
                } else {
                    expList = new ArrayList<>(experimentalJSON.keySet());
                }
            }

        }
    }

    private void storeGroup(String group) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("Group", group);
        mEditor.apply();
    }

    private String getGroup() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        String group = mSharedPreferences.getString("Group", "none");
        return group;
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
}


