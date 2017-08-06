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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AlexL on 7/29/2017.
 */

public class MorningReport extends AppCompatActivity{

    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";
    private DatabaseReference mReference;
    private List<Object> type_drink;
    private String type;
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
    private TextView display_wine_percent;
    private TextView display_beer_percent;
    private TextView display_liquor_percent;
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

        SharedPreferences userIDSharedPref = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = userIDSharedPref.getString("user ID", "none");

        mReference = FirebaseDatabase.getInstance().getReference();

        getTime();
        nightCount = getNightCount();
        numDrinks = getNumDrinks();

        pieChart = (PieChart) findViewById(R.id.pie_chart);
        pieChart.setHoleRadius(0f);
        pieChart.setHoleColor(ContextCompat.getColor(MorningReport.this, R.color.app_background));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.getDescription().setEnabled(false);

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

        SharedPreferences numDrinksSharedPref = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = numDrinksSharedPref.getInt("numDrinks",0);
        String numberDrinksStr = numberDrinks.toString();

        display_numDrinks = (TextView) findViewById(R.id.display_numDrinks);
        display_calories = (TextView) findViewById(R.id.display_calories);
        display_location = (TextView) findViewById(R.id.numLocationValue);
        TextView display_day = (TextView) findViewById(R.id.display_day);
        display_day.setText(date);
        litersDrank = (TextView) findViewById(R.id.liters_drank);

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

    private void addDataChart(){
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < data.length; i++){
            yEntrys.add(new PieEntry(data[i], i));
        }
        for (int i = 0; i < drinkNames.length; i++){
            xEntrys.add(drinkNames[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(0);
        pieDataSet.setValueTextSize(50);
        pieDataSet.setValueTextColor(ContextCompat.getColor(MorningReport.this, R.color.white));
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
        colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    private void getData(DataSnapshot dataSnapshot) {


        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String typeDrink = ds.child(userIDMA).child(""+nightCount).child("Answers").child(date).child("Type").getValue().toString();
            String typeDrinkSub = typeDrink.substring(1, typeDrink.length()-1);
            String[] testType = typeDrinkSub.split(",");
            typeList = new String[testType.length];
            for (int i =0; i<testType.length; i++) {
                String[] tempList = testType[i].split("=");
                typeList[i] = tempList[1];
            }

            String sizeDrink = ds.child(userIDMA).child(""+nightCount).child("Answers").child(date).child("Size").getValue().toString();
            String sizeDrinkSub = sizeDrink.substring(1, sizeDrink.length()-1);
            String[] test = sizeDrinkSub.split(",");
            sizeList = new String[test.length];
            for (int i =0; i<test.length; i++) {
                String[] tempList = test[i].split("=");
                sizeList[i] = tempList[1];
            }

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

            totalCalConsumed = 0;
            Integer totalOuncesConsumed = 0;
            Integer numWine = 0;
            Integer numLiquor = 0;
            Integer numBeer = 0;
            float percentWine = 0;
            float percentLiquor = 0;
            float percentBeer = 0;

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
                    numWine++;

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
                    numLiquor++;

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
                    numBeer++;

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

            int numberDrinks = numBeer+numWine+numLiquor;
            percentBeer = ((float) numBeer/ (float) numberDrinks)*100;
            percentLiquor = ((float) numLiquor/ (float) numberDrinks)*100;
            percentWine = ((float) numWine/ (float) numberDrinks)*100;

            display_numDrinks.setText(""+numberDrinks);

            Log.e("numBeer", ""+numBeer);
            Log.e("numLiquor", ""+numLiquor);
            Log.e("numWine", ""+numWine);
            Log.e("Total drinks", ""+numberDrinks);

            display_calories.setText(""+totalCalConsumed);

            ArrayList<PieEntry> yEntrys = new ArrayList<>();
            ArrayList<String> xEntrys = new ArrayList<>();

            yEntrys.add(new PieEntry(percentBeer, 0));
            yEntrys.add(new PieEntry(percentLiquor, 1));
            yEntrys.add(new PieEntry(percentWine, 2));

            for (int i = 0; i < drinkNames.length; i++){
                xEntrys.add(drinkNames[i]);
            }
            PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(20);
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.pink));
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.orange));
            colors.add(ContextCompat.getColor(MorningReport.this, R.color.green));

            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();

        }
    }

    private void getDistance(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String location = ds.child(userIDMA).child("" + nightCount).child("Location").getValue().toString();
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

            for (int i =0; i<testType.length-1; i++) {
                float lat1 = Float.parseFloat(latitudeList[i]);
                float lon1 = Float.parseFloat(longitudeList[i]);
                float lat2 = Float.parseFloat(latitudeList[i+1]);
                float lon2 = Float.parseFloat(longitudeList[i+1]);

                Location locationA = new Location("pointA");
                Location locationB = new Location("pointB");
                locationA.setLatitude(lat1);
                locationA.setLongitude(lon1);
                locationB.setLatitude(lat2);
                locationB.setLongitude(lon2);
                distance = locationA.distanceTo(locationB);
                Log.e("distance", ""+distance);

                if (distance > 1610) {
                    numLocation++;
                }

                display_location.setText(""+numLocation);
            }
        }
    }

    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(currentDate);
    }
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }

    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }

    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }

    private Integer getNumDrinks () {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        Integer numberDrinks = mSharedPreferences.getInt("numDrinks",0);
        return numberDrinks;
    }

}
