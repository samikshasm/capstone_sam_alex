package com.samalex.slucapstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by samikshasm on 7/24/17.
 */

public class MorningQS extends AppCompatActivity {

    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_qs);
        Button submit = (Button) findViewById(R.id.submit);

        /*userIDMA = getIntent().getStringExtra("User ID");
        if (userIDMA == null){
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("User ID Morning QS", userIDMA);
        }*/

        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");


        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                startActivity1 = "start";
                storeScreen(startActivity1);
                Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                goToStart.putExtra("Start Activity", startActivity);
                //goToStart.putExtra("User ID", userIDMA);
                startActivity(goToStart);
                finish();
            }
        };
        submit.setOnClickListener(handler1);
    }

    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }
}