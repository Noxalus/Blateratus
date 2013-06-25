/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.bo;

import com.mti.blateratus.dao.BlaterDao;
import com.mti.blateratus.model.Blater;
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

    public void update(Blater blater) {
        blatersDao.update(blater);
    }

    public void delete(Blater blater) {
        blatersDao.delete(blater);
    }

    public Blater find(int id) {
        return blatersDao.find(id);
    }
    
    public List<Blater> getAll() {
        return blatersDao.getAll();
    }
}
