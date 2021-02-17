// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import javax.management.InvalidAttributeValueException;
import javax.management.AttributeNotFoundException;
import javax.management.ReflectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import java.util.Iterator;
import javax.management.Attribute;
import javax.management.RuntimeOperationsException;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanRegistration;
import javax.management.DynamicMBean;

public abstract class AbstractDynamicMBean implements DynamicMBean, MBeanRegistration
{
    String dClassName;
    MBeanServer server;
    
    public AttributeList getAttributes(final String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributeNames.length == 0) {
            return resultList;
        }
        for (int i = 0; i < attributeNames.length; ++i) {
            try {
                final Object value = this.getAttribute(attributeNames[i]);
                resultList.add(new Attribute(attributeNames[i], value));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
    
    public AttributeList setAttributes(final AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), "Cannot invoke a setter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributes.isEmpty()) {
            return resultList;
        }
        for (final Attribute attr : attributes) {
            try {
                this.setAttribute(attr);
                final String name = attr.getName();
                final Object value = this.getAttribute(name);
                resultList.add(new Attribute(name, value));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
    
    protected abstract Logger getLogger();
    
    public void postDeregister() {
        this.getLogger().debug("postDeregister is called.");
    }
    
    public void postRegister(final Boolean registrationDone) {
    }
    
    public void preDeregister() {
        this.getLogger().debug("preDeregister called.");
    }
    
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) {
        this.getLogger().debug("preRegister called. Server=" + server + ", name=" + name);
        this.server = server;
        return name;
    }
}
