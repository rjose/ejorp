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
        assertEquals("Test 1", Task.extractTitle("- Test 1\n"));
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

    // TODO: Test parsing garbage
}
