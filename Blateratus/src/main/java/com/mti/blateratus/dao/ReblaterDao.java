package com.mti.blateratus.dao;

import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Reblater;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Noxalus
 */
public class ReblaterDao extends HibernateDaoSupport {
    public void save(Reblater reblater) {
        getHibernateTemplate().save(reblater);
    }
    
    public Model add(Reblater reblater) {
        /*
        List list = getHibernateTemplate().find("from Blater where content = ?", blater.getContent());
        if (list.size() > 0)
        {
            Error error = new Error();
            error.setMesage("Ce Blater existe déjà !");
            return error;
        }*/
        return (Reblater)getHibernateTemplate().merge(reblater);
    }

    public void update(Reblater reblater) {
        getHibernateTemplate().update(reblater);
    }

    public void delete(Reblater reblater) {
        getHibernateTemplate().delete(reblater);
    }

    public Reblater find(int id) {
        List list = getHibernateTemplate().find("from Reblater where id=?", id);
        if (list.isEmpty())
            return null;
        
        return (Reblater)list.get(0);
    }
    
    public List<Reblater> getAll(int user_id)
    {
        List list = getHibernateTemplate().find("from Reblater where user_id=?", user_id);
        return list;
    }
    
    public List<Reblater> getAllFromUser(int user_id)
    {
        List list = getHibernateTemplate().find("from Reblater where user_id=?", user_id);
        return list;
    }
}
