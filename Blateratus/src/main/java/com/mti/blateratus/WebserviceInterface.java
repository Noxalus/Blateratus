    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Users;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Noxalus
 */
public interface WebserviceInterface {
    @WebMethod
    @GET
    @Path("/getUser/{id}")
    public Users getUser(@PathParam("id") @WebParam(name="id") int id);

    @WebMethod
    @GET
    @Path("/register/{name}/{password}")
    public Model Register(@PathParam("name") @WebParam(name="name") String name,
                          @PathParam("password") @WebParam(name="password") String password);
    
    @WebMethod
    @GET
    @Path("/Connection/{username}/{password}")
    public Model Connection(@PathParam("username") @WebParam(name="username") String username,
                            @PathParam("password") @WebParam(name="password") String password);
    
    @WebMethod
    @GET
    @Path("/getBlater/{id}")
    public Model getBlater(@PathParam("id") @WebParam(name="id") int id);

    @WebMethod
    @GET
    @Path("/getBlaters/{user_id}")
    public List<Blater> getBlaters(@PathParam("user_id") @WebParam(name="user_id") int user_id);
}
