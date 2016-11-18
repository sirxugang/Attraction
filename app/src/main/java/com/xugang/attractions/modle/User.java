package com.xugang.attractions.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ASUS on 2016-10-22.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String password;
    private String attrname;

    @Generated(hash = 247388833)
    public User(Long id, String name, String password, String attrname) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.attrname = attrname;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAttrname() {
        return this.attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    @Override
    public String toString() {
        return "User{" +
                "attrname='" + attrname + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
