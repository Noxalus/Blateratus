/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.dao;

import com.mti.blateratus.model.ErrModel;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.ErrModel;
import com.mti.blateratus.model.Users;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 *
 * @author Noxalus
 */
public class UsersDao extends HibernateDaoSupport {

    public void save(Users user) {
        getHibernateTemplate().save(user);
    }

    public void update(Users user) {
        getHibernateTemplate().update(user);
    }

    public void delete(Users user) {
        getHibernateTemplate().delete(user);
    }

    public Users find(int id) {
        List list = getHibernateTemplate().find("from Users where id=?", id);
        return (Users)list.get(0);
    }

    public Users findByToken(String token)
    {
        List list = getHibernateTemplate().find("from Users where token=?", token);
        if (list.isEmpty())
            return null;
        else
            return (Users)list.get(0);
    }
    
    public Model add(Users user) {
        List list = getHibernateTemplate().find("from Users where name = ?", user.getName());
        if (list.size() > 0)
        {
            ErrModel error = new ErrModel();
            error.setMesage("Cet utilisateur existe déjà");
            return error;
        }
        return (Users)getHibernateTemplate().merge(user);
    }
    
    public Model connect(String name, String password)
    {
        String[] params = new String[] { name, password };
        List list = getHibernateTemplate().find("from Users where name = ? AND hash = ?", params);
        if (list.isEmpty())
        {
            ErrModel error = new ErrModel();
            error.setMesage("Mauvais login ou de mot de passe");
            return error;            
        }
        return (Users)list.get(0);
    }
}
