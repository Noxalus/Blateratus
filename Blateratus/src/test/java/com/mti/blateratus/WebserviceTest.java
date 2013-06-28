/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Users;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author Martial
 */
public class WebserviceTest extends TestCase {
    
    public WebserviceTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of Register method, of class Webservice.
     */
    public void testRegister() {
        System.out.println("Register");
        SecureRandom random = new SecureRandom();
        String name = new BigInteger(130, random).toString(32);
        String password = new BigInteger(130, random).toString(32);
        Webservice instance = new Webservice();
        Users result = (Users)instance.Register(name, password);
        assertEquals(name, result.getName());
        assertEquals(Md5.encode(password), result.getHash());
    }

    /**
     * Test of Connection method, of class Webservice.
     */
    public void testConnection() {
        System.out.println("Connection");
        SecureRandom random = new SecureRandom();
        String username = new BigInteger(130, random).toString(32);
        String password = new BigInteger(130, random).toString(32);
        
        Webservice instance = new Webservice();
        instance.Register(username, password);
        System.out.println("Registered a new user with these credentials:\nUsername: " + username + "\nPassword: " + password);
        
        Model result = instance.Connection(username, password);
        assertTrue(true);
    }

    /**
     * Test of getBlater method, of class Webservice.
     */
    public void testGetBlater() {
        System.out.println("getBlater");
        int id = 0;
        Webservice instance = new Webservice();
        Model expResult = null;
        Model result = instance.getBlater(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getBlaters method, of class Webservice.
     */
    public void testGetBlaters() {
        /*
        System.out.println("getBlaters");
        int user_id = 6;
        Webservice instance = new Webservice();
        List result = instance.getBlaters(user_id);
        */
        assertTrue(true);
        //assertTrue(!result.isEmpty());
    }

    /**
     * Test of postBlater method, of class Webservice.
     */
    public void testPostBlater() {
        System.out.println("postBlater");
        /*
        String token = "";
        String content = "";
        Webservice instance = new Webservice();
        Model expResult = null;
        Model result = instance.postBlater(token, content);
        assertEquals(expResult, result);
        */
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of updateBlater method, of class Webservice.
     */
    public void testUpdateBlater() {
        System.out.println("updateBlater");
        /*
        String token = "";
        int blater_id = 0;
        String content = "";
        Webservice instance = new Webservice();
        Model expResult = null;
        Model result = instance.updateBlater(token, blater_id, content);
        assertEquals(expResult, result);
        */
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteBlater method, of class Webservice.
     */
    public void testDeleteBlater() {
        /*
        System.out.println("deleteBlater");
        String token = "";
        int blater_id = 0;
        Webservice instance = new Webservice();
        Model expResult = null;
        Model result = instance.deleteBlater(token, blater_id);
        assertEquals(expResult, result);
        */
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
        //fail("The test case is a prototype.");
    }
}
