package com.samalex.slucapstone;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by samikshasm on 7/4/2017.
 */

public class Main2Activity extends AppCompatActivity{

    private boolean currentUserBool;
    private Integer numberDrinks = 0;
    private String userIDMA;
    private String initialTimeStr;
    private String time;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       // mDatabase = FirebaseDatabase.getInstance().getReference();


   /*     currentUserBool = getIntent().getBooleanExtra("switchToMain2", false);
        if (currentUserBool == true) {
            setContentView(R.layout.activity_main2);
            currentUserBool = false;
        }*/

      /*  Toast.makeText(Main2Activity.this, ""+numberDrinks, Toast.LENGTH_SHORT).show();
        numberDrinks++;
        String numberOfDrinks = numberDrinks.toString();
        writeNumDrinksToDB(numberOfDrinks);*/

        Button goToMainActivity = (Button) findViewById(R.id.goToMainActivity);
        goToMainActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                switchToMainActivity(view);
            }
        });

        numberDrinks = getIntent().getIntExtra("number of drinks", 0);
        userIDMA = getIntent().getStringExtra("User ID");
        initialTimeStr = getIntent().getStringExtra("initial time");

    }

    public void switchToMainActivity(View view){
        Intent switchToMainActivity = new Intent (Main2Activity.this, MainActivity.class);
        //switchToMainActivity.putExtra("number of drinks", numberDrinks);
        switchToMainActivity.putExtra("coming from start", "come from qs");
        switchToMainActivity.putExtra("User ID", userIDMA);
        switchToMainActivity.putExtra("initial time", initialTimeStr);
        startActivity(switchToMainActivity);
        finish();
    }

    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = dateFormat.format(currentDate);
    }
    public void writeNumDrinksToDB(String text1) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Number of Drinks");
        mRef.setValue(text1);

    }
}

