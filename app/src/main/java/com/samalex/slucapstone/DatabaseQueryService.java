package com.samalex.slucapstone;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.file.Paths.get;

public class DatabaseQueryService {
    public static Object getCost(DataSnapshot dataSnapshot, String username, int nightCount, int cycle) {
        Object costObject = dataSnapshot.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child(nightCount + "")
                .child("Answers")
                .child("Cost").getValue();
        return costObject;
    }

    public static Object getDrinkTypes(DataSnapshot dataSnapshot, String username, int nightCount, int cycle) {
        Object drinkObject = dataSnapshot.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child(nightCount + "")
                .child("Answers")
                .child("Type").getValue();
        return drinkObject;
    }

    public static Object getDrinkSizes(DataSnapshot ds, String username, Integer nightCount, int cycle) {
        Object sizeObject = ds.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child(nightCount + "")
                .child("Answers")
                .child("Size").getValue();
        return sizeObject;
    }

    public static Object getLocations(DataSnapshot dataSnapshot, String username, Integer nightCount, int cycle) {
        Object locationObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle)
                .child("Episodes").child(nightCount + "").child("Location").getValue();
        return locationObject;
    }

    public static Object getAllEpisodes(DataSnapshot dataSnapshot, String username, Integer nightCount, int cycle) {
        Object allEpisodesObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle).child("Episodes").getValue();
        return allEpisodesObject;
    }
}
