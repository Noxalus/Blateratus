package com.mti.blateratus.bo;

import com.mti.blateratus.dao.BlaterDao;
import com.mti.blateratus.model.Blater;
import com.mti.blateratus.model.Model;
import java.util.List;

/**
 *
 * @author Noxalus
 */
public class BlaterBo {
    private BlaterDao blatersDao;
    
    public void setBlaterDao(BlaterDao dao)
    {
        blatersDao = dao;
    }
    
    public BlaterDao getBlaterDao()
    {
        return blatersDao;
    }
    
    public void save(Blater blater) {
        blatersDao.save(blater);
    }
    
    public Model add(Blater blater) {
        return blatersDao.add(blater);
    }

    public void update(Blater blater) {
        blatersDao.update(blater);
    }

    public void delete(Blater blater) {
        blatersDao.delete(blater);
    }

    public Blater find(int id) {
        return blatersDao.find(id);
    }
    
    public List<Blater> getAll(int user_id) {
        return blatersDao.getAll(user_id);
    }
}
