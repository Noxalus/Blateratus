/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.dao;

import com.mti.blateratus.model.ErrModel;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.SuccessModel;
import com.mti.blateratus.model.User_Session;
import com.mti.blateratus.model.Users;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Noxalus
 */
public class User_SessionDao extends HibernateDaoSupport {
    public void save(User_Session user_session) {
        getHibernateTemplate().save(user_session);
    }

    public void update(User_Session user_session) {
        getHibernateTemplate().update(user_session);
    }

    public void delete(User_Session user_session) {
        getHibernateTemplate().delete(user_session);
    }

    public User_Session find(int id) {
        List list = getHibernateTemplate().find("from User_Session where id=?", id);
        return (User_Session)list.get(0);
    }
    
    public int getNbReservation(int session_id)
    {
        List list = getHibernateTemplate().find("from User_Session where session_id=?", session_id);
        return list.size();
    }
    
    public Model Add(User_Session user_session)
    {
        Integer[] params = new Integer[] { user_session.getUser_id(), user_session.getSession_id() };
        List list = getHibernateTemplate().find("from User_Session where user_id=? AND session_id=?", params);
        if (!list.isEmpty())
        {
            ErrModel error = new ErrModel();
            error.setMesage("Session déjà réservée");
            return error;
        }

        getHibernateTemplate().merge(user_session);
        SuccessModel success = new SuccessModel();
        success.setMesage("Session réservée avec succès");
        return success;
    }
}
