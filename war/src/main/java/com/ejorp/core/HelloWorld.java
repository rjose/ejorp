/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.core;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
    public String getHtml() throws NamingException, SQLException, IOException {
        // Just a couple of tests
        lookUpTaskViaJPA();
        lookUpTasksViaJDBC();

        // Try making an http request to couch
        URL url = new URL("http://localhost:5984/hello-world");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            StringWriter writer = new StringWriter();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            IOUtils.copy(in, writer, "UTF-8");
            logger.info("-----> From couchdb: " + writer.toString());
        }
        finally {
            urlConnection.disconnect();
        }

        Gson gson = new Gson();
        double[] coeffs = {0.0, 10.0};
        return gson.toJson(coeffs);
    }

    private void lookUpTasksViaJDBC() throws NamingException, SQLException {
        // NOTE: Just a hack to get database tested
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/ejorp");
        Connection conn = ds.getConnection();

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
        while(resultSet.next()) {
            logger.info("-----> (JDBC)" + resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
    }

    private void lookUpTaskViaJPA() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejorpPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        List<SampleTask> tasks = em.createNamedQuery("findAllTasks").getResultList();
        for(SampleTask t: tasks) {
            logger.info("-----> SampleTask (from JPA): " + t.getName());
        }
        em.close();
        emf.close();
    }

    /**
     * PUT method for updating or creating an instance of HelloWorld
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) throws IOException {
        logger.info("=====> Putting to HelloWorld");
        // TODO: Figure out how to check the response
        URL url = new URL("http://localhost:5984/hello-world/6789");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write("{\"title\": \"Ouch\", \"_rev\": \"2-1fc6347f9d61de367b724bb22baf0c81\"}");
        out.close();

        StringWriter writer = new StringWriter();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        IOUtils.copy(in, writer, "UTF-8");
        logger.info("-----> From couchdb: " + writer.toString());
    }
}
