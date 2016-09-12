/**
 * Project Name : hibernate-unproxy <br>
 * File Name : User.java <br>
 * Package Name : com.lee.util.test.proxy.stuff <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.util.test.proxy.stuff;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * ClassName : User <br>
 * Description : entity of user <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com
 */
@Entity
@Table(name = "APP_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 5072007432585946626L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer id;

    @Column(name = "USER_NAME")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "APP_GROUP_USER", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) })
    private List<Group> groups;

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
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
