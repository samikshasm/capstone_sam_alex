package com.example.samikshasm.slucapstone;

import android.provider.BaseColumns;

/**
 * Created by samikshasm on 6/6/17.
 */

public class LoginDBContract {
    private LoginDBContract() {}

    /* Inner class that defines the table contents */
    public static class LoginDBEntry implements BaseColumns {
        public static final String TABLE_NAME = "Login";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
}


