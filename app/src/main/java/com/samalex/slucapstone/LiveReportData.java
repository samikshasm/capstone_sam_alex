package com.samalex.slucapstone;

import java.util.LinkedHashMap;
import java.util.Map;

public class LiveReportData {
    private int numDrinks = 0;
    private int ouncesConsumed = 0;
    private int caloriesConsumed = 0;
    private double averageCost = 0;
    private Map<String, Float> drinkTypePercentage = new LinkedHashMap<>();

    public Map<String, Float> getDrinkTypePercentage() {
        return drinkTypePercentage;
    }

    public void setDrinkTypePercentage(Map<String, Float> drinkTypePercentage) {
        this.drinkTypePercentage = drinkTypePercentage;
    }

    public void setNumDrinks(int numDrinks) {
        this.numDrinks = numDrinks;
    }

    public void setOuncesConsumed(int ouncesConsumed) {
        this.ouncesConsumed = ouncesConsumed;
    }

    public void setCaloriesConsumed(int caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public int getNumDrinks() {
        return numDrinks;
    }

    public int getOuncesConsumed() {
        return ouncesConsumed;
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public double getLitersConsumed(){
        return ouncesConsumed * 0.03;
    }
}
