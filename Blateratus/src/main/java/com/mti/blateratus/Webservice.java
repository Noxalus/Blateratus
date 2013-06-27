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
import com.mti.blateratus.model.Error;
import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.SuccessModel;
import com.mti.blateratus.model.User_Session;
import com.mti.blateratus.model.Users;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
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

    private Model getUserByToken(String token) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
        userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));
        User_SessionBo user_sessionBo = (User_SessionBo) appContext.getBean("User_SessionBo");
        user_sessionBo.setUsersessionDao((User_SessionDao) appContext.getBean("usersessionDao"));

        User_Session user_session = user_sessionBo.findByToken(token);
        Users user = userBo.find(user_session.getUser_id());

        if (user == null) {
            Error error = new Error();
            error.setMesage("Cet utilisateur n'est pas connecté ou n'existe pas !");
            return error;
        }

        return user;
    }

    public Model Register(String name, String password) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
            userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));

            String hash = Md5.encode(password);

            Users user = new Users();
            user.setName(name);
            user.setHash(hash);

            return userBo.add(user);
        } catch (Exception ex) {
            Error error = new Error();
            error.setMesage(ex.getMessage());
            return error;
        }
    }

    public Model Connection(String username, String password) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
            userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));
            User_SessionBo user_sessionBo = (User_SessionBo) appContext.getBean("User_SessionBo");
            user_sessionBo.setUsersessionDao((User_SessionDao) appContext.getBean("usersessionDao"));

            Model model;
            if ((model = userBo.connect(username, Md5.encode(password))) instanceof Error) {
                return model;
            }

            Users user = (Users) model;

            String token = Md5.encode(String.valueOf(System.nanoTime()) + username + password);
            java.util.Date today = new java.util.Date();
            java.sql.Date sqlToday = new java.sql.Date(today.getTime());

            User_Session user_session = new User_Session();
            user_session.setToken(token);
            user_session.setUser_id(user.getId());
            user_session.setDate(sqlToday);

            return user_sessionBo.Add(user_session);
        } catch (Exception ex) {
            Error error = new Error();
            error.setMesage(ex.getMessage());
            return error;
        }
    }

    public Model getBlater(int id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));
        return blaterBo.find(id);
    }

    public List<Blater> getBlaters(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));
        return blaterBo.getAll(user_id);
    }

    public Model postBlater(String token, String content) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));
        
        Model model;
        if ((model = this.getUserByToken(token)) instanceof Error) {
            return model;
        }

        Users user = (Users)model;
        
        Blater blater = new Blater();

        blater.setUser_id(user.getId());
        blater.setContent(content);

        return blaterBo.add(blater);
    }

    public Model updateBlater(String token, int blater_id, String content) {
        Error error = new Error();
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

        Blater blater = blaterBo.find(blater_id);

        if (blater == null) {
            error.setMesage("Ce blater n'existe pas !");
            return error;
        }

        Model model;
        if ((model = this.getUserByToken(token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        if (user.getId() == blater.getUser_id()) {
            blater.setContent(content);

            return blaterBo.add(blater);
        } else {
            error.setMesage("Ce blater n'a pas été écrit pas cet utilisateur !");
            return error;
        }
    }

    public Model deleteBlater(String token, int blater_id) {
        Error error = new Error();
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

        Blater blater = blaterBo.find(blater_id);

        if (blater == null) {
            error.setMesage("Ce blater n'existe pas !");
            return error;
        }

        Model model;
        if ((model = this.getUserByToken(token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        if (user.getId() == blater.getUser_id()) {
            blaterBo.delete(blater);

            return new SuccessModel();
        } else {
            error.setMesage("Ce blater n'a pas été écrit pas cet utilisateur !");
            return error;
        }
    }
}
