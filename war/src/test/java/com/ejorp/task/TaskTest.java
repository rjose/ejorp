package com.ejorp.task;

import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.*;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/18/11
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskTest {
    @Test
    public void testExtractTitle() {
        ArrayList<String> titleInfo = Task.extractTitle("- Test 1\n");
        assertEquals(1, titleInfo.size());
        assertEquals("Test 1", titleInfo.get(0));

        // Try a title with an ID
        titleInfo = Task.extractTitle("- 512: Test 2\n");
        assertEquals(2, titleInfo.size());
        assertEquals("Test 2", titleInfo.get(0));
        assertEquals("512", titleInfo.get(1));
    }

    @Test
    public void testExtractComment() {
        assertEquals("Comment 1", Task.extractNote("* Comment 1\n"));
    }

    @Test
    public void testParseEstimate() {
        ArrayList<Double> estimate;

        estimate = Task.parseEstimate("5h");
        assertEquals(5.0, estimate.get(0), 0.1);
        assertEquals(5.0, estimate.get(1), 0.1);

        estimate = Task.parseEstimate("2.5 h");
        assertEquals(2.5, estimate.get(0), 0.1);
        assertEquals(2.5, estimate.get(1), 0.1);


        estimate = Task.parseEstimate("3-5 h");
        assertEquals(3.0, estimate.get(0), 0.1);
        assertEquals(5.0, estimate.get(1), 0.1);
    }

    @Test
    public void testParseGarbageEstimate() {
        ArrayList<Double> estimate;
        estimate = Task.parseEstimate("garbage");
        assertEquals(0, estimate.size());
    }

    @Test
    public void testCreateTask() {
        ArrayList<String> taskInfo = new ArrayList<String>();
        taskInfo.add("My title");
        taskInfo.add("101");

        Task task = new Task(taskInfo);
        assertEquals("My title", task.getTitle());
        assertEquals("101", task.getID());
    }

    // TODO: Test parsing garbage
}
