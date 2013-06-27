/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.model;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Noxalus
 */
@XmlRootElement(name = "user")
public class Users extends Model implements Serializable {

    private int id;
    private String hash;
    private String name;    

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int param)
    {
        id = param;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String param)
    {
        name = param;
    }
}
