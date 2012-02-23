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
    private Long ownerId;

    private Objective(Long id, String description, Long ownerId) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;
    }

    public static Objective create(Jedis jedis, String description,
                                   User owner) {
        Long id = jedis.incr("global:taskid");
        jedis.set("task:" + id.toString() + ":description", description);
        jedis.set("task:" + id.toString() + ":owner", owner.getId().toString());
        jedis.sadd("user:" + owner.getId().toString() + ":objectives",
                   id.toString());
        return new Objective(id, description, owner.getId());
    }

    public static Objective get(Jedis jedis, Long id) {
        String description = jedis.get("task:" + id.toString() + ":description");
        String owner = jedis.get("task:" + id.toString() + ":owner");
        if (description == null || owner == null) {
            return null;
        }
        return new Objective(id, description, Long.decode(owner));
    }

    public Long getId() {
      return id;
    }

    public String getDescription() {
      return description;
    }

    public Long getOwner() {
      return ownerId;
    }
}
