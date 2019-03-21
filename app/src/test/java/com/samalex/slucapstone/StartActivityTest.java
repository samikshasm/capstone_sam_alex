package com.samalex.slucapstone;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StartActivityTest {
    @Test
    public void testCalculateUserStartTimeWhenUserLoginBeforeMidnight() throws Exception {
        StartActivity startActivity = new StartActivity();

        // mock current date time to 03/21/2019 7:00:00.000 PM (login time)
        Calendar mockCurrentDateTimeCalendar = Calendar.getInstance();
        mockCurrentDateTimeCalendar.set(Calendar.YEAR, 2019);
        mockCurrentDateTimeCalendar.set(Calendar.MONTH, 2); // March
        mockCurrentDateTimeCalendar.set(Calendar.DAY_OF_MONTH, 21);
        mockCurrentDateTimeCalendar.set(Calendar.HOUR_OF_DAY, 19); // 7pm
        mockCurrentDateTimeCalendar.set(Calendar.MINUTE, 0);
        mockCurrentDateTimeCalendar.set(Calendar.SECOND, 0);
        mockCurrentDateTimeCalendar.set(Calendar.MILLISECOND, 0);

        long mockCycleLength = 24 * 60 * 60 * 1000; // 1 day
        long mockCycleOffset = 5 * 60 * 60 * 1000; // 5 hours -> cycle starts at 5am

        // expect start time to be 03/21/2019 5:00:00.000 AM
        Calendar expected = (Calendar) mockCurrentDateTimeCalendar.clone();
        expected.set(Calendar.HOUR_OF_DAY, 5);

        long actualMillis = startActivity.calculateUserStartTime(mockCurrentDateTimeCalendar, mockCycleLength, mockCycleOffset);

        assertEquals(expected.getTimeInMillis(), actualMillis);
    }

    @Test // when the user login time is between midnight and CYCLE_OFFSET
    public void testCalculateUserStartTimeWhenUserLoginBetweenMidnightAndCycleOffset() throws Exception {
        StartActivity startActivity = new StartActivity();

        // mock current date time to 03/21/2019 3:00:00.000 AM (login time)
        Calendar mockCurrentDateTimeCalendar = Calendar.getInstance();
        mockCurrentDateTimeCalendar.set(Calendar.YEAR, 2019);
        mockCurrentDateTimeCalendar.set(Calendar.MONTH, 2); // March
        mockCurrentDateTimeCalendar.set(Calendar.DAY_OF_MONTH, 21);
        mockCurrentDateTimeCalendar.set(Calendar.HOUR_OF_DAY, 3); // 3am
        mockCurrentDateTimeCalendar.set(Calendar.MINUTE, 0);
        mockCurrentDateTimeCalendar.set(Calendar.SECOND, 0);
        mockCurrentDateTimeCalendar.set(Calendar.MILLISECOND, 0);


        long mockCycleLength = 24 * 60 * 60 * 1000; // 1 day
        long mockCycleOffset = 5 * 60 * 60 * 1000; // 5 hours -> cycle starts at 5am

        // expect start time to be 03/20/2019 5:00:00.000 AM
        Calendar expected = (Calendar) mockCurrentDateTimeCalendar.clone();
        expected.set(Calendar.DAY_OF_MONTH, 20);
        expected.set(Calendar.HOUR_OF_DAY, 5);

        long actualMillis = startActivity.calculateUserStartTime(mockCurrentDateTimeCalendar, mockCycleLength, mockCycleOffset);

        assertEquals(expected.getTimeInMillis(), actualMillis);
    }
}