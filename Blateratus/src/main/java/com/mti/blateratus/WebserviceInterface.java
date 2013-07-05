    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Reblater;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ws.rs.*;

/**
 *
 * @author Noxalus
 */
public interface WebserviceInterface {
    @WebMethod
    @GET
    @Path("/register/{name}/{password}")
    public Model register(@PathParam("name") @WebParam(name="name") String name,
                          @PathParam("password") @WebParam(name="password") String password);
    
    @WebMethod
    @POST
    @Path("/connection")
    public Model connection(@FormParam("username")String username,
                            @FormParam("password")String password);
    
    @WebMethod
    @GET
    @Path("/blaters/{user_id}")
    public List<Blater> getBlaters(@PathParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @GET
    @Path("/blater/{id}")
    public Model getBlater(@PathParam("id") @WebParam(name="id") int id);
    
    @WebMethod
    @POST
    @Path("/blater/{user_token}")
    public Model postBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, @FormParam("content") String content);

    @WebMethod
    @PUT
    @Path("/blater/{blater_id}/{user_token}")
    public Model updateBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id,
                             @FormParam("content") String content);
    
    @WebMethod
    @DELETE
    @Path("/blater/{blater_id}/{user_token}")
    public Model deleteBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id);
    
    @WebMethod
    @GET
    @Path("/reblaters/{user_id}")
    public List<Reblater> getReblaters(@PathParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @GET
    @Path("/reblater/{id}")
    public Model getReblater(@PathParam("id") @WebParam(name="id") int id);
    
    @WebMethod
    @POST
    @Path("/reblater/{user_token}")
    public Model postReblater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              int blater_id);
    
    @WebMethod
    @DELETE
    @Path("/reblater/{reblater_id}/{user_token}")
    public Model deleteReblater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              int reblater_id);
}
