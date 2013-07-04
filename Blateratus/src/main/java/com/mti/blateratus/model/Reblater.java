package com.mti.blateratus.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Noxalus
 */
@XmlRootElement(name = "reblater")
public class Reblater extends Model implements Serializable {
    private int id;
    private int user_id;
    private int blater_id;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    
    public int getBlater_id() {
        return this.blater_id;
    }

    public void setBlater_id(int blater_id) {
        this.blater_id = blater_id;
    }
    
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}