package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
    private Double avgCost = 0.00;
    private String[] costList;
    private String group;
    private String broadcastInt = "none";
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

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
//        numDrinks = getNumDrinks(); // cannot get from SharedPreferences because it's already reset when submitting morning questionnaire

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

        SharedPreferences mSharedPreferences2 = getSharedPreferences("Group", MODE_PRIVATE);
        group = mSharedPreferences2.getString("Group", "none");

        dismissMorningQuestionnaireNotification();
    }

    private void dismissMorningQuestionnaireNotification() {
        NotificationManager manager = (NotificationManager) MorningReport.this.getSystemService(Context.NOTIFICATION_SERVICE);
        broadcastInt = getIntent().getStringExtra("broadcast Int");
        int notificationId;
        if (broadcastInt == null) {
            notificationId = 5;
        } else {
            notificationId = Integer.parseInt(broadcastInt);
        }
        manager.cancel(notificationId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
    }

    //function to analyze data received from snapshot
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
                    int numDrinksFromDB = testType.length;
                    typeList = new String[numDrinksFromDB];
                    for (int i = 0; i< numDrinksFromDB; i++) {
                        String[] tempList = testType[i].split("=");
                        typeList[i] = tempList[1];
                    }

                    Integer numWine = 0;
                    Integer numLiquor = 0;
                    Integer numBeer = 0;
                    float percentWine = 0;
                    float percentLiquor = 0;
                    float percentBeer = 0;

                    for (int i = 0; i < numDrinksFromDB; i++ ) {

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
                    display_numDrinks.setText(""+numDrinksFromDB);

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
                    display_numDrinks.setText("0");
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
                Object sizeObject = ds.child("UID: "+userIDMA).child("Night Count: "+nightCount).child("Answers").child("Date: "+date).child("Size").getValue();
                if (sizeObject != null){
                    //splits the string properly to get the necessary data
                    String sizeDrink = sizeObject.toString();
                    Log.e("sizeDrink",sizeDrink);
                    String sizeDrinkSub = sizeDrink.substring(1, sizeDrink.length()-1);
                    Log.e("sizeDrnkSub", sizeDrinkSub);
                    String[] test = sizeDrinkSub.split(",");
                    Log.e("test", ""+test);
                    sizeList = new String[test.length];
                    for (int i =0; i<test.length; i++) {
                        String[] tempList = test[i].split("=");
                        sizeList[i] = tempList[1];
                    }

                    //initializes default values
                    totalCalConsumed = 0;
                    Integer totalOuncesConsumed = 0;

                    for (int i = 0; i < test.length; i++ ) {

                        String type = typeList[i];
                        String size = sizeList[i];

                        if (!sizeList[i].equals("Shot")) {
                            Integer sizeOfDrink = Integer.parseInt(sizeList[i]);
                            totalOuncesConsumed = totalOuncesConsumed + sizeOfDrink;
                        }

                        else {
                            totalOuncesConsumed = totalOuncesConsumed + 1;
                        }

                        int oneServingSize = 1;
                        int caloriePerOneServing = 100;

                        if (type.equals("wine")) {
                            oneServingSize = 4;
                        }
                        else if (type.equals("beer")) {
                            oneServingSize = 12;
                        }
                        else if (type.equals("liquor")) {
                            oneServingSize = 1;
                        }

                        totalCalConsumed += Integer.parseInt(size) * caloriePerOneServing / oneServingSize;
                    }

                    double totalLitersConsumed = (totalOuncesConsumed * 0.03);
                    litersDrank.setText (String.format("%.2f", totalLitersConsumed));

                    //sets the display calories textview
                    display_calories.setText(""+totalCalConsumed);

                }else{
                    //set values to null if size drink is null
                    litersDrank.setText("0");
                    display_calories.setText("0");
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
                    TextView cost_txt = (TextView) findViewById(R.id.costText);
                    cost_txt.setText(String.format("%.2f", avgCost));
                }else{
                    //Log.e("null","cost object is null");
                }
            }



        }
    }


    //function to get number of locations visited
    private void getDistance(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //checks to make sure the location branch is not null
            String usersKey = ds.getKey().toString();
            if(usersKey.equals("Users")){
                Object locationObject = ds.child("UID: "+userIDMA).child("Night Count: " + nightCount).child("Location").getValue();
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