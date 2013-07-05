    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.*;
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
    @POST
    @Path("/register")
    public Model register(@FormParam("name") String name,
                          @FormParam("password") String password);
    
    @WebMethod
    @POST
    @Path("/connection")
    public Model connection(@FormParam("username")String username,
                            @FormParam("password")String password);
    
    @WebMethod
    @GET
    @Path("/users")
    public List<Users> getUsers();
    
    @WebMethod
    @GET
    @Path("/users/{user_id}")
    public Model getUser(@PathParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @GET
    @Path("/blaters")
    public List<Blater> getBlaters(@QueryParam("user_id") @WebParam(name="user_id") int user_id,
                                    @QueryParam("mine") @WebParam(name="mine") boolean mine);
    
    
    @WebMethod
    @GET
    @Path("/blaters/{id}")
    public Model getBlater(@PathParam("id") @WebParam(name="id") int id);
    
    @WebMethod
    @POST
    @Path("/blaters")
    public Model postBlater(@QueryParam("user_token") @WebParam(name="user_token") String user_token, @FormParam("content") String content);

    @WebMethod
    @PUT
    @Path("/blaters/{blater_id}")
    public Model updateBlater(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id,
                             @FormParam("content") String content);
    
    @WebMethod
    @DELETE
    @Path("/blaters/{blater_id}")
    public Model deleteBlater(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id);
    
    @WebMethod
    @GET
    @Path("/reblaters")
    public List<Reblater> getReblaters(@QueryParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @GET
    @Path("/reblaters/{id}")
    public Model getReblater(@PathParam("id") @WebParam(name="id") int id);
    
    @WebMethod
    @POST
    @Path("/reblaters")
    public Model postReblater(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                              int blater_id);
    
    @WebMethod
    @DELETE
    @Path("/reblaters/{reblater_id}")
    public Model deleteReblater(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("reblater_id") @WebParam(name="reblater_id") int reblater_id);
    
    @WebMethod
    @GET
    @Path("/follows")
    public List<Follow> getFollows(@QueryParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @GET
    @Path("/follows/{id}")
    public Model getFollow(@PathParam("id") @WebParam(name="id") int id);
    
    @WebMethod
    @POST
    @Path("/follows")
    public Model postFollow(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                            @FormParam("follow_user_id") int follow_user_id);
    
    @WebMethod
    @DELETE
    @Path("/follows/{follow_id}")
    public Model deleteFollow(@QueryParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("follow_id") @WebParam(name="follow_id")  int follow_id);
}
