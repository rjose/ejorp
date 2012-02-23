/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.models;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashSet;

import redis.clients.jedis.Jedis;

public class User {
    private Long id;
    private String name;
    private String email;
    private Collection<Long> objectiveIds;

    private User(Long id, String name, String email, Collection<Long> objectiveIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.objectiveIds = objectiveIds;
    }

    private User(Long id, String name, String email) {
        this(id, name, email, new HashSet<Long>());
    }

    public static User create(Jedis jedis, String name, String email) {
        Long id = jedis.incr("global:userid");
        jedis.set("user:" + id.toString() + ":name", name);
        jedis.set("user:" + id.toString() + ":email", email);
        return new User(id, name, email, null);
    }

    public static User get(Jedis jedis, Long id) {
        String name = jedis.get("user:" + id.toString() + ":name");
        String email = jedis.get("user:" + id.toString() + ":email");
        Collection<String> objectives = jedis.smembers("user:" + id.toString() + ":objectives");
        if (name == null || email == null || objectives == null) {
            return null;
        }
        Collection<Long> objectiveIds = new HashSet<Long>();
        for (String objective : objectives) {
            objectiveIds.add(Long.decode(objective));
        }
        return new User(id, name, email, objectiveIds);
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getEmail() {
      return email;
    }

    public Collection<Long> getObjectives() {
      return objectiveIds;
    }
}
