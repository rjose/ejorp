package com.ejorp.models;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/30/11
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Week {
    public static final Integer MAX_WEEKLY_POMODOROS = 20;    
    private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MMM dd, yyyy");
    private DateTime endDate;

    private DateTime currentDate; // Used for testing

    private String title;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    Integer numPomodoros = 0;


    public Week() {
        DateTime today = getCurrentDate();
        this.title = dateFormat.print(today);
        endDate = buildEndDate(title);
    }
    
    public Week(DateTime currentDate) {
        endDate = buildEndDate(dateFormat.print(currentDate));
        title = dateFormat.print(endDate);
    }


    public Week(String title) {
        this.title = title;
        endDate = buildEndDate(title);
    }
    
    public Week nextWeek() {
        DateTime nextWeekEndDate = endDate.plus(Weeks.ONE);
        String nextWeekTitle = dateFormat.print(nextWeekEndDate);
        Week result = new Week(nextWeekTitle);
        return result;
    }

    public String getTitle() {
        return title;
    }
    
    public DateTime endDate() {
        return endDate;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(title + ":\n");
        
        ListIterator<Task> taskListIterator = tasks.listIterator(tasks.size());
        while (taskListIterator.hasPrevious()) {
            Task task = taskListIterator.previous();
            builder.append(task.toString() + "\n");
        }

        return builder.toString();
    }

    // Title looks like "Dec 11, 2011"
    private DateTime buildEndDate(String title) {
        DateTime date = dateFormat.parseDateTime(title);
        DateTime result = date.withDayOfWeek(DateTimeConstants.SUNDAY);
        return result;
    }


    public ArrayList<Task> getTasks() {
        return tasks;
    }
    
    public void addTask(Task task) {
        tasks.add(0, task);
        numPomodoros += task.getEffortLeft();
    }

    public Double fractionOfWeekLeft() {
        DateTime today = getCurrentDate();
        if (endDate.isBefore(today)) {
            return 0.0;
        }
        Duration duration = new Duration(today, endDate);
        Double result = (1.0 + duration.getStandardDays())/7.0;
        if (result > 1.0) {
            result = 1.0;
        }
        return result;
    }

    public DateTime getCurrentDate() {
        if (currentDate == null) {
            return new DateTime();
        }
        return currentDate;
    }

    public void setCurrentDate(DateTime date) {
        currentDate = date;
    }

    public Double pomodorosLeft() {
        return MAX_WEEKLY_POMODOROS * fractionOfWeekLeft() - numPomodoros;
    }

    public void fillTasks(ListIterator<Task> taskIterator) {
        // Iterate over incomplete tasks and bucket them into weeks
        while(taskIterator.hasPrevious()) {
            Task task = taskIterator.previous();

            // Always add the first task
            if (numPomodoros == 0) {
                addTask(task);
                continue;
            }

            if (task.getEffortLeft() <= pomodorosLeft()) {
                addTask(task);
            }
            else {
                // Put the task "back"
                taskIterator.next();
                break;
            }
        }

    }
}
