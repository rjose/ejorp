package com.ejorp.core;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 11/12/11
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class HelloWorldTest {
    public static class StaticProvider {
        @DataProvider(name="create")
        public static Object[][] createData() {
            return new Object[][] {new Object[] {new HelloWorld()}};
        }
    }

    @Test
    public void testGetHtml() throws Exception {
        assert 1 == 1;
    }

    @Test(dataProvider = "create", dataProviderClass = HelloWorldTest.StaticProvider.class)
    public void test(HelloWorld app) {
        String result = app.getHtml();
        Assert.assertEquals(result, "[10.0]");
    }
}
