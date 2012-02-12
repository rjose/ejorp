package com.ejorp.core;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 11/7/11
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
@Startup
public class EjorpSingleton {
    private Logger logger;
    private JedisPool pool;

    @PostConstruct
    private void applicationStartup() {
        logger = Logger.getLogger("ejorp-spike");
        logger.info("Constructed! EjorpSingleton!");
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @PreDestroy
    private void applicationShutdown() {
        pool.destroy();
    }

    public JedisPool getJedisPool() {
        return pool;
    }

    public Logger getLogger() {
      return logger;
    }
}
