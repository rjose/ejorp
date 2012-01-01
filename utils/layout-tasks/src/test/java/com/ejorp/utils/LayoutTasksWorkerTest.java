package com.ejorp.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.*;


public class LayoutTasksWorkerTest {
    public static class TestDataProvider {
        @DataProvider(name="week data, one task")
        public static Object[][] weekData() {
            StringBuilder builder = new StringBuilder();
            builder.append("Jan 22, 2011:\n");
            builder.append("\t- Create ejorp engine code for generating and storing auth token: 4P\n");
            return new Object[][] {{builder.toString()}};
        }

        @DataProvider(name="week data, too many tasks")
        public static Object[][] weekDataTooManyTasks() {
            StringBuilder builder = new StringBuilder();
            builder.append("Jan 22, 2011:\n");
            builder.append("\t- Create ejorp engine code for generating and storing auth token: 14P\n");
            builder.append("\t- Too big: 18P\n");
            return new Object[][] {{builder.toString()}};
        }

        @DataProvider(name="week data, with done tasks")
        public static Object[][] weekDataWithDoneTasks() {
            StringBuilder builder = new StringBuilder();
            builder.append("Jan 22, 2011:\n");
            builder.append("\t- Task 1: 14P @done\n");
            builder.append("\t- Task 2: 18P @done\n");
            return new Object[][] {{builder.toString()}};
        }


        @DataProvider(name="week data, with tasks")
        public static Object[][] weekDataWithTasks() {
            StringBuilder builder = new StringBuilder();
            builder.append("Jan 22, 2011:\n");
            builder.append("\t- Task 1: 4P\n");
            builder.append("\t- Task 2: 8P\n");
            return new Object[][] {{builder.toString()}};
        }
        // TODO: Add data without an estimate
    }

    public static LayoutTasksWorker getWorker() {
        LayoutTasksWorker worker = new LayoutTasksWorker();

        // Set up current date
        DateTime curDate = new DateTime(2012, 1, 20, 9, 30);
        worker.setCurrentDate(curDate);
        return worker;
    }

    @Test
    public void testKnowsCurrentWeek() {
        LayoutTasksWorker worker = getWorker();

        // Set up current date
        DateTime curDate = new DateTime(2011, 12, 30, 9, 30);
        worker.setCurrentDate(curDate);

        DateTime curSunday = curDate.withDayOfWeek(DateTimeConstants.SUNDAY);
        assertEquals(curSunday.toString(), worker.currentSunday().toString());

        assertEquals("Jan 1, 2012", worker.currentWeekHeading());

        // Try a week heading with a two digit date
        worker.setCurrentDate(new DateTime(2012, 1, 21, 9, 30));
        assertEquals("Jan 22, 2012", worker.currentWeekHeading());
    }


    @Test(dataProvider = "week data, one task", dataProviderClass = TestDataProvider.class)
    public void testScheduleOneWeek(String input) {
        LayoutTasksWorker worker = getWorker().processInput(input);
        worker.scheduleTasks();
        ArrayList<Task> tasks = worker.getWeeks().get(0).getTasks();
        assertEquals(1, tasks.size());
        assertEquals("\t- Create ejorp engine code for generating and storing auth token: 4P", tasks.get(0).toString());
    }

    @Test(dataProvider = "week data, with tasks", dataProviderClass = TestDataProvider.class)
    public void testScheduleOneWeekWithTasks(String input) {
        LayoutTasksWorker worker = getWorker().processInput(input);
        worker.scheduleTasks();
        ArrayList<Task> tasks = worker.getWeeks().get(0).getTasks();
        assertEquals(2, tasks.size());
    }

    // TODO: Test effort left
    // TODO: Test complete tasks


    @Test(dataProvider = "week data, too many tasks", dataProviderClass = TestDataProvider.class)
    public void testScheduleTooManyTasks(String input) {
        LayoutTasksWorker worker = getWorker().processInput(input);
        worker.scheduleTasks();
        assertEquals(2, worker.getWeeks().size());

        // Check output
        StringBuilder builder = new StringBuilder();
        builder.append("Jan 29, 2012:\n");
        builder.append("\t- Create ejorp engine code for generating and storing auth token: 14P\n");
        builder.append("\n");

        builder.append("Jan 22, 2012:\n");
        builder.append("\t- Too big: 18P\n");
        builder.append("\n");

        assertEquals(builder.toString(), worker.getPrintableSchedule());
    }
    
    @Test(dataProvider = "week data, with done tasks", dataProviderClass = LayoutTasksWorkerTest.TestDataProvider.class)
    public void testHandleDoneTasks(String input) {
        LayoutTasksWorker worker = getWorker().processInput(input);
        worker.scheduleTasks();
        assertEquals(1, worker.getWeeks().size());
    }


}