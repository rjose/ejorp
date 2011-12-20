package com.ejorp.parser;

import com.ejorp.task.Task;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 12/18/11
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateParserTest {
    public static class StaticProvider {
        @DataProvider(name="updated task")
        public static Object[][] getUpdatedTaskData() {
            StringBuilder builder = new StringBuilder();
            builder.append("- 512: Update team tasks\n");
            builder.append("  > @rjose 5h\n");
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

    @Test(dataProvider = "updated task", dataProviderClass = UpdateParserTest.StaticProvider.class)
    public void testParseUpdatedTask(String input) throws RecognitionException {
        ArrayList<Task> tasks = parseTaskFile(input);
        assertEquals(1, tasks.size());
        assertEquals("512", tasks.get(0).getID());
    }
}
