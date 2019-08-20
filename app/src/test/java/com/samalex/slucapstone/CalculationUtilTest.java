package com.samalex.slucapstone;

import com.samalex.slucapstone.dto.DrinkAnswer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CalculationUtilTest {
    @Test
    public void testGetLiveDataWine4oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("wine");
        ans1.setSize(4);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);
        Assert.assertEquals(100, liveData.getCaloriesConsumed(), 0);
    }
    @Test
    public void testGetLiveDataWine5oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("wine");
        ans1.setSize(5);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);
        Assert.assertEquals(125, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataWine6oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("wine");
        ans1.setSize(6);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(150, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataBeer12oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("beer");
        ans1.setSize(12);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(100, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataBeer13oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("beer");
        ans1.setSize(13);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(108, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataBeer14oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("beer");
        ans1.setSize(14);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(116, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataBeer15oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("beer");
        ans1.setSize(15);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(125, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataBeer16oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("beer");
        ans1.setSize(16);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(133, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataLiquor1oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("liquor");
        ans1.setSize(1);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(100, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataLiquor2oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("liquor");
        ans1.setSize(2);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(200, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataLiquor3oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("liquor");
        ans1.setSize(3);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(300, liveData.getCaloriesConsumed());
    }
    @Test
    public void testGetLiveDataLiquor4oz(){
        List<DrinkAnswer> answers = new ArrayList<>();
        DrinkAnswer ans1 = new DrinkAnswer();
        ans1.setType("liquor");
        ans1.setSize(4);
        ans1.setCost("$0.00");
        answers.add(ans1);
        LiveReportData liveData = CalculationUtil.getLiveData(answers);

        Assert.assertEquals(400, liveData.getCaloriesConsumed());
    }
}
