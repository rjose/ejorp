/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejorp.core;

import com.google.gson.Gson;
import ejorp_clj.core.Sample;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author rjose
 */
@Path("helloworld")
public class HelloWorld {

    @Context
    private UriInfo context;

    /** Creates a new instance of HelloWorld */
    public HelloWorld() {
    }

    /**
     * Retrieves representation of an instance of com.ejorp.helloworld.HelloWorld
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getHtml() {
        Gson gson = new Gson();
        double[] coeffs = {Sample.binomial(5, 3)};
        return gson.toJson(coeffs);
    }

    /**
     * PUT method for updating or creating an instance of HelloWorld
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}
