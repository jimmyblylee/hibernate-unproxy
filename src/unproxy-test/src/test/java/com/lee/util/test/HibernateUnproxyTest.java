/**
 * Project Name : hibernate-unproxy <br>
 * File Name : HibernateUnproxyTest.java <br>
 * Package Name : com.lee.util.test <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.util.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lee.util.HibernateUnproxy;
import com.lee.util.test.proxy.stuff.AppService;
import com.lee.util.test.proxy.stuff.Group;
import com.lee.util.test.proxy.stuff.User;

/**
 * ClassName : HibernateUnproxyTest <br>
 * Description : unit test for HibernateUnproxy <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-context.xml" })
public class HibernateUnproxyTest {

    @Resource
    private AppService service;

    @Test
    public void testLostSession() {
        List<User> users = service.getUsersByLazy();
        assertThat(users, notNullValue());
        assertThat(users.size(), greaterThan(0));

        try {
            User user = users.get(0);
            assertThat(user, notNullValue());
            List<Group> groups = user.getGroups();
            groups.size();
        } catch (Exception e) {
            assertThat(e, instanceOf(LazyInitializationException.class));
        }
    }

    @Test
    public void testUnproxyObject() {
        List<User> users = service.getUsersByLazy();
        assertThat(users, notNullValue());
        assertThat(users.size(), greaterThan(0));
        User user = users.get(0);
        try {
            HibernateUnproxy.unproxy(user);
        } catch (IntrospectionException e) {
            fail();
        }
        try {
            assertThat(user.getGroups(), nullValue());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUnproxyCollection() {
        List<User> users = service.getUsersByLazy();
        assertThat(users, notNullValue());
        assertThat(users.size(), greaterThan(0));
        try {
            HibernateUnproxy.unproxy(users);
        } catch (IntrospectionException e) {
            fail();
        }
        User user = users.get(0);
        try {
            assertThat(user.getGroups(), nullValue());
        } catch (Exception e) {
            fail();
        }
    }
}
