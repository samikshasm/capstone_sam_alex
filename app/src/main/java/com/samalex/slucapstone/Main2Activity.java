package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String counterStr;
    private Integer counterInt;
    private String time;
    private DatabaseReference mDatabase;
    private String typeOfDrink;
    private String sizeOfDrink;
    private String withWhom;
    private String where;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button beer = (Button) findViewById(R.id.beerBtn);
        Button liquor = (Button) findViewById(R.id.liquorBtn);
        Button wine = (Button) findViewById(R.id.wineBtn);

        Button shot = (Button) findViewById(R.id.shotBtn);
        Button eight = (Button) findViewById(R.id.eight);
        Button sixteen = (Button) findViewById(R.id.sixteen);
        Button twentyFour = (Button) findViewById(R.id.twentyFour);

        Button nobody = (Button) findViewById(R.id.nobody);
        Button partner  = (Button) findViewById(R.id.partner);
        Button friends = (Button) findViewById(R.id.friends);
        Button other = (Button) findViewById(R.id.other);

        Button home = (Button) findViewById(R.id.home);
        Button work = (Button) findViewById(R.id.work);
        Button bar = (Button) findViewById(R.id.bar);
        Button party = (Button) findViewById(R.id.party);
        Button otherPlace = (Button) findViewById(R.id.otherPlace);


        beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "beer";
            }
        });

        liquor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "liquor";
            }
        });

        wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "wine";
            }
        });

        shot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "Shot";
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "8";
            }
        });

        sixteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "16";
            }
        });

        twentyFour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "24";

            }
        });

        nobody.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Nobody";
            }
        });

        partner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Partner";
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Friends";
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Other";

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Home";

            }
        });

        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Work";

            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Bar/Restaurant";

            }
        });

        party.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Party";

            }
        });

        otherPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Other";

            }
        });

        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeNumDrinks(0);
                writeTypeToDB(typeOfDrink);
                writeSizeToDB(sizeOfDrink);
                writeWhoToDB(withWhom);
                writeWhereToDB(where);
                switchToMainActivity(view);
            }
        });

        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        numberDrinks = getNumDrinks();
        numberDrinks++;
        storeNumDrinks(numberDrinks);
        String numberOfDrinks = numberDrinks.toString();
       // writeNumDrinksToDB(numberOfDrinks);

        String broadcastID = getIntent().getStringExtra("broadcast Int");
        if (broadcastID!= null) {
            int notificationId = Integer.parseInt(broadcastID);
            Toast.makeText(this, broadcastID, Toast.LENGTH_SHORT).show();

            NotificationManager manager = (NotificationManager) Main2Activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

    }

    public void switchToMainActivity(View view){
        Intent switchToMainActivity = new Intent (Main2Activity.this, MainActivity.class);
        switchToMainActivity.putExtra("coming from start", "come from qs");
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
    public void writeNumDrinksToDB(String text1, String currentTime) {
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Answers").child(time);
        mRef.setValue("Number of Drinks, " +text1);
    }

    public void writeTypeToDB(String type) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Answers").child(time).child("Type");
        mRef.setValue(type);
    }

    public void writeSizeToDB(String size) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Answers").child(time).child("Size");
        mRef.setValue(size);
    }

    public void writeWhoToDB(String who) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Answers").child(time).child("Who");
        mRef.setValue(who);
    }

    public void writeWhereToDB (String where) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child(userIDMA).child("Answers").child(time).child("Where");
        mRef.setValue(where);
    }
    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }

    private Integer getNumDrinks () {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks",0);
        return numberDrinks;
    }
}