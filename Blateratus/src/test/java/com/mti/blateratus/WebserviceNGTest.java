/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Users;
import com.mti.blateratus.model.Error;
import com.mti.blateratus.model.User_Session;
import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Reblater;
import com.mti.blateratus.model.Follow;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

/**
 *
 * @author Martial
 */
public class WebserviceNGTest {
    
    private String username;
    private String password;
    private User_Session user_session;
    private Blater blater;
    private Reblater reblater;
    private Follow follow;
    
    public WebserviceNGTest() {
        SecureRandom random = new SecureRandom();
        
        this.username = new BigInteger(130, random).toString(32);
        this.password = new BigInteger(130, random).toString(32);
        this.user_session = null;
        this.blater = null;
        this.reblater = null;
        this.follow = null;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of register method, of class Webservice.
     */
    @Test
    public void testRegister() {
        System.out.println("Register");
        
        Webservice instance = new Webservice();
        Model result = instance.register(this.username, this.password);
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }

        Users user = (Users) result;
        assertEquals(this.username, user.getName());
        assertEquals(Md5.encode(this.password), user.getHash());
    }

    /**
     * Test of connection method, of class Webservice.
     */
    @Test(dependsOnMethods={"testRegister"})
    public void testConnection() {
        System.out.println("Connection");
        
        Webservice instance = new Webservice();
        Model result = instance.connection(this.username, this.password);

        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }

        User_Session us = (User_Session) result;
        assertNotNull(us.getUser_id());
        assertNotNull(us.getToken());
        
        this.user_session = us;
    }

    /**
     * Test of getBlater method, of class Webservice.
     */
    @Test(dependsOnMethods={"testUpdateBlater"})
    public void testGetBlater() {
        System.out.println("getBlater");
        Webservice instance = new Webservice();
        
        Model result = instance.getBlater(this.blater.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        Blater blater = (Blater)result;
        
        assertEquals(this.blater.getContent() + "42", blater.getContent());
        assertEquals(this.blater.getUser_id(), blater.getUser_id());
    }

    /**
     * Test of getBlaters method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testPostBlater"})
    public void testGetBlaters() {
        System.out.println("getBlaters");
        
        Webservice instance = new Webservice();
        
        List result = instance.getBlaters(this.user_session.getUser_id());
        
        assertTrue(!result.isEmpty());
        Blater blater = (Blater)result.get(0);
        assertEquals(blater.getUser_id(), this.user_session.getUser_id());
        assertEquals(blater.getContent(), this.username + "COUCOU");
        
        this.blater = blater;
    }

    /**
     * Test of postBlater method, of class Webservice.
     */
    @Test(dependsOnMethods={"testConnection"})
    public void testPostBlater() {
        System.out.println("postBlater");
        Webservice instance = new Webservice();

        Model result = instance.postBlater(this.user_session.getToken(), this.username + "COUCOU");
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }

        assertTrue(true);
    }

    /**
     * Test of updateBlater method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetBlaters"})
    public void testUpdateBlater() {
        System.out.println("updateBlater");
        Webservice instance = new Webservice();
        
        Model result = instance.updateBlater(this.user_session.getToken(), this.blater.getId(), this.blater.getContent() + "42");
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        assertTrue(true);
    }

    /**
     * Test of deleteBlater method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testDeleteReblater"})
    public void testDeleteBlater() {
        System.out.println("deleteBlater");
        
        Webservice instance = new Webservice();
        
        Model result = instance.deleteBlater(this.user_session.getToken(), this.blater.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        assertTrue(true);
    }

    /**
     * Test of getReblater method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetReblaters"})
    public void testGetReblater() {
        System.out.println("getReblater");
        
        Webservice instance = new Webservice();

        Model result = instance.getReblater(this.reblater.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        Reblater reblater = (Reblater)result;
        
        assertEquals(this.reblater.getDate(), reblater.getDate());
        assertEquals(this.reblater.getUser_id(), reblater.getUser_id());
        assertEquals(this.blater.getId(), reblater.getBlater_id());
    }

    /**
     * Test of getReblaters method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testPostReblater"})
    public void testGetReblaters() {
        System.out.println("getReblaters");
        Webservice instance = new Webservice();
        
        List result = instance.getReblaters(this.user_session.getUser_id());
        
        assertTrue(!result.isEmpty());
        Reblater reblater = (Reblater)result.get(0);
        assertEquals(reblater.getUser_id(), this.user_session.getUser_id());
        assertEquals(reblater.getBlater_id(), this.blater.getId());
        assertNotNull(reblater.getDate());
        
        this.reblater = reblater;
    }

    /**
     * Test of postReblater method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetBlater"})
    public void testPostReblater() {
        System.out.println("postReblater");
        
        Webservice instance = new Webservice();
        
        Model result = instance.postReblater(this.user_session.getToken(), this.blater.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        assertTrue(true);
    }

    /**
     * Test of deleteReblater method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetReblater"})
    public void testDeleteReblater() {
        System.out.println("deleteReblater");
        Webservice instance = new Webservice();
        
        Model result = instance.deleteReblater(this.user_session.getToken(), this.reblater.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        assertTrue(true);
    }

    /**
     * Test of getFollows method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testPostFollow"})
    public void testGetFollows() {
        System.out.println("getFollows");
        
        Webservice instance = new Webservice();

        List result = instance.getFollows(this.user_session.getUser_id());
        
        assertTrue(!result.isEmpty());
        Follow follow = (Follow)result.get(0);
        assertEquals(follow.getUser_id(), this.user_session.getUser_id());
        assertEquals(follow.getFollow_user_id(), this.user_session.getUser_id());
        
        this.follow = follow;
    }

    /**
     * Test of getFollow method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetFollows"})
    public void testGetFollow() {
        System.out.println("getFollow");
        
        Webservice instance = new Webservice();

        Model result = instance.getFollow(this.follow.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        Follow follow = (Follow)result;
        
        assertEquals(this.follow.getUser_id(), follow.getUser_id());
        assertEquals(this.follow.getFollow_user_id(), follow.getFollow_user_id());
    }

    /**
     * Test of postFollow method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testConnection"})
    public void testPostFollow() {
        System.out.println("postFollow");
        
        Webservice instance = new Webservice();
        
        Model result = instance.postFollow(this.user_session.getToken(), this.user_session.getUser_id());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        assertTrue(true);
    }

    /**
     * Test of deleteFollow method, of class Webservice.
     */
    @Test(dependsOnMethods = {"testGetFollow"})
    public void testDeleteFollow() {
        System.out.println("deleteFollow");
        
        Webservice instance = new Webservice();
        
        Model result = instance.deleteFollow(this.user_session.getToken(), this.follow.getId());
        if (result instanceof Error) {
            Error error = (Error) result;
            fail(error.getMessage());
        }
        
        assertTrue(true);
    }
}