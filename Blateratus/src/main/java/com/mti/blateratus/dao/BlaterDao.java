/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.dao;

import com.mti.blateratus.model.Blater;
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

    public void update(Blater blater) {
        getHibernateTemplate().update(blater);
    }

    public void delete(Blater blater) {
        getHibernateTemplate().delete(blater);
    }

    public Blater find(int id) {
        List list = getHibernateTemplate().find("from Blater where id=?", id);
        return (Blater)list.get(0);
    }
    
    public List<Blater> getAll()
    {
        List list = getHibernateTemplate().find("from blaters");
        for (int i = 0; i < list.size(); i++)
        {
            final int id = ((Blater)list.get(i)).getId();
            List tmp_list = (List)getHibernateTemplate().execute(new HibernateCallback()
            {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createSQLQuery("SELECT blaters.* FROM blaters");
                    List list = query.list();
                    return list;
                }
            });
            
            ArrayList<Blater> blaters = new ArrayList<Blater>();
            for (int j = 0; j < tmp_list.size(); j++)
            {
                Object[] row = (Object[]) tmp_list.get(j);
                Blater blater = new Blater();
                blater.setId((Integer)row[0]);
                // TODO

                blaters.add(blater);
            }
            //((Blater)(list.get(i))).setBlater(blater);
        }
        
        return list;
    }
}
