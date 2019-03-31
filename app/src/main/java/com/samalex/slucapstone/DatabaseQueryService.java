package com.samalex.slucapstone;

import com.google.firebase.database.DataSnapshot;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseQueryService {
    public static Object getCost(DataSnapshot dataSnapshot, String username, int nightCount, String date) {
        Object costObject = dataSnapshot.child("UID: " + username)
                .child("Night Count: " + nightCount)
                .child("Answers")
                .child("Date: " + date)
                .child("Cost").getValue();
        return costObject;
    }
}
