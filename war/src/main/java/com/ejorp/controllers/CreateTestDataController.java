/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.handlers;

import com.ejorp.core.EjorpSingleton;
import com.ejorp.models.Objective;
import com.ejorp.models.Person;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;
import java.util.Map;
import java.util.TreeMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Path("create_test_data/{suite}")
@Stateless
public class CreateTestDataController {
    @Context
    private UriInfo context;

    @EJB
    private EjorpSingleton ejorp;

    @PUT
    @Produces("application/json")
    public String createTestData(@PathParam("suite") String suite) {
        Map<String, Long> result = new TreeMap<String, Long>();
        if (suite.equals("a")) {
            JedisPool pool = ejorp.getJedisPool();
            Jedis jedis = pool.getResource();
            try {
                jedis.flushDB();
                Person person = Person.create(jedis, "Test Person", "person@test.com");
                Objective objective1 = Objective.create(jedis, "Goal 1", person);
                Objective objective2 = Objective.create(jedis, "Goal 2", person);
                result.put("Test Person", person.getId());
                result.put("Goal 1", objective1.getId());
                result.put("Goal 2", objective2.getId());
            } finally {
                pool.returnResource(jedis);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(result);
    }
}
