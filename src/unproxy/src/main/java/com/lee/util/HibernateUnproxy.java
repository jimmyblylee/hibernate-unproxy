/**
 * Project Name : hibernate-unproxy <br>
 * File Name : HibernateUnproxy.java <br>
 * Package Name : com.lee.util <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.SerializableProxy;

/**
 * ClassName : HibernateUnproxy <br>
 * Description : Strip the proxy of entities managed by hibernate session, in case of session lost exception while fetching lazy fields with no session <br>
 * Create Time : 2016-09-12 <br>
 * Create by : jimmyblylee@126.com
 */
public class HibernateUnproxy {


    /**
     * Description : Strip the proxy of target entity or collections of entity<br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param <T> any Object
     * @param object
     *            the object with proxies which you want to strip all hibernate proxies
     * @return the object without any proxy
     * @throws IntrospectionException
     *             if an exception occurs during introspection.
     */
    public static <T> T unproxy(T object) throws IntrospectionException {
        T result = unproxyObject(object);
        unproxySubProperties(result, new ArrayList<Object>());
        return result;
    }

    /**
     * Description : strip the proxy of the sub-properties in the target object <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param object
     *            the object which maybe contain sub-properties managed by hibernate proxy
     * @param handledObjects
     *            objects container which sub-property is dealed
     * @throws IntrospectionException
     *             if an exception occurs during introspection.
     */
    private static void unproxySubProperties(Object object, ArrayList<Object> handledObjects) throws IntrospectionException {
        if ((object != null) && (!isProxy(object)) && !containsTotallyEqual(handledObjects, object)) {
            handledObjects.add(object);
            if (object instanceof Iterable) {
                for (Object item : (Iterable<?>) object) {
                    unproxySubProperties(item, handledObjects);
                }
            } else if (object.getClass().isArray()) {
                for (Object item : (Object[]) object) {
                    unproxySubProperties(item, handledObjects);
                }
            }
            BeanInfo beanInfo = null;
            beanInfo = Introspector.getBeanInfo(object.getClass());
            if (beanInfo != null) {
                for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                    try {
                        if ((property.getWriteMethod() != null) && (property.getReadMethod() != null)) {
                            Object fieldValue = property.getReadMethod().invoke(object);
                            if (isProxy(fieldValue)) {
                                fieldValue = unproxyObject(fieldValue);
                                property.getWriteMethod().invoke(object, fieldValue);
                            }
                            unproxySubProperties(fieldValue, handledObjects);
                        }
                    } catch (Exception e) {
                        IntrospectionException ex = new IntrospectionException("***** Get property writer() or reader() exception! *****");
                        ex.addSuppressed(e);
                        throw ex;
                    }
                }
            }
        }
    }

    /**
     * Description : strip proxy from hibernate proxy <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param object
     *            the object which may be managed by hibernate proxy
     * @return the original object
     */
    @SuppressWarnings("unchecked")
    private static <T> T unproxyObject(T object) {
        if (isProxy(object)) {
            if (object instanceof PersistentCollection) {
                PersistentCollection persistentCollection = (PersistentCollection) object;
                return (T) unproxyPersistentCollection(persistentCollection);
            } else if (object instanceof HibernateProxy) {
                HibernateProxy hibernateProxy = (HibernateProxy) object;
                return (T) unproxyHibernateProxy(hibernateProxy);
            } else {
                return null;
            }
        }
        return object;
    }

    /**
     * Description : strip proxy from HibernateProxy <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param hibernateProxy
     *            object wrap up with HibernateProxy
     * @return the original object
     */
    private static Object unproxyHibernateProxy(HibernateProxy hibernateProxy) {
        Object result = hibernateProxy.writeReplace();
        if (!(result instanceof SerializableProxy)) {
            return result;
        }
        return null;
    }

    /**
     * Description : strip proxy from PersistentCollection <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param persistentCollection
     *            object wrap up with persistentCollection
     * @return collection of the original object
     */
    private static Object unproxyPersistentCollection(PersistentCollection persistentCollection) {
        if (persistentCollection instanceof PersistentSet) {
            return persistentCollection.getStoredSnapshot() == null ? null : unproxyPersistentSet((Map<?, ?>) persistentCollection.getStoredSnapshot());
        }
        return persistentCollection.getStoredSnapshot();
    }

    /**
     * Description : strip proxy from PersistentSet <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param persistenceSet
     * @return list of the original object
     */
    private static <T> Set<T> unproxyPersistentSet(Map<T, ?> persistenceSet) {
        return new LinkedHashSet<T>(persistenceSet.keySet());
    }

    /**
     * Description : validate whether the target object is in the collection exactly <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param collection
     *            target collection
     * @param object
     *            target object
     * @return true if the object is in the collection exactly
     */
    private static boolean containsTotallyEqual(Collection<?> collection, Object object) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        for (Object item : collection) {
            if (item == object) {
                return true;
            }
        }
        return false;
    }

    /**
     * Description : validate whether the object is managed by hibernate proxy <br>
     * Create Time: Sep 7, 2016 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param object
     *            the case to validate
     * @return true if the object is a not null object which is managed by hibernate proxy
     */
    private static boolean isProxy(Object object) {
        return object != null && (object instanceof HibernateProxy || object instanceof PersistentCollection);
    }
}
