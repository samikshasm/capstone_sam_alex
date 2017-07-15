package com.samalex.slucapstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by samikshasm on 7/4/2017.
 */

public class Main2Activity extends AppCompatActivity{

    private boolean currentUserBool;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        currentUserBool = getIntent().getBooleanExtra("switchToMain2", false);
        if (currentUserBool == true) {
            setContentView(R.layout.activity_main2);
            currentUserBool = false;
        }

        Button goToMainActivity = (Button) findViewById(R.id.goToMainActivity);
        goToMainActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });
    }
}
