/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.core;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST Web Service
 *
 * @author simonjam
 */
@Path("list_tasks")
public class ListTasksHandler {
    private Logger logger = Logger.getLogger("ejorp-spike");

    @Context
    private UriInfo context;

    /** Creates a new instance of ListTasksHandler*/
    public ListTasksHandler() {
    }

    class Task {
      public Task(String description) {
        this.description = description;
      }

      private String description;
    }
    /**
     * Retrieves representation of an instance of com.ejorp.helloworld.HelloWorld
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getHtml() throws IOException {
        Gson gson = new Gson();
        Task t1 = new Task("Task #1");
        Task[] tasks = new Task[1];
        tasks[0] = t1;
        return gson.toJson(tasks);
    }
}
