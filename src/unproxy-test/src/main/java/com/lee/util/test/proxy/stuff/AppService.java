/**
 * Project Name : hibernate-unproxy <br>
 * File Name : AppService.java <br>
 * Package Name : com.lee.util.test.proxy.stuff <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.util.test.proxy.stuff;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName : AppService <br>
 * Description : app service for test hibernateUnproxy <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com
 */
@Service
@Transactional(readOnly = true)
public class AppService {

    @PersistenceContext(unitName = "data_mgmt")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<User> getUsersByEager() {
        return em.createQuery("select distinct u from User as u left join fetch u.groups").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsersByLazy() {
        return em.createQuery("from User").getResultList();
    }
}
