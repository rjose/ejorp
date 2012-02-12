/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.handlers;

import com.ejorp.core.EjorpSingleton;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Path("list_tasks/{userId}")
@Stateless
public class ListTasksHandler {
    @Context
    private UriInfo context;

    @EJB
    private EjorpSingleton ejorp;

    @GET
    @Produces("application/json")
    public String getHtml(@PathParam("userId") String userId) throws IOException {
        Set<String> results = new HashSet<String>();
        JedisPool pool = ejorp.getJedisPool();
        Jedis jedis = pool.getResource();
        try {
            Set<String> taskIds = jedis.smembers("user:" + userId + ":tasks");
            for (String taskId : taskIds) {
                results.add(jedis.get("task:" + taskId + ":description"));
            }
        } finally {
            pool.returnResource(jedis);
        }
        Gson gson = new Gson();
        return gson.toJson(results);
    }
}
