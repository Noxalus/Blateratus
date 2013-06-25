/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.bo.BlaterBo;
import com.mti.blateratus.bo.User_SessionBo;
import com.mti.blateratus.bo.UsersBo;
import com.mti.blateratus.dao.BlaterDao;
import com.mti.blateratus.dao.User_SessionDao;
import com.mti.blateratus.dao.UsersDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.ErrModel;
import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.User_Session;
import com.mti.blateratus.model.Users;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Noxalus
 */
@WebService(serviceName = "Webservice")
public class Webservice implements WebserviceInterface {

    public Users getUser(int id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
    	UsersBo userBo = (UsersBo)appContext.getBean("UsersBo");
        userBo.setUsersDao((UsersDao)appContext.getBean("usersDao"));
        return userBo.find(id);
    }  

    public Model Register(String name, String password) {
        try 
        {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] token = md.digest(String.valueOf(System.nanoTime()).getBytes());
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            UsersBo userBo = (UsersBo)appContext.getBean("UsersBo");
            userBo.setUsersDao((UsersDao)appContext.getBean("usersDao"));
            Users user = new Users();
            user.setName(name);
            user.setHash(password);
            user.setToken(String.valueOf(token));

            return userBo.add(user);
        }
        catch (NoSuchAlgorithmException ex) {
            ErrModel error = new ErrModel();
            error.setMesage("MessageDiegest error");
            return error;
        }
    }

    public Model Connection(String username, String password) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        UsersBo userBo = (UsersBo)appContext.getBean("UsersBo");
        userBo.setUsersDao((UsersDao)appContext.getBean("usersDao"));
        return userBo.connect(username, password);
    }

    public Model getBlater(int id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        BlaterBo blaterBo = (BlaterBo)appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao)appContext.getBean("blaterDao"));
        return blaterBo.find(id);
    }

    public List<Blater> getBlaters(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        BlaterBo blaterBo = (BlaterBo)appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao)appContext.getBean("blaterDao"));
        return blaterBo.getAll();
    }
}
