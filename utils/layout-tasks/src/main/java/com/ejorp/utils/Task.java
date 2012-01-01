package com.ejorp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/30/11
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Task {
    String rawLine;
    Integer pomodoroEstimate;
    public static final Integer DEFAULT_ESTIMATE = 2;
    private static Pattern estimatePattern = Pattern.compile(".*?(\\d+)P");

    
    public Task(String line) {
        rawLine = line;
        Matcher estimateMatcher = estimatePattern.matcher(line);
        if (estimateMatcher.matches()) {
            pomodoroEstimate = Integer.parseInt(estimateMatcher.group(1));
        }
        else {
            pomodoroEstimate = DEFAULT_ESTIMATE;
        }
    }
    
    public String toString() {
        return rawLine;
    }
    
    // Returns estimated number of pomodoros
    public Integer getEstimate() {
        return pomodoroEstimate;
    }
    
    public Integer getEffortLeft() {
        // TODO: Subtract off effort spent
        return pomodoroEstimate;
    }
}
