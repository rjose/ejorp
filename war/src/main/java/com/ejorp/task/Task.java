package com.ejorp.task;

// TODO: Figure out how to document this

import java.util.ArrayList;
import java.util.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/17/11
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Task {
    private String title;
    private ArrayList<Double> effortLeft = new ArrayList<Double>();
    private static final Pattern estimatePattern = Pattern.compile("(\\d+\\.?\\d*)\\s*h");
    private static final Pattern rangeEstimatePattern = Pattern.compile("(\\d+\\.?\\d*)\\s*\\-\\s*(\\d+\\.?\\d*)\\s*h");
    // TODO: We may remove this when we figure out how to handle assignees
    private String assigneeHandle;
    private ArrayList<String> notes;

    public static String extractTitle(String title) {
        String[] strings = title.split("\\-\\s+", 2);
        String result = title;
        if (strings.length == 2) {
            result = strings[1].trim();
        }
        return result;
    }

    public static String extractNote(String note) {
        String[] strings = note.split("\\*\\s+", 2);
        String result = note;
        if (strings.length == 2) {
            result = strings[1].trim();
        }
        return result;
    }


    public static ArrayList<Double> parseEstimate(String estimateString) {
        ArrayList<Double> result = new ArrayList<Double>(2);

        Matcher estimateMatcher = estimatePattern.matcher(estimateString);
        Matcher rangeEstimateMatcher = rangeEstimatePattern.matcher(estimateString);

        // Check for matches like "5h"
        if (estimateMatcher.matches()) {
            Double estimate = Double.parseDouble(estimateMatcher.group(1));
            result.add(estimate);
            result.add(estimate);
        }
        // Check for matches like "3-5h"
        else if (rangeEstimateMatcher.matches()) {
            result.add(Double.parseDouble(rangeEstimateMatcher.group(1)));
            result.add(Double.parseDouble(rangeEstimateMatcher.group(2)));
        }

        return result;
    }

    public Task(String title) {
        this.title = title;
    }

    public String getAssigneeHandle() {
        return assigneeHandle;
    }

    public void setAssigneeHandle(String assigneeHandle) {
        this.assigneeHandle = assigneeHandle;
    }

    public void setEstimate(String estimate) {
        effortLeft = Task.parseEstimate(estimate);
    }

    public ArrayList<Double> getEffortLeft() {
        return effortLeft;
    }

    public void addNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
