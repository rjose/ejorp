package com.ejorp.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/29/11
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayoutTasksWorker {
    private DateTime currentDate;
    private static DateTimeFormatter weekHeadingFmt = DateTimeFormat.forPattern("MMM d, yyyy");

    private static Pattern taskPattern = Pattern.compile("\t\\-.+");
    private static Pattern doneTaskPattern = Pattern.compile("\t\\-.+@done.*");

    private ArrayList<Task> incompleteTasks = new ArrayList<Task>();
    private ArrayList<Week> weeks = new ArrayList<Week>();


    public void processLine(String line) {
        Matcher taskMatcher = taskPattern.matcher(line);
        Matcher doneTaskMatcher = doneTaskPattern.matcher(line);

        if (doneTaskMatcher.matches()) {
            return;
        }

        if (taskMatcher.matches()) {
            Task task = new Task(line);
            incompleteTasks.add(task);
        }
    }

    public void outputSchedule(PrintStream output) {
        scheduleTasks();
        output.print(getPrintableSchedule());
    }
    
    public void scheduleTasks() {
        ListIterator<Task> taskIterator = incompleteTasks.listIterator(incompleteTasks.size());

        // Start with the current week
        Week week = new Week(currentSunday());
        do {
            week.fillTasks(taskIterator);
            weeks.add(week);
            week = week.nextWeek();
        } while(taskIterator.hasPrevious());

    }

    public String getPrintableSchedule() {
        StringBuilder builder = new StringBuilder();

        ListIterator<Week> weekListIterator = weeks.listIterator(weeks.size());

        while (weekListIterator.hasPrevious()) {
            Week w = weekListIterator.previous();
            builder.append(w.toString());
            builder.append("\n");            
        }
        return builder.toString();
    }
    
    public LayoutTasksWorker processInput(String input) {
        String[] lines = input.split("\n");
        for (String l: lines) {
            processLine(l);
        }
        return this;
    }

    public ArrayList<Week> getWeeks() {
        return weeks;
    }

    public static void main(String[] args) {
        LayoutTasksWorker worker = new LayoutTasksWorker();

        System.out.println("LayoutTasksWorker!");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while ((line = in.readLine()) != null) {
                worker.processLine(line);
            }
        } catch (IOException e) {
            System.out.println("Trouble reading from stdin");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        worker.outputSchedule(System.out);
    }

    public void setCurrentDate(DateTime date) {
        currentDate = date;
    }

    public DateTime currentSunday() {
        if (currentDate == null) {
            currentDate = new DateTime();
        }
        return currentDate.withDayOfWeek(DateTimeConstants.SUNDAY);
    }

    public String currentWeekHeading() {
        return weekHeadingFmt.print(currentSunday());
    }
}
