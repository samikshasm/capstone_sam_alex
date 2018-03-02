package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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


    //initializes variables
    private Integer numberDrinks = 0;
    private String userIDMA;
    private String time;
    private DatabaseReference mDatabase;
    private String initialTimeStr;
    private String typeOfDrink;
    private String sizeOfDrink;
    private String withWhom;
    private String where;
    private String date;
    private Integer nightCount;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //initialize databse
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nightCount = getNightCount();

        //initialize all ui elements
        final ImageButton beer = (ImageButton) findViewById(R.id.beerBtn);
        final ImageButton liquor = (ImageButton) findViewById(R.id.liquorBtn);
        final ImageButton wine = (ImageButton) findViewById(R.id.wineBtn);

        final ImageButton shot = (ImageButton) findViewById(R.id.shotBtn);
        final ImageButton eight = (ImageButton) findViewById(R.id.eight);
        final ImageButton sixteen = (ImageButton) findViewById(R.id.sixteen);
        final ImageButton twentyFour = (ImageButton) findViewById(R.id.twentyFour);

        final ImageButton nobody = (ImageButton) findViewById(R.id.nobody);
        final ImageButton partner  = (ImageButton) findViewById(R.id.partner);
        final ImageButton friends = (ImageButton) findViewById(R.id.friends);
        final ImageButton other = (ImageButton) findViewById(R.id.other);

        final ImageButton home = (ImageButton) findViewById(R.id.home);
        final ImageButton work = (ImageButton) findViewById(R.id.work);
        final ImageButton bar = (ImageButton) findViewById(R.id.bar);
        final ImageButton party = (ImageButton) findViewById(R.id.party);
        final ImageButton otherPlace = (ImageButton) findViewById(R.id.otherPlace);


        //creates all of the onClick listeners for each button
        //switches the images of the buttons when one is pressed
        beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "beer";
                wine.setImageResource(R.drawable.new_wine_purple);
                beer.setImageResource(R.drawable.beer_button_small_green);
                liquor.setImageResource(R.drawable.liquor_button_small_purple);

            }
        });

        liquor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "liquor";
                wine.setImageResource(R.drawable.new_wine_purple);
                beer.setImageResource(R.drawable.beer_button_small_purple);
                liquor.setImageResource(R.drawable.liquor_button_small_green);
            }
        });

        wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "wine";
                wine.setImageResource(R.drawable.wine_button_small_green);
                beer.setImageResource(R.drawable.beer_button_small_purple);
                liquor.setImageResource(R.drawable.liquor_button_small_purple);
            }
        });

        shot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "Shot";
                shot.setImageResource(R.drawable.shot_green);
                eight.setImageResource(R.drawable.eight_purple);
                sixteen.setImageResource(R.drawable.sixteen_purple);
                twentyFour.setImageResource(R.drawable.twenty_four_purple);

            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "8";
                shot.setImageResource(R.drawable.shot_purple);
                eight.setImageResource(R.drawable.eight_green);
                sixteen.setImageResource(R.drawable.sixteen_purple);
                twentyFour.setImageResource(R.drawable.twenty_four_purple);
            }
        });

        sixteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "16";
                shot.setImageResource(R.drawable.shot_purple);
                eight.setImageResource(R.drawable.eight_purple);
                sixteen.setImageResource(R.drawable.sixteen_green);
                twentyFour.setImageResource(R.drawable.twenty_four_purple);
            }
        });

        twentyFour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sizeOfDrink = "24";
                shot.setImageResource(R.drawable.shot_purple);
                eight.setImageResource(R.drawable.eight_purple);
                sixteen.setImageResource(R.drawable.sixteen_purple);
                twentyFour.setImageResource(R.drawable.twenty_four_green);
            }
        });

        nobody.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Nobody";
                nobody.setImageResource(R.drawable.alone_button_green);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_purple);
            }
        });

        partner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Partner";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_green);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_purple);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Friends";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_green);
                other.setImageResource(R.drawable.other_purple);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Other";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_green);

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Home";
                home.setImageResource(R.drawable.home_green);
                work.setImageResource(R.drawable.work_purple);
                bar.setImageResource(R.drawable.bar_purple);
                party.setImageResource(R.drawable.party_purple);
                otherPlace.setImageResource(R.drawable.other_purple);

            }
        });

        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Work";
                home.setImageResource(R.drawable.home_purple);
                work.setImageResource(R.drawable.work_green);
                bar.setImageResource(R.drawable.bar_purple);
                party.setImageResource(R.drawable.party_purple);
                otherPlace.setImageResource(R.drawable.other_purple);

            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Bar/Restaurant";
                home.setImageResource(R.drawable.home_purple);
                work.setImageResource(R.drawable.work_purple);
                bar.setImageResource(R.drawable.bar_green);
                party.setImageResource(R.drawable.party_purple);
                otherPlace.setImageResource(R.drawable.other_purple);

            }
        });

        party.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Party";
                home.setImageResource(R.drawable.home_purple);
                work.setImageResource(R.drawable.work_purple);
                bar.setImageResource(R.drawable.bar_purple);
                party.setImageResource(R.drawable.party_green);
                otherPlace.setImageResource(R.drawable.other_purple);
            }
        });

        otherPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                where = "Other";
                home.setImageResource(R.drawable.home_purple);
                work.setImageResource(R.drawable.work_purple);
                bar.setImageResource(R.drawable.bar_purple);
                party.setImageResource(R.drawable.party_purple);
                otherPlace.setImageResource(R.drawable.other_green);

            }
        });

        //onClick listener for submit button
        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                writeTypeToDB(typeOfDrink);
                writeSizeToDB(sizeOfDrink);
                writeWhoToDB(withWhom);
                writeWhereToDB(where);
                switchToMainActivity(view);
            }
        });


        //gets shared preference variables necessary for writing to the database
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        numberDrinks = getNumDrinks();
        numberDrinks++;
        storeNumDrinks(numberDrinks);

        //cancels notification when the activity opens
        String broadcastID = getIntent().getStringExtra("broadcast Int");
        if (broadcastID != null) {
            int notificationId = Integer.parseInt(broadcastID);

            NotificationManager manager = (NotificationManager) Main2Activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

    }

    //function for switching back to the main activity after questions are answered
    public void switchToMainActivity(View view){
        Intent switchToMainActivity = new Intent (Main2Activity.this, MainActivity.class);
        switchToMainActivity.putExtra("coming from start", "come from qs");
        switchToMainActivity.putExtra("initial time", initialTimeStr);
        startActivity(switchToMainActivity);
        finish();
    }

    //function to get current time
    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        date = dateFormat.format(currentDate);
        time = timeFormat.format(currentDate);
    }


    //functions that write all of the answers to questions to the database
    public void writeTypeToDB(String type) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Type").child("Time: "+time);
        mRef.setValue(type);
    }

    public void writeSizeToDB(String size) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Size").child("Time: "+time);
        mRef.setValue(size);
    }

    public void writeWhoToDB(String who) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Who").child("Time: "+time);
        mRef.setValue(who);
    }

    public void writeWhereToDB (String where) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Where").child("Time: "+time);
        mRef.setValue(where);
    }

    //stores number of drinks as shared preference variable
    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }

    //gets number of drinks from shared preferences
    private Integer getNumDrinks () {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks",0);
        return numberDrinks;
    }

    //gets night count from shared preferences
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }
}