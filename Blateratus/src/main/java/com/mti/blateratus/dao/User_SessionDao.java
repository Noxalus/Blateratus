/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.dao;

import com.mti.blateratus.model.Error;
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
    
    public User_Session findByUserId(int user_id)
    {
        List list = getHibernateTemplate().find("from User_Session where user_id=?", user_id);
        if (list.isEmpty())
            return null;
        else
            return (User_Session)list.get(0);
    }
    
    public User_Session findByToken(String token)
    {
        List list = getHibernateTemplate().find("from User_Session where token=?", token);
        if (list.isEmpty())
            return null;
        else
            return (User_Session)list.get(0);
    }
    
    public Model Add(User_Session user_session)
    {
        return (User_Session)getHibernateTemplate().merge(user_session);
    }
}
