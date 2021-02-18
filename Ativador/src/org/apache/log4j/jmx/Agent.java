// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import com.sun.jdmk.comm.CommunicatorServer;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import com.sun.jdmk.comm.HtmlAdaptorServer;
import javax.management.MBeanServerFactory;
import org.apache.log4j.Logger;

public class Agent
{
    static Logger log;
    
    public void start() {
        final MBeanServer server = MBeanServerFactory.createMBeanServer();
        final HtmlAdaptorServer html = new HtmlAdaptorServer();
        try {
            Agent.log.info("Registering HtmlAdaptorServer instance.");
            server.registerMBean(html, new ObjectName("Adaptor:name=html,port=8082"));
            Agent.log.info("Registering HierarchyDynamicMBean instance.");
            final HierarchyDynamicMBean hdm = new HierarchyDynamicMBean();
            server.registerMBean(hdm, new ObjectName("log4j:hiearchy=default"));
        }
        catch (Exception e) {
            Agent.log.error("Problem while regitering MBeans instances.", e);
            return;
        }
        ((CommunicatorServer)html).start();
    }
    
    static {
        Agent.log = Logger.getLogger(Agent.class);
    }
}
