package com.samalex.slucapstone;

import com.google.firebase.database.DataSnapshot;
import com.samalex.slucapstone.dto.DrinkAnswer;

import java.util.ArrayList;
import java.util.List;

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

    public static Object getLocations(DataSnapshot dataSnapshot, String username, Integer nightCount, int cycle) {
        Object locationObject = dataSnapshot.child("UID: " + username).child("Cycle: " + cycle)
                .child("Episodes").child(nightCount + "").child("Location").getValue();
        return locationObject;
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
            for (DataSnapshot singleDrinkAnswerDS: allAnswersDS.getChildren()) {
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
