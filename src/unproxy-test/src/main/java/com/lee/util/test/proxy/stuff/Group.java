/**
 * Project Name : hibernate-unproxy <br>
 * File Name : Group.java <br>
 * Package Name : com.lee.util.test.proxy.stuff <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.util.test.proxy.stuff;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * ClassName : Group <br>
 * Description : entity of Group <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com
 */
@Entity
@Table(name = "APP_GROUP")
public class Group implements Serializable {

    private static final long serialVersionUID = -3224382495643829791L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID")
    private Integer id;

    @Column(name = "GROUP_NAME")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private List<User> users;

    @Override
    public String toString() {
        return name == null ? "empty" : name;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
