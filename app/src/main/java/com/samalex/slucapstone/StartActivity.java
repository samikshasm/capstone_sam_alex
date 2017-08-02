package com.samalex.slucapstone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by samikshasm on 7/17/17.
 */

public class StartActivity extends AppCompatActivity {

    private String userIDMA;
    private String currentUserFromLA;
    private String startActivity;
    private Integer id = 1;
    private String lastActivity;
    private Integer nightCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        userIDMA = getIntent().getStringExtra("User ID");
        if(userIDMA != null) {
            Log.e("User ID Start", userIDMA);
        }

        nightCount = getNightCount();
        nightCount++;
        storeNight(nightCount);
        Toast.makeText(this, ""+nightCount, Toast.LENGTH_SHORT).show();

        ImageButton startDrinking = (ImageButton) findViewById(R.id.start_drinking);
        startDrinking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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
                Intent switchToLogin = new Intent(StartActivity.this, LoginActivity.class);
                switchToLogin.putExtra("sign out", currentUserFromLA);
                startActivity(switchToLogin);
                finish();
            }
        });

        ImageButton contact = (ImageButton) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast toast = Toast.makeText(StartActivity.this, "Contact Dr. Shacham: eshacham@slu.edu", Toast.LENGTH_LONG );
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("currentScreen","none");
        if (selectedScreen.equals("main")) {
            Intent switchToMain = new Intent(StartActivity.this, MainActivity.class);
            startActivity(switchToMain);
            finish();
        }

        else if (selectedScreen.equals("morningQS")){
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            startActivity(switchToMorning);
            finish();
        }

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

    private String getScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("user ID", "none");
        return selectedScreen;
    }

    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }

    @Override
    public void onResume(){
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("currentScreen","none");
        if (selectedScreen.equals("morningQS")){
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            startActivity(switchToMorning);
            finish();
        }
        super.onResume();
    }

}