package com.samalex.slucapstone;

public class LiveReportData {
    private int numDrinks = 0;
    private int ouncesConsumed = 0;
    private long caloriesConsumed = 0;
    private float averageCost = 0;
    private float beerPercentage = 0;
    private float winePercentage = 0;
    private float liquorPercentage = 0;

    public LiveReportData() {
    }

    public LiveReportData(int numDrinks, int ouncesConsumed, long caloriesConsumed, float averageCost, float beerPercentage, float winePercentage, float liquorPercentage) {
        this.numDrinks = numDrinks;
        this.ouncesConsumed = ouncesConsumed;
        this.caloriesConsumed = caloriesConsumed;
        this.averageCost = averageCost;
        this.beerPercentage = beerPercentage;
        this.winePercentage = winePercentage;
        this.liquorPercentage = liquorPercentage;
    }

    public void setNumDrinks(int numDrinks) {
        this.numDrinks = numDrinks;
    }

    public void setOuncesConsumed(int ouncesConsumed) {
        this.ouncesConsumed = ouncesConsumed;
    }

    public void setCaloriesConsumed(long caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public void setAverageCost(float averageCost) {
        this.averageCost = averageCost;
    }

    public void setBeerPercentage(float beerPercentage) {
        this.beerPercentage = beerPercentage;
    }

    public void setWinePercentage(float winePercentage) {
        this.winePercentage = winePercentage;
    }

    public void setLiquorPercentage(float liquorPercentage) {
        this.liquorPercentage = liquorPercentage;
    }

    public int getNumDrinks() {
        return numDrinks;
    }

    public int getOuncesConsumed() {
        return ouncesConsumed;
    }

    public long getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public float getAverageCost() {
        return averageCost;
    }

    public double getLitersConsumed() {
        return ouncesConsumed * 0.03;
    }

    public float getBeerPercentage() {
        return beerPercentage;
    }

    public float getWinePercentage() {
        return winePercentage;
    }

    public float getLiquorPercentage() {
        return liquorPercentage;
    }
}
