package com.ejorp.parser;

import com.ejorp.task.Task;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/17/11
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateParserTest {
    public static class StaticProvider {
        @DataProvider(name="task title")
        public static Object[][] createData() {
            return new Object[][] {{"- SampleTask 1\n"}};
        }

        @DataProvider(name="task with data")
        public static Object[][] getTaskStringWithData() {
            StringBuilder builder = new StringBuilder();
            builder.append("- SampleTask 2\n");
            builder.append(" > @rjose 5h\n");
            return new Object[][] {{builder.toString()}};
        }

        @DataProvider(name="task with comments")
        public static Object[][] getTaskStringWithComments() {
            StringBuilder builder = new StringBuilder();
            builder.append("- SampleTask 3\n");
            builder.append(" * Comment 1\n");
            builder.append(" * Comment 2\n");
            return new Object[][] {{builder.toString()}};
        }

        @DataProvider(name="two tasks")
        public static Object[][] getTwoTasks() {
            StringBuilder builder = new StringBuilder();
            builder.append("- SampleTask 4\n");
            builder.append(" > @rjose 5-9h\n");
            builder.append("\n");
            builder.append("- SampleTask 5\n");
            builder.append(" * Comment 1\n");
            return new Object[][] {{builder.toString()}};
        }

    }

    private static ArrayList<Task> parseTaskFile(String fileContent) throws RecognitionException {
        ANTLRStringStream stringStream = new ANTLRStringStream(fileContent);
        CreateTaskLexer lexer = new CreateTaskLexer(stringStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CreateTaskParser parser = new CreateTaskParser(tokens);
        ArrayList<Task> result=  parser.tasklist();
        return result;
    }

    @Test(dataProvider = "task title", dataProviderClass = CreateParserTest.StaticProvider.class)
    public void testParseTaskTitle(String input) throws RecognitionException {
        ArrayList<Task> tasks = parseTaskFile(input);
        assertEquals(1, tasks.size());
        assertEquals("SampleTask 1", tasks.get(0).getTitle());
    }

    @Test(dataProvider = "task with data", dataProviderClass = CreateParserTest.StaticProvider.class)
    public void testParseTaskData(String input) throws RecognitionException {
        ArrayList<Task> tasks = parseTaskFile(input);
        Task task = tasks.get(0);
        assertEquals(1, tasks.size());
        assertEquals("SampleTask 2", task.getTitle());
        assertEquals("@rjose", task.getAssigneeHandle());

        ArrayList<Double> effortLeft = task.getEffortLeft();
        assertEquals(5.0, effortLeft.get(0), 0.1);
        assertEquals(5.0, effortLeft.get(1), 0.1);
    }

    @Test(dataProvider = "task with comments", dataProviderClass = CreateParserTest.StaticProvider.class)
    public void testParseTaskComments(String input) throws RecognitionException {
        ArrayList<Task> tasks = parseTaskFile(input);
        Task task = tasks.get(0);
        assertEquals(1, tasks.size());
        assertEquals("SampleTask 3", task.getTitle());

        ArrayList<String> notes= task.getNotes();
        assertEquals(2, notes.size());
        assertEquals("Comment 1", notes.get(0));
        assertEquals("Comment 2", notes.get(1));
    }

    @Test(dataProvider = "two tasks", dataProviderClass = CreateParserTest.StaticProvider.class)
    public void testParseTwoTasks(String input) throws RecognitionException {
        ArrayList<Task> tasks = parseTaskFile(input);
        assertEquals(2, tasks.size());
    }

}
