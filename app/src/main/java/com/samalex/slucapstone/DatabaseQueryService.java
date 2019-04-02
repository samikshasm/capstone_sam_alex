package com.samalex.slucapstone;

import com.google.firebase.database.DataSnapshot;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseQueryService {
    public static Object getCost(DataSnapshot dataSnapshot, String username, int nightCount, String date, int cycle) {
        Object costObject = dataSnapshot.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child("Episode: " + nightCount)
                .child("Answers")
                .child("Date: " + date)
                .child("Cost").getValue();
        return costObject;
    }

    public static Object getDrinkTypes(DataSnapshot dataSnapshot, String username, int nightCount, String date, int cycle) {
        Object drinkObject = dataSnapshot.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child("Episode: " + nightCount)
                .child("Answers")
                .child("Date: " + date)
                .child("Type").getValue();
        return drinkObject;
    }

    public static Object getDrinkSizes(DataSnapshot ds, String username, Integer nightCount, String date, int cycle) {
        Object sizeObject = ds.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child("Episode: " + nightCount)
                .child("Answers")
                .child("Date: " + date)
                .child("Size").getValue();
        return sizeObject;
    }

    public static Object getLocations(DataSnapshot dataSnapshot, String username, Integer nightCount, int cycle) {
        Object locationObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle)
                .child("Episodes").child("Episode: " + nightCount).child("Location").getValue();
        return locationObject;
    }
}
