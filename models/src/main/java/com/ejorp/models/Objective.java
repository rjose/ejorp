/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.models;

import com.ejorp.models.User;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

public class Objective {
    private Long id;
    private String description;
    private User owner;

    private Objective(Long id, String description, User owner) {
      this.id = id;
      this.description = description;
      this.owner = owner;
    }

    public static Objective create(Jedis jedis, String description,
                                   User owner) {
        Long id = jedis.incr("global:taskid");
        jedis.set("task:" + id.toString() + ":description", description);
        jedis.set("task:" + id.toString() + ":owner", owner.getId().toString());
        jedis.sadd("user:" + owner.getId().toString() + ":tasks",
                   id.toString());
        return new Objective(id, description, owner);
    }

    public Long getId() {
      return id;
    }

    public String getDescription() {
      return description;
    }

    public User getOwner() {
      return owner;
    }
}
