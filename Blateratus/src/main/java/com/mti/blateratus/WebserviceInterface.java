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
    
    @POST
    @Path("/connection")
    public Model connection(@FormParam("username")String username,
                            @FormParam("password")String password);
    
    @GET
    @Path("/users")
    public List<Users> getUsers();
    
    @GET
    @Path("/users/{user_id}")
    public Model getUser(@PathParam("user_id") int user_id);
    
    @GET
    @Path("/blaters")
    public List<Blater> getBlaters(@QueryParam("user_id") int user_id,
                                   @QueryParam("mine") boolean mine);
    
    @GET
    @Path("/blaters/{id}")
    public Model getBlater(@PathParam("id") int id);
    
    @POST
    @Path("/blaters")
    public Model postBlater(@QueryParam("user_token") String user_token, @FormParam("content") String content);

    @PUT
    @Path("/blaters/{blater_id}")
    public Model updateBlater(@QueryParam("user_token") String user_token, 
                              @PathParam("blater_id") int blater_id,
                              @FormParam("content") String content);
    
    @DELETE
    @Path("/blaters/{blater_id}")
    public Model deleteBlater(@QueryParam("user_token") String user_token, 
                              @PathParam("blater_id") int blater_id);
    
    @GET
    @Path("/reblaters")
    public List<Reblater> getReblaters(@QueryParam("user_id") int user_id);

    @GET
    @Path("/reblaters/{id}")
    public Model getReblater(@PathParam("id") int id);
    
    @POST
    @Path("/reblaters")
    public Model postReblater(@QueryParam("user_token") String user_token, 
                              int blater_id);
    
    @DELETE
    @Path("/reblaters/{reblater_id}")
    public Model deleteReblater(@QueryParam("user_token") String user_token, 
                                @PathParam("reblater_id") int reblater_id);
    
    @GET
    @Path("/follows")
    public List<Follow> getFollows(@QueryParam("user_id") int user_id);
    
    @GET
    @Path("/follows/{id}")
    public Model getFollow(@PathParam("id") int id);
    
    @POST
    @Path("/follows")
    public Model postFollow(@QueryParam("user_token") String user_token, 
                            @FormParam("follow_user_id") int follow_user_id);
    
    @DELETE
    @Path("/follows/{follow_id}")
    public Model deleteFollow(@QueryParam("user_token") String user_token, 
                              @PathParam("follow_id")  int follow_id);
}
