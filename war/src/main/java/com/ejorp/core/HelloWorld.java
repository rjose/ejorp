/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.core;

import com.google.gson.Gson;
import ejorp_clj.core.Sample;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * REST Web Service
 *
 * @author rjose
 */
@Path("helloworld")
public class HelloWorld {
    private Logger logger = Logger.getLogger("ejorp-spike");

    @Context
    private UriInfo context;

    /** Creates a new instance of HelloWorld */
    public HelloWorld() {
    }

    /**
     * Retrieves representation of an instance of com.ejorp.helloworld.HelloWorld
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getHtml() throws NamingException, SQLException {
        // NOTE: Just a hack to get database tested
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/ejorp");
        Connection conn = ds.getConnection();

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
        while(resultSet.next()) {
            logger.info("-----> " + resultSet.getString(1));
        }
        resultSet.close();
        statement.close();

        Gson gson = new Gson();
        double[] coeffs = {Sample.binomial(5, 3)};
        return gson.toJson(coeffs);
    }

    /**
     * PUT method for updating or creating an instance of HelloWorld
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}
