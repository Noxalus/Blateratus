package com.mti.blateratus.bo;

import com.mti.blateratus.dao.ReblaterDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Reblater;
import java.util.List;

/**
 *
 * @author Noxalus
 */
public class ReblaterBo {
    private ReblaterDao reblaterDao;
    
    public void setReblaterDao(ReblaterDao dao)
    {
        reblaterDao = dao;
    }
    
    public ReblaterDao getReblaterDao()
    {
        return reblaterDao;
    }
    
    public void save(Reblater reblater) {
        reblaterDao.save(reblater);
    }
    
    public Model add(Reblater reblater) {
        return reblaterDao.add(reblater);
    }

    public void update(Reblater reblater) {
        reblaterDao.update(reblater);
    }

    public void delete(Reblater reblater) {
        reblaterDao.delete(reblater);
    }

    public Reblater find(int id) {
        return reblaterDao.find(id);
    }
    
    public List<Reblater> getAll(int user_id) {
        return reblaterDao.getAll(user_id);
    }
}
