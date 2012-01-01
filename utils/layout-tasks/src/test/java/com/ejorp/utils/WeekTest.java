package com.ejorp.utils;

import org.joda.time.DateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/31/11
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeekTest {
    public static class TestDataProvider {
        @DataProvider(name="week with tasks")
        public static Object[][] weekWithTasks() {
            Week week = new Week("Dec 11, 2011");
            Task task = new Task("\t- A new task: 4P");
            week.addTask(task);
            return new Object[][] {{week}};
        }
    }

    @Test
    public void testKnowsEndDate() {
        Week week = new Week("Dec 11, 2011");
        DateTime expectedEndDate = new DateTime(2011, 12, 11, 9, 30);
        DateTime endDate = week.endDate();
        assertEquals(expectedEndDate.year(), endDate.year());
        assertEquals(expectedEndDate.monthOfYear(), endDate.monthOfYear());
        assertEquals(expectedEndDate.dayOfMonth(), endDate.dayOfMonth());
    }
    
    @Test
    public void testKnowsNextWeek() {
        Week week = new Week("Dec 11, 2011");
        Week nextWeek = week.nextWeek();
        assertEquals("Dec 18, 2011", nextWeek.getTitle());
    }

    @Test
    public void testToString() {
        Week week = new Week("Dec 11, 2011");
        assertEquals("Dec 11, 2011:\n", week.toString());
    }

    @Test(dataProvider = "week with tasks", dataProviderClass = TestDataProvider.class)
    public void testToStringWithTasks(Week week) {
        StringBuilder builder = new StringBuilder();
        builder.append("Dec 11, 2011:\n");
        builder.append("\t- A new task: 4P\n");
        assertEquals(builder.toString(), week.toString());
    }

    @Test
    public void testKnowsFractionOfWeek() {
        Week week = new Week("Dec 11, 2011");
        DateTime dec11 = new DateTime(2011, 12, 11, 9, 30);
        DateTime dec7 = new DateTime(2011, 12, 7, 9, 30);

        week.setCurrentDate(dec11);
        assertEquals(0.0, week.fractionOfWeekLeft(), 0.1);

        week.setCurrentDate(dec7);
        assertEquals(4.0/7.0, week.fractionOfWeekLeft(), 0.1);
    }
}
