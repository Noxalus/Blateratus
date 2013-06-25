/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mti.blateratus.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Noxalus
 */
@XmlRootElement(name = "Error")
public class ErrModel extends Model implements Serializable {
    private String message;
    
    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMesage(String message) {
        this.message = message;
    }
}
