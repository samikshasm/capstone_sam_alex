package com.samalex.slucapstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AlexL on 7/29/2017.
 */


//utilized MPAndroidChart library to create pie chart visual
public class MorningReport extends AppCompatActivity{

    //initialize variables
    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";
    private DatabaseReference mReference;
    private String[] typeList;
    private String[] sizeList;
    private String[] locationList;
    private String[] longitudeList;
    private String[] latitudeList;
    private String date;
    private Integer totalCalConsumed;
    private TextView display_calories;
    private Integer nightCount;
    private Integer numLocation;
    private TextView display_location;
    private TextView display_numDrinks;
    private Integer numDrinks;
    private PieChart pieChart;
    private float[] data = {30.0f, 30.0f, 40.0f};
    private String[] drinkNames = {"beer", "liquor", "wine"};
    private TextView litersDrank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_report);

        //gets shared preference variables
        SharedPreferences userIDSharedPref = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = userIDSharedPref.getString("user ID", "none");

        //initializes firebase instance
        mReference = FirebaseDatabase.getInstance().getReference();

        //gets shared preference variables
        getTime();
        nightCount = getNightCount();
        numDrinks = getNumDrinks();

        //initializes pie chart and sets basic features
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        pieChart.setHoleRadius(0f);
        pieChart.setHoleColor(ContextCompat.getColor(MorningReport.this, R.color.app_background));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);


        //gets a snapshot of the data at the given instance from firebase
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getData(dataSnapshot);
                getDistance(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //gets shared preference information needed
        SharedPreferences numDrinksSharedPref = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = numDrinksSharedPref.getInt("numDrinks",0);
        String numberDrinksStr = numberDrinks.toString();

        //initializes ui elements
        display_numDrinks = (TextView) findViewById(R.id.display_numDrinks);
        display_calories = (TextView) findViewById(R.id.display_calories);
        display_location = (TextView) findViewById(R.id.numLocationValue);
        TextView display_day = (TextView) findViewById(R.id.display_day);
        display_day.setText(date);
        litersDrank = (TextView) findViewById(R.id.liters_drank);

        //creates on Click listener for go to start button
        //resets variables
        Button goToStart = (Button) findViewById(R.id.switch_to_start);
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                startActivity1 = "start";
                storeScreen(startActivity1);
                storeNumDrinks(0);
                totalCalConsumed =0;
                Intent goToStart = new Intent(MorningReport.this, StartActivity.class);
                goToStart.putExtra("Start Activity", startActivity);
                startActivity(goToStart);
                finish();
            }
        };
        goToStart.setOnClickListener(handler1);
    }

    //function to analyze data received from snapshot
    private void getData(DataSnapshot dataSnapshot) {

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //gets information from database
            //makes sure that there is data at the given branch
            Object typeObject = ds.child(userIDMA).child(""+nightCount).child("Answers").child(date).child("Type").getValue();
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

                Integer numWine = 0;
                Integer numLiquor = 0;
                Integer numBeer = 0;
                float percentWine = 0;
                float percentLiquor = 0;
                float percentBeer = 0;

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
                percentBeer = ((float) numBeer/ (float) numberDrinks)*100;
                percentLiquor = ((float) numLiquor/ (float) numberDrinks)*100;
                percentWine = ((float) numWine/ (float) numberDrinks)*100;

                //sets the display drinks textview
                display_numDrinks.setText(""+numDrinks);

                //sets the display calories textview
                display_calories.setText(""+totalCalConsumed);

                //initializes arrays
                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();

                //checks to see if at least one of the type was drank
                //adds percent to data list and adds color to color list
                if(percentBeer > 0){
                    yEntrys.add(new PieEntry(percentBeer, 0));
                    colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
                }
                if(percentLiquor > 0){
                    yEntrys.add(new PieEntry(percentLiquor, 1));
                    colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
                }
                if(percentWine > 0){
                    yEntrys.add(new PieEntry(percentWine, 2));
                    colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));
                }


                for (int i = 0; i < drinkNames.length; i++){
                    xEntrys.add(drinkNames[i]);
                }

                //creates pieData set that will populate pie chart
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(20);
                pieDataSet.setValueTextColor(R.color.white);
                pieDataSet.setValueFormatter(new PercentFormatter());

                pieDataSet.setColors(colors);

                //draws pie chart
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            }else{
                //if no data was entered from user, sets default value
                display_numDrinks.setText(""+numDrinks);
                //populates an empty pie chart if size of drink is null
                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();

                float percentBeer = 0;
                yEntrys.add(new PieEntry(percentBeer, 0));
                colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
                float percentLiquor = 0;
                yEntrys.add(new PieEntry(percentLiquor, 1));
                colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
                float percentWine = 0;
                yEntrys.add(new PieEntry(percentWine, 2));
                colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));


                for (int i = 0; i < drinkNames.length; i++){
                    xEntrys.add(drinkNames[i]);
                }
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(20);
                pieDataSet.setValueTextColor(R.color.white);
                pieDataSet.setValueFormatter(new PercentFormatter());

                pieDataSet.setColors(colors);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
            }

            //checks to make sure data was actually entered
            Object sizeObject = ds.child(userIDMA).child(""+nightCount).child("Answers").child(date).child("Size").getValue();
            if (sizeObject != null){
                //splits the string properly to get the necessary data
                String sizeDrink = sizeObject.toString();
                String sizeDrinkSub = sizeDrink.substring(1, sizeDrink.length()-1);
                String[] test = sizeDrinkSub.split(",");
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
                    totalCalConsumed+= i;

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

                double totalLitersConsumed = (totalOuncesConsumed * 0.03);
                litersDrank.setText (""+totalLitersConsumed);

                //sets the display drinks textview
                display_numDrinks.setText(""+numDrinks);

                //sets the display calories textview
                display_calories.setText(""+totalCalConsumed);

            }else{
                //set values to null if size drink is null
                litersDrank.setText("0");
                display_calories.setText("0");
                display_numDrinks.setText(""+numDrinks);
            }


        }
    }


    //function to get number of locations visited
    private void getDistance(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //checks to make sure the location branch is not null
            Object locationObject = ds.child(userIDMA).child("" + nightCount).child("Location").getValue();
            if (locationObject != null){
                //gets the specific latitude and longitude string from database
                String location = locationObject.toString();
                String locationSub = location.substring(1, location.length() - 1);
                String[] testType = locationSub.split(",");
                locationList = new String[testType.length];
                longitudeList = new String[testType.length];
                latitudeList = new String[testType.length];

                for (int i = 0; i < testType.length; i++) {
                    String[] tempList = testType[i].split("=");
                    locationList[i] = tempList[1];
                }

                for (int i =0; i<testType.length;i++) {
                    String[] tempList = locationList[i].split("&");
                    latitudeList[i] = tempList[0];
                    longitudeList[i] = tempList[1];
                }

                float distance = 0;
                numLocation = 1;

                //gets the difference in location between two latitude and longitude points
                for (int i =0; i<testType.length-1; i++) {
                    float lat1 = Float.parseFloat(latitudeList[i]);
                    float lon1 = Float.parseFloat(longitudeList[i]);
                    float lat2 = Float.parseFloat(latitudeList[i + 1]);
                    float lon2 = Float.parseFloat(longitudeList[i + 1]);

                    Location locationA = new Location("pointA");
                    Location locationB = new Location("pointB");
                    locationA.setLatitude(lat1);
                    locationA.setLongitude(lon1);
                    locationB.setLatitude(lat2);
                    locationB.setLongitude(lon2);
                    distance = locationA.distanceTo(locationB);
                    Log.e("distance", "" + distance);

                    //if the distance is greater than a mile, then they traveled to a new location
                    if (distance > 1610) {
                        numLocation++;
                    }

                    //sets the display location textview to number of locations visited
                    display_location.setText("" + numLocation);
                }
            }else{
                //if no locations were recorded, sets textview to 0
                display_location.setText("0");
            }
        }
    }

    //function to get the current time
    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(currentDate);
    }
    //function to store the screen
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }
    //function to store the number of drinks
    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }
    //gets night count from shared preferences
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }
    //gets number of drinks from shared preferences
    private Integer getNumDrinks () {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks",0);
        return numberDrinks;
    }

}