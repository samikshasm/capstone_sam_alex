package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
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
import java.util.List;
import java.util.Map;

/**
 * Created by AlexL on 7/29/2017.
 */


//utilized MPAndroidChart library to create pie chart visual
public class MorningReport extends AppCompatActivity {

    //initialize variables
    private int forCycle = 0;
    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";
    private DatabaseReference mReference;
    private String[] typeList;
    private String[] sizeList;
    private String[] locationList;
    private String[] longitudeList;
    private String[] latitudeList;
    private String reportForDate;
    private Integer totalCalConsumed;
    private TextView display_calories;
    private Integer numLocation;
    private TextView display_location;
    private TextView display_numDrinks;
    private PieChart pieChart;
    private float[] data = {30.0f, 30.0f, 40.0f};
    private String[] drinkNames = {"beer", "liquor", "wine"};
    private TextView litersDrank;
    private String broadcastInt = "none";
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_report);

        forCycle = getIntent().getIntExtra("this survey is for cycle", 0);

        //gets shared preference variables
        SharedPreferences userIDSharedPref = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = userIDSharedPref.getString("user ID", "none");

        //initializes firebase instance
        mReference = FirebaseDatabase.getInstance().getReference();

        //gets shared preference variables
        reportForDate = getDateOnWhichItReport();
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
        display_day.setText(reportForDate);
        litersDrank = (TextView) findViewById(R.id.liters_drank);

        //creates on Click listener for go to start button
        //resets variables
        Button goToStart = (Button) findViewById(R.id.switch_to_start);
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                startActivity1 = "start";
                storeScreen(startActivity1);
                storeNumDrinks(0);
                totalCalConsumed = 0;
                Intent goToStart = new Intent(MorningReport.this, StartActivity.class);
                goToStart.putExtra("Start Activity", startActivity);
                startActivity(goToStart);
                finish();
            }
        };
        goToStart.setOnClickListener(handler1);

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
        NotificationService.dismissNotification(this, notificationId);
    }

    //function to analyze data received from snapshot
    private void getData(DataSnapshot dataSnapshot) {

        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                List<PieEntry> yEntries = new ArrayList<>();
                List<String> xEntries = new ArrayList<>();
                List<Integer> colors = new ArrayList<>();

                int nightCount = getNightCount();

                Object allEpisodes = DatabaseQueryService.getAllEpisodes(ds, userIDMA, nightCount, forCycle);


                Object drinkTypesObject = DatabaseQueryService.getDrinkTypes(ds, userIDMA, nightCount, forCycle); // { "Time: 23:43:00": "beer", "Time: 23:42:32": "wine" }
                Object drinkSizesObject = DatabaseQueryService.getDrinkSizes(ds, userIDMA, nightCount, forCycle); // { "Time: 23:43:00": "12", "Time: 23:42:32": "14" }
                Object costObject = DatabaseQueryService.getCost(ds, userIDMA, nightCount, forCycle); // { "Time: 23:43:00": "$1.00-$5.00.", "Time: 23:42:32": "$16.00+" }

                if (drinkTypesObject != null && drinkSizesObject != null && costObject != null) {

                    // calculate numbers of and types of drink proportion consumed for rendering a pie chart
                    Map<String, String> drinkTypeseMap = (Map<String, String>) drinkTypesObject;
                    int numDrinks = drinkTypeseMap.size();
                    Map<String, Float> drinkPercentages = CalculationUtil.getDrinkPercentages(drinkTypeseMap);

                    display_numDrinks.setText("" + numDrinks);
                    display_calories.setText("" + totalCalConsumed);

                    drawPieChart(yEntries, xEntries, colors, drinkPercentages.get("beer"), drinkPercentages.get("liquor"), drinkPercentages.get("wine"));


                    // calculate amount and calories consumed
                    totalCalConsumed = 0;
                    int totalOuncesConsumed = 0;
                    Map<String, String> drinkSizeseMap = (Map<String, String>) drinkSizesObject;
                    for (Map.Entry<String, String> entry : drinkSizeseMap.entrySet()) {

                        String drinkSizeStr = entry.getValue();
                        String date = entry.getKey();
                        int sizeOfDrink = Integer.parseInt(drinkSizeStr);

                        totalOuncesConsumed += sizeOfDrink;

                        String drinkType = drinkTypeseMap.get(date);
                        int oneServingSize = CalculationUtil.DRINK_SERVING_SIZE_MAP.get(drinkType);

                        totalCalConsumed += sizeOfDrink * CalculationUtil.CALORIES_PER_SERVING / oneServingSize;
                    }

                    double totalLitersConsumed = (totalOuncesConsumed * 0.03);
                    litersDrank.setText(String.format("%.2f", totalLitersConsumed));

                    //sets the display calories textview
                    display_calories.setText("" + totalCalConsumed);


                    // calculate average cost
                    Map<String, String> costJSON = (Map<String, String>) costObject;
                    double avgCost = CalculationUtil.getAverageCost(costJSON);
                    TextView cost_txt = (TextView) findViewById(R.id.costText);
                    cost_txt.setText(String.format("%.2f", avgCost));

                } else { //if no data was entered from user

                    // sets default value
                    display_numDrinks.setText("0");

                    //set values to null if size drink is null
                    litersDrank.setText("0");
                    display_calories.setText("0");

                    // draw empty pie chart
                    drawPieChart(yEntries, xEntries, colors);
                }
            }
        }
    }

    private void drawPieChart(List<PieEntry> yEntries, List<String> xEntries, List<Integer> colors) {
        float percentBeer = 0;
        yEntries.add(new PieEntry(percentBeer, 0));
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
        float percentLiquor = 0;
        yEntries.add(new PieEntry(percentLiquor, 1));
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
        float percentWine = 0;
        yEntries.add(new PieEntry(percentWine, 2));
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));


        for (int i = 0; i < drinkNames.length; i++) {
            xEntries.add(drinkNames[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueFormatter(new PercentFormatter());

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void drawPieChart(List<PieEntry> yEntries, List<String> xEntries, List<Integer> colors, float percentBeer, float percentLiquor, float percentWine) {
        if (percentBeer > 0) {
            yEntries.add(new PieEntry(percentBeer, 0));
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
        }
        if (percentLiquor > 0) {
            yEntries.add(new PieEntry(percentLiquor, 1));
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
        }
        if (percentWine > 0) {
            yEntries.add(new PieEntry(percentWine, 2));
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));
        }


        for (int i = 0; i < drinkNames.length; i++) {
            xEntries.add(drinkNames[i]);
        }

        //creates pieData set that will populate pie chart
        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueFormatter(new PercentFormatter());

        pieDataSet.setColors(colors);

        //draws pie chart
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


    //function to get number of locations visited
    private void getDistance(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //checks to make sure the location branch is not null
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                Object locationObject = DatabaseQueryService.getLocations(ds, userIDMA, getNightCount(), forCycle);
                // { "Time: 2019-03-17 22:42:51": "37.4219983&-122.084"
                if (locationObject != null) {
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

                    for (int i = 0; i < testType.length; i++) {
                        String[] tempList = locationList[i].split("&");
                        latitudeList[i] = tempList[0];
                        longitudeList[i] = tempList[1];
                    }

                    float distance = 0;
                    numLocation = 1;

                    //gets the difference in location between two latitude and longitude points
                    for (int i = 0; i < testType.length - 1; i++) {
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
                } else {
                    //if no locations were recorded, sets textview to 0
                    display_location.setText("0");
                }
            }

        }
    }

    public String getDateOnWhichItReport() {
        // TODO: change logic to get the cycle's date on which it is reporting
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentDate);
    }

    //function to store the screen
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }

    //function to store the number of drinks
    private void storeNumDrinks(Integer integer) {
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
    private Integer getNumDrinks() {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks", 0);
        return numberDrinks;
    }
}