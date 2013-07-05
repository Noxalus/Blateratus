/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus;

import com.mti.blateratus.bo.BlaterBo;
import com.mti.blateratus.bo.FollowBo;
import com.mti.blateratus.bo.ReblaterBo;
import com.mti.blateratus.bo.User_SessionBo;
import com.mti.blateratus.bo.UsersBo;
import com.mti.blateratus.dao.BlaterDao;
import com.mti.blateratus.dao.FollowDao;
import com.mti.blateratus.dao.ReblaterDao;
import com.mti.blateratus.dao.User_SessionDao;
import com.mti.blateratus.dao.UsersDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Error;
import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Follow;
import com.mti.blateratus.model.Reblater;
import com.mti.blateratus.model.SuccessModel;
import com.mti.blateratus.model.User_Session;
import com.mti.blateratus.model.Users;
import java.util.LinkedList;
import java.util.List;
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

        if (user_session == null) {
            Error error = new Error();
            error.setMesage("Cet utilisateur n'est pas connecté ou n'existe pas !");
            return error;
        }

        Users user = userBo.find(user_session.getUser_id());

        if (user == null) {
            Error error = new Error();
            error.setMesage("Cet utilisateur n'est pas connecté ou n'existe pas !");
            return error;
        }

        return user;
    }

    public Model register(String name, String password) {
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

    /**
    * Check that the user exists into the database and that the password
    * given matchs well. 
    * <p>
    * If there is a problem (user doesn't exist, wrong 
    * password, etc...) an Error object is returned with retails, else,
    * a session is created an insert into the database with a random token
    * to identify the user.. 
    *
    * @param  username  user's name
    * @param  password user's password
    * @return a Model that is either an Error or a User_Session
    */
    public Model connection(String username, String password) {
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

        Model model = blaterBo.find(id);
        if (model == null) {
            Error error = new Error();
            error.setMesage("Ce blater n'existe pas !");
            return error;
        }

        return (Blater) model;
    }

    public List<Blater> getBlaters(int user_id, boolean mine) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
            blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

            FollowBo fBo = (FollowBo) appContext.getBean("FollowBo");
            fBo.setFollowDao((FollowDao) appContext.getBean("followDao"));

            if (mine) {
                return blaterBo.getAllFromUser(user_id);
            } else {
                List<Follow> followed = fBo.getAll(user_id);
                List<Blater> complete_list = blaterBo.getAllFromUser(user_id);
                for (Follow follow : followed) {
                    List<Blater> list = blaterBo.getAllFromUser(follow.getFollow_user_id());
                    for (Blater b : list) {
                        complete_list.add(b);
                    }
                }

                return complete_list;
            }
        } catch (Exception e) {
            return new LinkedList<Blater>();
        }


    }

    public Model postBlater(String user_token, String content) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

        Model model;
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        Blater blater = new Blater();

        blater.setUser_id(user.getId());
        blater.setContent(content);
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlToday = new java.sql.Date(today.getTime());
        blater.setDate(sqlToday);
        
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
            error.setMesage("Ce blater n'a pas été écrit par cet utilisateur !");
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

    public Model getReblater(int id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));

        Model model = reblaterBo.find(id);
        if (model == null) {
            Error error = new Error();
            error.setMesage("Ce reblater n'existe pas !");
            return error;
        }

        return (Reblater) model;
    }

    public List<Reblater> getReblaters(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));
        return reblaterBo.getAll(user_id);
    }

    public Model postReblater(String token, int blater_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));

        Model model;
        if ((model = this.getUserByToken(token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        Blater blater = blaterBo.find(blater_id);
        if (blater == null) {
            Error error = new Error();
            error.setMesage("Ce blater n'existe pas !");
            return error;
        }

        Reblater reblater = new Reblater();

        reblater.setUser_id(user.getId());
        reblater.setBlater_id(blater_id);
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlToday = new java.sql.Date(today.getTime());
        reblater.setDate(sqlToday);

        return reblaterBo.add(reblater);
    }

    public Model deleteReblater(String user_token, int reblater_id) {
        Error error = new Error();
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));

        Reblater reblater = reblaterBo.find(reblater_id);

        if (reblater == null) {
            error.setMesage("Ce reblater n'existe pas !");
            return error;
        }

        Model model;
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        if (user.getId() == reblater.getUser_id()) {
            reblaterBo.delete(reblater);

            return new SuccessModel();
        } else {
            error.setMesage("Ce reblater n'a pas été soumis par cet utilisateur !");
            return error;
        }
    }

    public List<Follow> getFollows(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        FollowBo followBo = (FollowBo) appContext.getBean("FollowBo");
        followBo.setFollowDao((FollowDao) appContext.getBean("followDao"));
        return followBo.getAll(user_id);
    }

    public Model getFollow(int id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        FollowBo followBo = (FollowBo) appContext.getBean("FollowBo");
        followBo.setFollowDao((FollowDao) appContext.getBean("followDao"));

        Model model = followBo.find(id);
        if (model == null) {
            Error error = new Error();
            error.setMesage("Ce follow n'existe pas !");
            return error;
        }

        return (Follow) model;
    }

    public Model postFollow(String user_token, int follow_user_id) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

            UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
            userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));

            FollowBo followBo = (FollowBo) appContext.getBean("FollowBo");
            followBo.setFollowDao((FollowDao) appContext.getBean("followDao"));

            Model model;
            if ((model = this.getUserByToken(user_token)) instanceof Error) {
                return model;
            }

            Users user = (Users) model;

            Users followUser = userBo.find(follow_user_id);
            if (followUser == null) {
                Error error = new Error();
                error.setMesage("L'utilisateur que vous voulez suivre n'existe pas !");
                return error;
            }

            Follow follow = new Follow();

            follow.setUser_id(user.getId());
            follow.setFollow_user_id(follow_user_id);


            return followBo.add(follow);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            Error error = new Error();
            error.setMesage("Vous ne pouvez pas follow le meme utilisateur deux fois.");
            return error;
        }


    }

    public Model deleteFollow(String user_token, int follow_id) {
        Error error = new Error();
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        Model model;
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
            return model;
        }

        FollowBo followBo = (FollowBo) appContext.getBean("FollowBo");
        followBo.setFollowDao((FollowDao) appContext.getBean("followDao"));

        Follow follow = followBo.find(follow_id);

        if (follow == null) {
            error.setMesage("Ce follow n'existe pas !");
            return error;
        }

        Users user = (Users) model;

        if (user.getId() == follow.getUser_id()) {
            followBo.delete(follow);

            return new SuccessModel();
        } else {
            error.setMesage("Ce follow n'a pas été soumis par cet utilisateur !");
            return error;
        }
    }

    public List<Users> getUsers() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        UsersBo usersBo = (UsersBo) appContext.getBean("UsersBo");
        usersBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));
        List<Users> list = usersBo.getAll();
        for (Users user : list) {
            user.setHash(null);
        }

        return list;
    }

    public Model getUser(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        UsersBo usersBo = (UsersBo) appContext.getBean("UsersBo");
        usersBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));

        try {

            Model model = usersBo.find(user_id);
            if (model == null) {
                Error error = new Error();
                error.setMesage("Cet utilisateur n'existe pas !");
                return error;
            }
            Users user = (Users) model;
            user.setHash(null);

            return user;
        } catch (IndexOutOfBoundsException e) {
            Error error = new Error();
            error.setMesage("Cet utilisateur n'existe pas !");
            return error;
        }

    }
}
