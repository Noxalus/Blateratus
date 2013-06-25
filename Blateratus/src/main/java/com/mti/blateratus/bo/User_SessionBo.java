/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.bo;

import com.mti.blateratus.dao.User_SessionDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.User_Session;

/**
 *
 * @author Noxalus
 */
public class User_SessionBo {
    private User_SessionDao usersessionDao;
    
    public void setUsersessionDao(User_SessionDao dao)
    {
        usersessionDao = dao;
    }
    
    public User_SessionDao getUsersessionDao()
    {
        return usersessionDao;
    }
    
    public void save(User_Session user_session) {
        usersessionDao.save(user_session);
    }

    public void update(User_Session user_session) {
        usersessionDao.update(user_session);
    }

    public void delete(User_Session user_session) {
        usersessionDao.delete(user_session);
    }

    public User_Session find(int id) {
        return usersessionDao.find(id);
    }    
    
    public Model Add(User_Session user_session)
    {
        return usersessionDao.Add(user_session);
    }
    
    public int getNbReservation(int session_id)
    {
        return usersessionDao.getNbReservation(session_id);
    }
}
