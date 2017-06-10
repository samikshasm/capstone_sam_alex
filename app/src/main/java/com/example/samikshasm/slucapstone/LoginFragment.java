package com.example.samikshasm.slucapstone;

/**
 * Created by samikshasm on 6/6/17.
 */


import android.content.res.AssetManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;


public class LoginFragment {
    MainActivity _activity;
    int _contactID;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);

        final String contactID = LoginDBContract.LoginDBEntry._ID;

        EditText username = (EditText) findViewById(R.id.usernameField);
        EditText password = (EditText) findViewById(R.id.passwordField);

        String usernameEntry = username.getText().toString();
        String passwordEntry = password.getText().toString();

        Button submit = (Button) findViewById(R.id.submitBtn);

        return view;
    }

    public void excelRead (View view, String usernameEntry, String passwordEntry){
            try {
                AssetManager am = _activity.getAssets(); //idk if this is right lol
                InputStream i = am.open("slu_capstone_login_credentials.xls");
                Workbook wb = Workbook.getWorkbook(i);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

            }

            catch (Exception e) {

        }


    }
}
