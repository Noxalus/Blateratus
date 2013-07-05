package com.mti.blateratus.dao;

import com.mti.blateratus.model.Follow;
import com.mti.blateratus.model.Model;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Noxalus
 */
public class FollowDao extends HibernateDaoSupport {
    public void save(Follow follow) {
        getHibernateTemplate().save(follow);
    }
    
    public Model add(Follow follow) {
        /*
        List list = getHibernateTemplate().find("from Blater where content = ?", blater.getContent());
        if (list.size() > 0)
        {
            Error error = new Error();
            error.setMesage("Ce Blater existe déjà !");
            return error;
        }*/
        return (Follow)getHibernateTemplate().merge(follow);
    }

    public void update(Follow follow) {
        getHibernateTemplate().update(follow);
    }

    public void delete(Follow follow) {
        getHibernateTemplate().delete(follow);
    }

    public Follow find(int id) {
        List list = getHibernateTemplate().find("from Follow where id=?", id);
        if (list.isEmpty())
            return null;
        
        return (Follow)list.get(0);
    }
    
    public List<Follow> getAll(int user_id)
    {
        List list = getHibernateTemplate().find("from Follow where user_id=?", user_id);
        return list;
    }
    
    public List<Follow> getAllFromUser(int user_id)
    {
        List list = getHibernateTemplate().find("from Follow where user_id=?", user_id);
        return list;
    }
}
