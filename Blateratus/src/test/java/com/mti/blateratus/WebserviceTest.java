/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Users;
import com.mti.blateratus.model.Error;
import com.mti.blateratus.model.User_Session;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

/**
 *
 * @author Martial
 */
public class WebserviceTest extends TestCase {

    private String username;
    private String password;
    private String token;
    
    private Model CreateRandomUser()
    {
        SecureRandom random = new SecureRandom();
        
        String username = new BigInteger(130, random).toString(32);
        String password = new BigInteger(130, random).toString(32);

         Webservice instance = new Webservice();
        instance.register(username, password);
        System.out.println("Registered a new user with these credentials:\n\tUsername: " + username + "\n\tPassword: " + password);

        Model result = instance.connection(username, password);

        if (result instanceof Error) {
            Error error = (Error) result;
            return error;
        }

        User_Session userSession = (User_Session) result;
        
        return userSession;
    }
    
    public WebserviceTest(String testName) {
        super(testName);
        SecureRandom random = new SecureRandom();
        
        this.username = new BigInteger(130, random).toString(32);
        this.password = new BigInteger(130, random).toString(32);
        this.token = null;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
}
