package com.samalex.slucapstone;

import com.samalex.slucapstone.dto.DrinkAnswer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalculationUtil {
    private static final String COST_OPTION_1 = "$01.00";
    private static final String COST_OPTION_2 = "$1.00-$5.00";
    private static final String COST_OPTION_3 = "$6.00-$10.00";
    private static final String COST_OPTION_4 = "$11.00-$15.00";
    private static final String COST_OPTION_5 = "$16.00+";

    private static final double AVG_COST_OPTION_1 = 0;
    private static final double AVG_COST_OPTION_2 = 3.00;
    private static final double AVG_COST_OPTION_3 = 8.00;
    private static final double AVG_COST_OPTION_4 = 13.00;
    private static final double AVG_COST_OPTION_5 = 16.00;

    public static final int BEER_SERVING_SIZE = 12;
    public static final int WINE_SERVING_SIZE = 4;
    public static final int LIQUOR_SERVING_SIZE = 1;
    public static final Map<String, Integer> DRINK_SERVING_SIZE_MAP = new LinkedHashMap<>();
    public static final Integer CALORIES_PER_SERVING = 100;

    static {
        DRINK_SERVING_SIZE_MAP.put("wine", 4);
        DRINK_SERVING_SIZE_MAP.put("beer", 12);
        DRINK_SERVING_SIZE_MAP.put("liquor", 1);
    }

    public static double getAverageCost(Map<String, String> costObject) {
        Map<String, String> costJSON = costObject;

        double avgCost = 0;
        for (Map.Entry<String, String> entry : costJSON.entrySet()) {


            switch (entry.getValue()) {
                case COST_OPTION_1:
                    avgCost += AVG_COST_OPTION_1;
                    break;
                case COST_OPTION_2:
                    avgCost += AVG_COST_OPTION_2;
                    break;
                case COST_OPTION_3:
                    avgCost += AVG_COST_OPTION_3;
                    break;
                case COST_OPTION_4:
                    avgCost += AVG_COST_OPTION_4;
                    break;
                case COST_OPTION_5:
                    avgCost += AVG_COST_OPTION_5;
                    break;
            }
        }
        return avgCost;
    }

    public static Map<String, Float> oldGetDrinkPercentages(Map<String, String> drinkseMap) {
        int numDrinks = drinkseMap.size();
        Integer numWine = 0;
        Integer numLiquor = 0;
        Integer numBeer = 0;

        for (Map.Entry<String, String> entry : drinkseMap.entrySet()) {
            String drinkType = entry.getValue();
            switch (drinkType) {
                case "wine":
                    numWine++;
                    break;
                case "liquor":
                    numLiquor++;
                    break;
                case "beer":
                    numBeer++;
                    break;
            }
        }

        float percentBeer = ((float) numBeer / (float) numDrinks) * 100;
        float percentLiquor = ((float) numLiquor / (float) numDrinks) * 100;
        float percentWine = ((float) numWine / (float) numDrinks) * 100;

        Map<String, Float> percentDrinks = new LinkedHashMap<>();
        percentDrinks.put("beer", percentBeer);
        percentDrinks.put("liquor", percentLiquor);
        percentDrinks.put("wine", percentWine);
        return percentDrinks;
    }

    public static Map<String, Float> oldGetDrinkPercentages(List<String> drinkTypeList) {
        int numDrinks = drinkTypeList.size();
        Integer numWine = 0;
        Integer numLiquor = 0;
        Integer numBeer = 0;

        for (int i = 0; i < drinkTypeList.size(); i++) {
            String drinkType = drinkTypeList.get(i);
            switch (drinkType) {
                case "wine":
                    numWine++;
                    break;
                case "liquor":
                    numLiquor++;
                    break;
                case "beer":
                    numBeer++;
                    break;
            }
        }

        float percentBeer = ((float) numBeer / (float) numDrinks) * 100;
        float percentLiquor = ((float) numLiquor / (float) numDrinks) * 100;
        float percentWine = ((float) numWine / (float) numDrinks) * 100;

        Map<String, Float> percentDrinks = new LinkedHashMap<>();
        percentDrinks.put("beer", percentBeer);
        percentDrinks.put("liquor", percentLiquor);
        percentDrinks.put("wine", percentWine);
        return percentDrinks;
    }

    public static double getAverageCost(List<String> costList) {
        double avgCost = 0;
        for (int i = 0; i < costList.size(); i++) {
            switch (costList.get(i)) {
                case COST_OPTION_1:
                    avgCost += AVG_COST_OPTION_1;
                    break;
                case COST_OPTION_2:
                    avgCost += AVG_COST_OPTION_2;
                    break;
                case COST_OPTION_3:
                    avgCost += AVG_COST_OPTION_3;
                    break;
                case COST_OPTION_4:
                    avgCost += AVG_COST_OPTION_4;
                    break;
                case COST_OPTION_5:
                    avgCost += AVG_COST_OPTION_5;
                    break;
            }
        }
        return avgCost;
    }

    public static Map<String, Float> getDrinkPercentages(List<DrinkAnswer> allDrinkAnswers) {
        int numDrinks = allDrinkAnswers.size();

        Integer numWine = 0;
        Integer numLiquor = 0;
        Integer numBeer = 0;

        for (int i = 0; i < allDrinkAnswers.size(); i++) {
            DrinkAnswer answer = allDrinkAnswers.get(i);

            // TODO: change type from string to enum
            switch (answer.getType()) {
                case "wine":
                    numWine++;
                    break;
                case "liquor":
                    numLiquor++;
                    break;
                case "beer":
                    numBeer++;
                    break;
            }


        }

        float percentBeer = ((float) numBeer / (float) numDrinks) * 100;
        float percentLiquor = ((float) numLiquor / (float) numDrinks) * 100;
        float percentWine = ((float) numWine / (float) numDrinks) * 100;

        Map<String, Float> percentDrinks = new LinkedHashMap<>();
        percentDrinks.put("beer", percentBeer);
        percentDrinks.put("liquor", percentLiquor);
        percentDrinks.put("wine", percentWine);
        return percentDrinks;
    }

    public static LiveReportData getLiveData(List<DrinkAnswer> allDrinkAnswers) {

        int numDrinks = allDrinkAnswers.size();

        int totalNumWine = 0, totalNumLiquor = 0, totalNumBeer = 0;
        int totalOuncesConsumed = 0;
        long totalCaloriesConsumed = 0;
        int totalAverageCost = 0;

        for (int i = 0; i < allDrinkAnswers.size(); i++) {
            DrinkAnswer answer = allDrinkAnswers.get(i);

            int drinkSize = answer.getSize();
            totalOuncesConsumed += drinkSize;

            // for drink type percentage
            // TODO: change type from string to enum
            switch (answer.getType()) {
                case "wine":
                    totalNumWine++;
                    totalCaloriesConsumed += (CalculationUtil.CALORIES_PER_SERVING * (drinkSize / WINE_SERVING_SIZE));
                    break;

                case "liquor":
                    totalNumLiquor++;
                    totalCaloriesConsumed += (CalculationUtil.CALORIES_PER_SERVING * (drinkSize / LIQUOR_SERVING_SIZE));
                    break;

                case "beer":
                    totalNumBeer++;
                    totalCaloriesConsumed += (CalculationUtil.CALORIES_PER_SERVING * (drinkSize / BEER_SERVING_SIZE));
                    break;
            }

            switch (answer.getCost()) {
                case COST_OPTION_1:
                    totalAverageCost += AVG_COST_OPTION_1;
                    break;
                case COST_OPTION_2:
                    totalAverageCost += AVG_COST_OPTION_2;
                    break;
                case COST_OPTION_3:
                    totalAverageCost += AVG_COST_OPTION_3;
                    break;
                case COST_OPTION_4:
                    totalAverageCost += AVG_COST_OPTION_4;
                    break;
                case COST_OPTION_5:
                    totalAverageCost += AVG_COST_OPTION_5;
                    break;
            }
        }

        // calculate percentage of each drink type
        float beerPercentage = ((float) totalNumBeer / (float) numDrinks) * 100;
        float liquorPercentage = ((float) totalNumLiquor / (float) numDrinks) * 100;
        float winePercentage = ((float) totalNumWine / (float) numDrinks) * 100;

        LiveReportData liveReportData = new LiveReportData(
                numDrinks,
                totalOuncesConsumed,
                totalCaloriesConsumed,
                totalAverageCost,
                beerPercentage,
                winePercentage,
                liquorPercentage);

        return liveReportData;
    }
}
