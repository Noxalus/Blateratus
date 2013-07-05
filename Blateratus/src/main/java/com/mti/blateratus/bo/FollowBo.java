package com.mti.blateratus.bo;

import com.mti.blateratus.dao.FollowDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Follow;
import java.util.List;

/**
 *
 * @author Noxalus
 */
public class FollowBo {
    private FollowDao followDao;
    
    public void setReblaterDao(FollowDao dao)
    {
        followDao = dao;
    }
    
    public FollowDao getReblaterDao()
    {
        return followDao;
    }
    
    public void save(Follow follow) {
        followDao.save(follow);
    }
    
    public Model add(Follow follow) {
        return followDao.add(follow);
    }

    public void update(Follow follow) {
        followDao.update(follow);
    }

    public void delete(Follow follow) {
        followDao.delete(follow);
    }

    public Follow find(int id) {
        return followDao.find(id);
    }
    
    public List<Follow> getAll(int user_id) {
        return followDao.getAll(user_id);
    }
}
