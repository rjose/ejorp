package com.ejorp.core;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
    private Logger logger = Logger.getLogger("ejorp-spike");

    @PostConstruct
    private void initSelf() {
        logger.info("Constructed! EjorpSingleton!");
    }
}
