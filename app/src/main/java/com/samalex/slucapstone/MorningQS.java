package com.samalex.slucapstone;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by samikshasm on 7/24/17.
 */

public class MorningQS extends AppCompatActivity {

    private String userIDMA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_qs);
        Button submit = (Button) findViewById(R.id.submit);

        userIDMA = getIntent().getStringExtra("User ID");
    }
}