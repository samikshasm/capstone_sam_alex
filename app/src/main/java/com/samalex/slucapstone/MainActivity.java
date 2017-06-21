package com.samalex.slucapstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String userIDMA;
    private String text;
    private EditText textbox;
    private String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userIDMA = getIntent().getStringExtra("User ID");
        Toast.makeText(MainActivity.this, userIDMA,
                Toast.LENGTH_SHORT).show();

        textbox = (EditText) findViewById(R.id.text);
        Button saveDB = (Button) findViewById(R.id.saveDB);
        saveDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentDateTime = System.currentTimeMillis();
                Date currentDate = new Date(currentDateTime);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = dateFormat.format(currentDate);
                Toast.makeText(MainActivity.this, time,
                        Toast.LENGTH_SHORT).show();
                text = textbox.getText().toString();
                writeToDB(text);
            }
        });

        //allows data collection in the absence of wifi
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void writeToDB(String text1) {
        DatabaseReference mRef = mDatabase.child("Users").child(userIDMA).child(time);
        mRef.setValue(text1);

    }


}
