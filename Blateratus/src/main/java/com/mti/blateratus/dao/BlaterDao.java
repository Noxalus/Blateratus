/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.dao;

import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Error;
import com.mti.blateratus.model.Model;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Noxalus
 */
public class BlaterDao extends HibernateDaoSupport {
    public void save(Blater blater) {
        getHibernateTemplate().save(blater);
    }
    
    public Model add(Blater blater) {
        /*
        List list = getHibernateTemplate().find("from Blater where content = ?", blater.getContent());
        if (list.size() > 0)
        {
            Error error = new Error();
            error.setMesage("Ce Blater existe déjà !");
            return error;
        }*/
        return (Blater)getHibernateTemplate().merge(blater);
    }

    public void update(Blater blater) {
        getHibernateTemplate().update(blater);
    }

    public void delete(Blater blater) {
        getHibernateTemplate().delete(blater);
    }

    public Blater find(int id) {
        List list = getHibernateTemplate().find("from Blater where id=?", id);
        if (list.isEmpty())
            return null;
        
        return (Blater)list.get(0);
    }
    
    public List<Blater> getAll(int user_id)
    {
        List list = getHibernateTemplate().find("from Blater where user_id=?", user_id);
        return list;
    }
    
    public List<Blater> getAllFromUser(int user_id)
    {
        List list = getHibernateTemplate().find("from Blater where user_id=?", user_id);
        return list;
    }
}
