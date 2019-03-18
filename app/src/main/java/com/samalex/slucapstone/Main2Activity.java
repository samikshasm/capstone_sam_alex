package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private String drinkCost;
    private Integer drinksCounter=0;
    private Integer peopleCounter=0;
    private boolean typeCheck = false;
//    private boolean sizeCheck = false;
    private boolean withCheck = false;
    private boolean whereCheck = false;
    private boolean costCheck = false;
    private String broadcastInt = "none";

    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";


    private TextView wineSizeLabel;
    private TextView beerSizeLabel;
    private TextView liquorSizeLabel;
    private int wineSize = 0;
    private int beerSize = 0;
    private int liquorSize = 0;

    SeekBar.OnSeekBarChangeListener wineSizeChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            wineSizeLabel.setText(progress + " oz.");
            wineSize = progress;
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };
    SeekBar.OnSeekBarChangeListener beerSizeChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            beerSizeLabel.setText(progress + " oz.");
            beerSize = progress;
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };
    SeekBar.OnSeekBarChangeListener liquorSizeChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            liquorSizeLabel.setText(progress + " oz (" + progress + " shot(s))");
            liquorSize = progress;
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

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

//        final ImageButton eight = (ImageButton) findViewById(R.id.eight);
//        final ImageButton sixteen = (ImageButton) findViewById(R.id.sixteen);
//        final ImageButton twentyFour = (ImageButton) findViewById(R.id.twentyFour);

        final ImageButton nobody = (ImageButton) findViewById(R.id.nobody);
        final ImageButton partner  = (ImageButton) findViewById(R.id.partner);
        final ImageButton friends = (ImageButton) findViewById(R.id.friends);
        final ImageButton other = (ImageButton) findViewById(R.id.other);

        final ImageButton home = (ImageButton) findViewById(R.id.home);
        final ImageButton work = (ImageButton) findViewById(R.id.work);
        final ImageButton bar = (ImageButton) findViewById(R.id.bar);
        final ImageButton party = (ImageButton) findViewById(R.id.party);
        final ImageButton otherPlace = (ImageButton) findViewById(R.id.otherPlace);

        final RadioButton drink0 = (RadioButton) findViewById(R.id.radio_0);
        final RadioButton drink1_5 = (RadioButton) findViewById(R.id.radio_1_5);
        final RadioButton drink6_10 = (RadioButton) findViewById(R.id.radio_6_10);
        final RadioButton drink11_15 = (RadioButton) findViewById(R.id.radio_11_15);
        final RadioButton drink16plus = (RadioButton) findViewById(R.id.radio_16plus);
        final RadioGroup costGroup = (RadioGroup) findViewById(R.id.radioCost);



        broadcastInt = getIntent().getStringExtra("broadcast Int");
        if(broadcastInt != null){
            int notificationId = Integer.parseInt(broadcastInt);
            NotificationManager manager = (NotificationManager) Main2Activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                manager.deleteNotificationChannel(CHANNEL_ID);
            }

        }

        final SeekBar liquorSizeSelector = (SeekBar) findViewById(R.id.liquor_size_selector);
        final SeekBar wineSizeSelector = (SeekBar) findViewById(R.id.wine_size_selector);
        final SeekBar beerSizeSelector = (SeekBar) findViewById(R.id.beer_size_selector);

        final LinearLayout wineSizeSelectorWrapper = (LinearLayout) findViewById(R.id.wine_size_selector_wrapper);
        final LinearLayout beerSizeSelectorWrapper = (LinearLayout) findViewById(R.id.beer_size_selector_wrapper);
        final LinearLayout liquorSizeSelectorWrapper = (LinearLayout) findViewById(R.id.liquor_size_selector_wrapper);

        // set a change listener on the SeekBar
        wineSizeSelector.setOnSeekBarChangeListener(wineSizeChangeListener);
        beerSizeSelector.setOnSeekBarChangeListener(beerSizeChangeListener);
        liquorSizeSelector.setOnSeekBarChangeListener(liquorSizeChangeListener);


        wineSize = wineSizeSelector.getProgress();
        wineSizeLabel = findViewById(R.id.wine_size_text);
        wineSizeLabel.setText(wineSize + " oz");

        beerSize = beerSizeSelector.getProgress();
        beerSizeLabel = findViewById(R.id.beer_size_text);
        beerSizeLabel.setText(beerSize + " oz");

        liquorSize = liquorSizeSelector.getProgress();
        liquorSizeLabel = findViewById(R.id.liquor_size_text);
        liquorSizeLabel.setText(liquorSize + " oz (" + liquorSize + " shot(s))");


        drink1_5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                drink1_5.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.green));
                drink0.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink6_10.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink11_15.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink16plus.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));

            }
        });

        drink0.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                drink0.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.green));
                drink1_5.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink6_10.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink11_15.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink16plus.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));

            }
        });

        drink6_10.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                drink6_10.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.green));
                drink0.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink1_5.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink11_15.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink16plus.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));

            }
        });

        drink11_15.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                drink11_15.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.green));
                drink0.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink6_10.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink1_5.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink16plus.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));

            }
        });

        drink16plus.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                drink16plus.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.green));
                drink0.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink6_10.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink11_15.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
                drink1_5.setButtonTintList(ContextCompat.getColorStateList(Main2Activity.this, R.color.purple));
            }
        });

        //creates all of the onClick listeners for each button
        //switches the images of the buttons when one is pressed
        beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "beer";
                wine.setImageResource(R.drawable.new_wine_purple);
                beer.setImageResource(R.drawable.beer_button_small_green);
                liquor.setImageResource(R.drawable.liquor_button_small_purple);
                typeCheck = true;

                wineSizeSelectorWrapper.setVisibility(View.GONE);
                beerSizeSelectorWrapper.setVisibility(View.VISIBLE);
                liquorSizeSelectorWrapper.setVisibility(View.GONE);
            }
        });

        liquor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "liquor";
                wine.setImageResource(R.drawable.new_wine_purple);
                beer.setImageResource(R.drawable.beer_button_small_purple);
                liquor.setImageResource(R.drawable.liquor_button_small_green);
                typeCheck = true;

                wineSizeSelectorWrapper.setVisibility(View.GONE);
                beerSizeSelectorWrapper.setVisibility(View.GONE);
                liquorSizeSelectorWrapper.setVisibility(View.VISIBLE);
            }
        });

        wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                typeOfDrink = "wine";
                wine.setImageResource(R.drawable.wine_button_small_green);
                beer.setImageResource(R.drawable.beer_button_small_purple);
                liquor.setImageResource(R.drawable.liquor_button_small_purple);
                typeCheck = true;

                wineSizeSelectorWrapper.setVisibility(View.VISIBLE);
                beerSizeSelectorWrapper.setVisibility(View.GONE);
                liquorSizeSelectorWrapper.setVisibility(View.GONE);
            }
        });

        nobody.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Nobody";
                nobody.setImageResource(R.drawable.alone_button_green);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_purple);
                withCheck = true;
            }
        });

        partner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Partner";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_green);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_purple);
                withCheck = true;
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Friends";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_green);
                other.setImageResource(R.drawable.other_purple);
                withCheck = true;
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withWhom = "Other";
                nobody.setImageResource(R.drawable.alone_button_purple);
                partner.setImageResource(R.drawable.couple_purple);
                friends.setImageResource(R.drawable.friends_purple);
                other.setImageResource(R.drawable.other_green);
                withCheck = true;

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
                whereCheck = true;

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
                whereCheck = true;

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
                whereCheck = true;

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
                whereCheck = true;
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
                whereCheck = true;

            }
        });

        //onClick listener for submit button
        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        costCheck = true;

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                if((!whereCheck || !withCheck || !typeCheck || !costCheck || !sizeCheck)){
                if((!whereCheck || !withCheck || !typeCheck || !costCheck)){
                    Log.e("check","false");
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(Main2Activity.this, R.style.MyDialogTheme);
                    } else {
                        builder = new AlertDialog.Builder(Main2Activity.this);
                    }
                    builder.setTitle("Try Again")
                            .setMessage("Please respond to every question!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(R.drawable.white_small_icon)
                            .show();
                }else{
                    Log.e("check","true");
                    switch (typeOfDrink) {
                        case "wine":
                            sizeOfDrink = wineSize + "";
                            break;
                        case "beer":
                            sizeOfDrink = beerSize + "";
                            break;
                        case "liquor":
                            sizeOfDrink = liquorSize + "";
                            break;
                    }
                    writeTypeToDB(typeOfDrink);
                    writeSizeToDB(sizeOfDrink);
                    writeWhoToDB(withWhom);
                    writeWhereToDB(where);
                    writePlannedDrinksToDB(drinksCounter);
                    writeNumberOfPeopleToDB(peopleCounter);
                    // get selected radio button from radioGroup
                    int selectedId = costGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton costButton;
                    costButton = (RadioButton) findViewById(selectedId);
                    drinkCost = costButton.getText().toString();
                    writeCostToDB(drinkCost);
                    switchToMainActivity(view);
                }

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
        SharedPreferences settings = getSharedPreferences("NumberSwitchesToMain2Activity", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("switches", 2);
        editor.commit();

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

        Log.e("date: ",""+date);
        Log.e("time:",""+time);
    }

    public void writeCostToDB(String drink_cost){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Cost").child("Time: "+time);
        mRef.setValue(drink_cost);
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

    public void writePlannedDrinksToDB (Integer drinksCounter) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("DrinksPlanned").child("Time: "+time);
        mRef.setValue(drinksCounter);
    }

    public void writeNumberOfPeopleToDB (Integer peopleCounter) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("NumberOfPeople").child("Time: "+time);
        mRef.setValue(peopleCounter);
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