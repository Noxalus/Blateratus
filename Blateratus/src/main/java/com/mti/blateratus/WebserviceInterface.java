    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Model;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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
    @GET
    @Path("/connection/{username}/{password}")
    public Model connection(@PathParam("username") @WebParam(name="username") String username,
                            @PathParam("password") @WebParam(name="password") String password);
    
    @WebMethod
    @GET
    @Path("/blater/{id}")
    public Model getBlater(@PathParam("id") @WebParam(name="id") int id);

    @WebMethod
    @GET
    @Path("/blaters/{user_id}")
    public List<Blater> getBlaters(@PathParam("user_id") @WebParam(name="user_id") int user_id);
    
    @WebMethod
    @POST
    @Path("/blater")
    public Model postBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                            @PathParam("content") @WebParam(name="content") String content);

    @WebMethod
    @GET
    @Path("/updateBlater/{user_token}/{blater_id}/{content}")
    public Model updateBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                            @PathParam("blater_id") @WebParam(name="blater_id") int blater_id,
                            @PathParam("content") @WebParam(name="content") String content);
    
    @WebMethod
    @GET
    @Path("/deleteBlater/{user_token}/{blater_id}")
    public Model deleteBlater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id);
    
    @WebMethod
    @GET
    @Path("/postReblater/{user_token}/{blater_id}")
    public Model postReblater(@PathParam("user_token") @WebParam(name="user_token") String user_token, 
                              @PathParam("blater_id") @WebParam(name="blater_id") int blater_id);
}
