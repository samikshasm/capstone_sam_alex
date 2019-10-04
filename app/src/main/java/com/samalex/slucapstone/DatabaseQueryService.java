package com.samalex.slucapstone;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.samalex.slucapstone.dto.DrinkAnswer;

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
                .child("cost").getValue();
        return costObject;
    }

    public static Object getDrinkTypes(DataSnapshot dataSnapshot, String username, int nightCount, int cycle) {
        Object drinkObject = dataSnapshot.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child(nightCount + "")
                .child("Answers")
                .child("type").getValue();
        return drinkObject;
    }

    public static Object getDrinkSizes(DataSnapshot ds, String username, Integer nightCount, int cycle) {
        Object sizeObject = ds.child("UID: " + username)
                .child("Cycle: " + cycle)
                .child("Episodes")
                .child(nightCount + "")
                .child("Answers")
                .child("size").getValue();
        return sizeObject;
    }

    public static List<Location> getLocations(DataSnapshot dataSnapshot, String username, int cycle) {
//        Object locationObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle)
//                .child("Episodes").child(nightCount + "").child("Location").getValue();
//        return locationObject;
        List<Location> locationList = new ArrayList<>();
        List episodeList = (List) dataSnapshot.child("UID: " + username).child("Cycle: " + cycle).child("Episodes").getValue();
        if(episodeList != null) {
            for (int j = 0; j < episodeList.size(); j++) {
                Map<String, Object> episode = (Map<String, Object>) episodeList.get(j);
                if (episode == null) continue;
                Map<String, String> locationMap = (Map<String, String>) episode.get("Location");

                if(locationMap != null) {
                    long locationNum = locationMap.size();

                    Object[] strLocationList = locationMap.values().toArray();
                    for (int i = 0; i < locationNum; i++) {
                        String strLocation = strLocationList[i].toString();
                        String[] latlong = strLocation.split("&");

                        Location l = new Location("location" + i);
                        l.setLatitude(Float.parseFloat(latlong[0]));
                        l.setLongitude(Float.parseFloat(latlong[1]));
                        locationList.add(l);
                    }
                }
            }
        }
        return locationList;
    }

    public static Object getAllEpisodes(DataSnapshot dataSnapshot, String username, Integer nightCount, int cycle) {
        Object allEpisodesObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle).child("Episodes").getValue();
        return allEpisodesObject;
    }

    public static List<DrinkAnswer> getAllDrinksAnswers(DataSnapshot dataSnapshot, String username, int cycle) {
        DataSnapshot allEpisodesDataSnapshot = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle).child("Episodes");
        List<DrinkAnswer> allDrinkAnswersForEntireCycle = new ArrayList<>();
        for (DataSnapshot episodeDS : allEpisodesDataSnapshot.getChildren()) {
            DataSnapshot allAnswersDS = episodeDS.child("Answers");
            for (DataSnapshot singleDrinkAnswerDS : allAnswersDS.getChildren()) {
                DrinkAnswer drinkAnswer = new DrinkAnswer();
                drinkAnswer.setCost(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getCost());
                drinkAnswer.setDrinksPlanned(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getDrinksPlanned());
                drinkAnswer.setSize(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getSize());
                drinkAnswer.setType(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getType());
                drinkAnswer.setWhere(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getWhere());
                drinkAnswer.setWho(singleDrinkAnswerDS.getValue(DrinkAnswer.class).getWho());

                allDrinkAnswersForEntireCycle.add(drinkAnswer);
            }
        }

        return allDrinkAnswersForEntireCycle;
    }
}
