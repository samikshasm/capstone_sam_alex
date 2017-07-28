package com.samalex.slucapstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        userIDMA = getIntent().getStringExtra("User ID");
        if(userIDMA != null) {
            Log.e("User ID Start", userIDMA);
        }

        //storeUserID(userIDMA);

        //String hello = getScreen();
        //Toast.makeText(this, hello, Toast.LENGTH_SHORT).show();

        Button startDrinking = (Button) findViewById(R.id.start_drinking);
        startDrinking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                id = 2;
                startActivity = "start";
                Intent switchToMainActivity = new Intent(StartActivity.this, MainActivity.class);
                switchToMainActivity.putExtra("coming from start", "start");
                switchToMainActivity.putExtra("Start Activity", startActivity);
                // switchToMainActivity.putExtra("User ID", userIDMA);
                switchToMainActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(switchToMainActivity);
                finish();

            }
        });

        //sign out button
        Button signOut = (Button) findViewById(R.id.sign_out_button);
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

        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("currentScreen","none");
        if (selectedScreen.equals("main")) {
            Intent switchToMain = new Intent(StartActivity.this, MainActivity.class);
            // switchToMain.putExtra("User ID", userIDMA);
            startActivity(switchToMain);
            finish();
        }

        else if (selectedScreen.equals("morningQS")){
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            // switchToMorning.putExtra("User ID", userIDMA);
            startActivity(switchToMorning);
            finish();
        }


        //Toast.makeText(this, selectedScreen, Toast.LENGTH_SHORT).show();

        startActivity = getIntent().getStringExtra("Start Activity");
        if (startActivity != null){
            if (startActivity.equals("main" )){
                startActivity = "start";

            }
        }


    }

    private void storeUserID(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("user ID", string);
        mEditor.apply();
    }

    private String getScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("user ID", "none");
        return selectedScreen;
    }

    @Override
    public void onResume(){
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("currentScreen","none");
        if (selectedScreen.equals("morningQS")){
            Intent switchToMorning = new Intent(StartActivity.this, MorningQS.class);
            // switchToMorning.putExtra("User ID", userIDMA);
            startActivity(switchToMorning);
            finish();
        }
        super.onResume();
    }

}