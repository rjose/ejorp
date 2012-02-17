/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.models;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

public class User {
    private Long id;
    private String name;
    private String email;

    private User(Long id, String name, String email) {
      this.id = id;
      this.name = name;
      this.email = email;
    }

    public static User create(Jedis jedis, String name, String email) {
        Long id = jedis.incr("global:userid");
        jedis.set("user:" + id.toString() + ":name", name);
        jedis.set("user:" + id.toString() + ":email", email);
        return new User(id, name, email);
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
}
