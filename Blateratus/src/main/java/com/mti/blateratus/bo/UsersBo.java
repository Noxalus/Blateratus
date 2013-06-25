/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.bo;

import com.mti.blateratus.dao.UsersDao;
import com.mti.blateratus.model.Model;
import com.mti.blateratus.model.Users;

/**
 *
 * @author Noxalus
 */
public class UsersBo {

    private UsersDao usersDao;
    
    public void setUsersDao(UsersDao dao)
    {
        usersDao = dao;
    }
    
    public UsersDao getUsersDao()
    {
        return usersDao;
    }
    
    public void save(Users user) {
        usersDao.save(user);
    }

    public void update(Users user) {
        usersDao.update(user);
    }

    public void delete(Users user) {
        usersDao.delete(user);
    }

    public Users find(int id) {
        return usersDao.find(id);
    }
    
    public Model add(Users user) {
        return usersDao.add(user);
    }
    
    public Model connect(String name, String password)
    {
        return usersDao.connect(name, password);
    }
    
    public Users findByToken(String token)
    {
        return usersDao.findByToken(token);
    }
}
