package com.example.samikshasm.slucapstone;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    LoginFragment _loginFragment;
    SQLiteDatabase _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize the fragments if they do not exist
        if (_loginFragment == null) {
            _loginFragment = new LoginFragment();
            _loginFragment.setActivity(this);
        }


        // Add the initial fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.activity_contact, _loginFragment).commit();

        // Initialize the database if it does not exist
        if (_db == null) {
            LoginDatabaseHelper dbHelper = new LoginDatabaseHelper(getBaseContext());
            _db = dbHelper.getWritableDatabase();
        }
    }

    public void switchFragment(String newFragment) {
        if (newFragment == "login") {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.activity_contact, _loginFragment).commit();
        }
    }

}
