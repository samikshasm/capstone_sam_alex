package com.samalex.slucapstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String userIDMA;
    private String text;
    private EditText textbox;
    private int counter = 1;
    private String counterStr;
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
                counter ++;
                counterStr = Integer.toString(counter);
                text = textbox.getText().toString();
                Toast.makeText(MainActivity.this, text,
                        Toast.LENGTH_SHORT).show();
                writeToDB(text);
            }
        });

        //allows data collection in the absence of wifi
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void writeToDB(String text1) {
        DatabaseReference mRef = mDatabase.child("Users").child(userIDMA).child(counterStr);
        mRef.setValue(text1);

    }


}
