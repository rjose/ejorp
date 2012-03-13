/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.controllers;

import com.ejorp.core.EjorpSingleton;
import com.ejorp.models.Objective;
import com.ejorp.models.Person;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Path("list_tasks/{personId}")
@Stateless
public class ListTasksController {
    @Context
    private UriInfo context;

    @EJB
    private EjorpSingleton ejorp;

    private class Output {
        public class Objective {
            public Long id;
            public String description;
        }
        public Long id;
        public String name;
        public String email;
        public Collection<Objective> objectives;
    }

    @GET
    @Produces("application/json")
    public String getPerson(@PathParam("personId") String personId) throws IOException {
        Output output = new Output();
        JedisPool pool = ejorp.getJedisPool();
        Jedis jedis = pool.getResource();
        try {
            Person person = Person.get(jedis, Long.decode(personId));
            output.id = person.getId();
            output.name = person.getName();
            output.email = person.getEmail();
            output.objectives = new ArrayList<Output.Objective>();
            for (Long objectiveId : person.getObjectives()) {
                Objective objective = Objective.get(jedis, objectiveId);
                Output.Objective outputObjective = output.new Objective();
                outputObjective.id = objective.getId();
                outputObjective.description = objective.getDescription();
                output.objectives.add(outputObjective);
            }
        } finally {
            pool.returnResource(jedis);
        }
        Gson gson = new Gson();
        return gson.toJson(output);
    }
}
