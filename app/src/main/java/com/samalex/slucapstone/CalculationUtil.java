package com.samalex.slucapstone;

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

    public static double getAverageCost(Map<String, String> costObject) {
        Map<String, String> costJSON = costObject;

        double newAvgCost = 0;
        for (Map.Entry<String, String> entry : costJSON.entrySet()) {


            switch (entry.getValue()) {
                case COST_OPTION_1:
                    newAvgCost += AVG_COST_OPTION_1;
                    break;
                case COST_OPTION_2:
                    newAvgCost += AVG_COST_OPTION_2;
                    break;
                case COST_OPTION_3:
                    newAvgCost += AVG_COST_OPTION_3;
                    break;
                case COST_OPTION_4:
                    newAvgCost += AVG_COST_OPTION_4;
                    break;
                case COST_OPTION_5:
                    newAvgCost += AVG_COST_OPTION_5;
                    break;
            }
        }
        return newAvgCost;
    }
}
