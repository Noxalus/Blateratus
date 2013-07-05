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
import java.util.Collections;
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
            error.setMesage("Cet utilisateur n'est pas connectï¿½ ou n'existe pas !");
            return error;
        }

        return user;
    }

    /**
     * Register a new user into the database.
     *
     * @param username user's name
     * @param password user's password
     * @return a Model that is either an Error or a Users object
     */
    public Model register(String username, String password) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
            userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));

            String hash = Md5.encode(password);

            Users user = new Users();
            user.setName(username);
            user.setHash(hash);

            return userBo.add(user);
        } catch (Exception ex) {
            Error error = new Error();
            error.setMesage(ex.getMessage());
            return error;
        }
    }

    /**
     * Check that the user exists into the database and that the password given
     * matchs well. <p> If there is a problem (user doesn't exist, wrong
     * password, etc...) an Error object is returned with retails, else, a
     * session is created an insert into the database with a random token to
     * identify the user..
     *
     * @param username user's name
     * @param password user's password
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
    
    /**
     * Get the list of all users registered into the database.
     * <p>
     * MD5 hash of each user is not present.
     *
     * @return a list of users
     */
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
    
    /**
     * Delete a specific user with his session.
     *
     * @param user_id id of user we want to delete
     * @return a model that is an Error or a SuccessModel
     */
    public Model deleteUser(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        UsersBo userBo = (UsersBo) appContext.getBean("UsersBo");
        userBo.setUsersDao((UsersDao) appContext.getBean("usersDao"));
        User_SessionBo user_sessionBo = (User_SessionBo) appContext.getBean("User_SessionBo");
        user_sessionBo.setUsersessionDao((User_SessionDao) appContext.getBean("usersessionDao"));

        Model model;
        if ((model = user_sessionBo.findByUserId(user_id)) instanceof Error) {
            return model;
        }
        
        User_Session user_session = (User_Session)model;
        
        if ((model = userBo.find(user_id)) instanceof Error) {
            return model;
        }

        Users user = (Users)model;

        user_sessionBo.delete(user_session);
        userBo.delete(user);
        
        return new SuccessModel();
    }

    /**
     * Get a specific user.
     * <p>
     * MD5 hash of each user is not present.
     *
     * @param id of the user we want to get
     * @return a list of users
     */
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

    /**
     * Get the blater whose the id is the one given as argument
     *
     * @param id blater's id
     * @return a Model that is either an Error or a Blater
     */
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

    /**
     * Get all blaters of a specific user whose the id is given as argument.
     * <p>
     * If the second argument is true, this method returns only the blaters
     * written by this user, else that returns also blaters of followed users.
     *
     * @param user_id user's id
     * @param mine boolean to know if we want only user's blaters or also
     * blaters of followed users
     * @return a list of blaters
     */
    public List<Blater> getBlaters(int user_id, boolean mine) {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
            BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
            blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

            FollowBo fBo = (FollowBo) appContext.getBean("FollowBo");
            fBo.setFollowDao((FollowDao) appContext.getBean("followDao"));

            ReblaterBo rBo = (ReblaterBo) appContext.getBean("ReblaterBo");
            rBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));

            if (mine) {

                List<Reblater> reblaters = rBo.getAll(user_id);
                List<Blater> complete_list = blaterBo.getAllFromUser(user_id);

                for (Reblater reblater : reblaters) {
                    Blater b = blaterBo.find(reblater.getBlater_id());
                    if (b != null) {
                        b.setDate(reblater.getDate());
                        complete_list.add(b);
                    }
                }

                Collections.sort(complete_list);

                return complete_list;
            } else {
                List<Follow> followed = fBo.getAll(user_id);
                List<Reblater> reblaters = rBo.getAll(user_id);
                List<Blater> complete_list = blaterBo.getAllFromUser(user_id);
                for (Follow follow : followed) {
                    List<Blater> list = blaterBo.getAllFromUser(follow.getFollow_user_id());
                    for (Blater b : list) {
                        complete_list.add(b);
                    }
                }
                for (Reblater reblater : reblaters) {
                    Blater b = blaterBo.find(reblater.getBlater_id());
                    if (b != null) {
                        b.setDate(reblater.getDate());
                        complete_list.add(b);
                    }
                }
                Collections.sort(complete_list);
                return complete_list;
            }
        } catch (Exception e) {
            return new LinkedList<Blater>();
        }


    }

    /**
     * Insert a new blater into the database.
     *
     * @param user_token token of the current user session
     * @param content content of the blater
     * @return a model that is an Error or the blater inserted
     */
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

    /**
     * Update a specific blater.
     *
     * @param user_token token of the current user session
     * @param blater_id id of blater we want to update
     * @param content new content of the blater
     * @return a model that is an Error or the blater inserted
     */
    public Model updateBlater(String user_token, int blater_id, String content) {
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
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        if (user.getId() == blater.getUser_id()) {
            blater.setContent(content);

            return blaterBo.add(blater);
        } else {
            error.setMesage("Ce blater n'a pas ï¿½tï¿½ ï¿½crit par cet utilisateur !");
            return error;
        }
    }

    /**
     * Delete a specific blater.
     *
     * @param user_token token of the current user session
     * @param blater_id id of blater we want to delete
     * @return a model that is an Error or a SuccessModel
     */
    public Model deleteBlater(String user_token, int blater_id) {
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
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
            return model;
        }

        Users user = (Users) model;

        if (user.getId() == blater.getUser_id()) {
            blaterBo.delete(blater);

            return new SuccessModel();
        } else {
            error.setMesage("Ce blater n'a pas ï¿½tï¿½ ï¿½crit pas cet utilisateur !");
            return error;
        }
    }

    /**
     * Get a specific reblater.
     *
     * @param id id of the reblater that we want to get
     * @return a model that is an Error or a Reblater
     */
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

    /**
     * Get all reblaters of a specific user.
     *
     * @param user_id id of the user whose we want to get reblaters
     * @return a list of Reblaters
     */
    public List<Reblater> getReblaters(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));
        return reblaterBo.getAll(user_id);
    }

    /**
     * Insert a new reblater into the database.
     *
     * @param user_token token of the current user session
     * @param blater_id id of blater we want to reblater
     * @return a model that is an Error or the reblater inserted
     */
    public Model postReblater(String user_token, int blater_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        BlaterBo blaterBo = (BlaterBo) appContext.getBean("BlaterBo");
        blaterBo.setBlaterDao((BlaterDao) appContext.getBean("blaterDao"));

        ReblaterBo reblaterBo = (ReblaterBo) appContext.getBean("ReblaterBo");
        reblaterBo.setReblaterDao((ReblaterDao) appContext.getBean("reblaterDao"));

        Model model;
        if ((model = this.getUserByToken(user_token)) instanceof Error) {
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

    /**
     * Delete a specific reblater.
     *
     * @param user_token token of the current user session
     * @param reblater_id id of reblater we want to delete
     * @return a model that is an Error or a SuccessModel
     */
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
            error.setMesage("Ce reblater n'a pas ï¿½tï¿½ soumis par cet utilisateur !");
            return error;
        }
    }

    /**
     * Get all users that this user follows.
     *
     * @param user_id id of the user whom we want his follows
     * @return a list of Follow
     */
    public List<Follow> getFollows(int user_id) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        FollowBo followBo = (FollowBo) appContext.getBean("FollowBo");
        followBo.setFollowDao((FollowDao) appContext.getBean("followDao"));
        return followBo.getAll(user_id);
    }

    /**
     * Get a specific follow.
     *
     * @param id id of the follow we want to get
     * @return a model that is an Error or a Follow
     */
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

    /**
     * Insert a new follow into the database.
     *
     * @param user_token token of the current user session
     * @param follow_user_id id of the user we want to follow
     * @return a model that is an Error or a Follow
     */
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

    /**
     * Delete a specific follow.
     *
     * @param user_token token of the current user session
     * @param follow_id id of the follow we want to get
     * @return a model that is an Error or a Follow
     */
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
            error.setMesage("Ce follow n'a pas ï¿½tï¿½ soumis par cet utilisateur !");
            return error;
        }
    }
}
