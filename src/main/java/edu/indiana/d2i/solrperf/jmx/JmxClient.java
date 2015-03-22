package edu.indiana.d2i.solrperf.jmx;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

public class JmxClient {
	private static Logger logger = Logger.getLogger("logger");
	private JMXConnector connector;
	private MBeanServerConnection connection;
	//private String jmxServiceURLStr;
	public JmxClient(String jmxServiceURLStr) throws IOException {
		 //this.jmxServiceURLStr = jmxServiceURLStr;
		 JMXServiceURL url = new JMXServiceURL(jmxServiceURLStr);
		 this.connector = JMXConnectorFactory.connect(url);
		 this.connection = connector.getMBeanServerConnection();
	}
	
	public MBeanServerConnection getMBeanServerConnection() {
		return connection;
	}
	
	
	public void close(){
		try {
			connector.close();
		} catch (IOException e) {
			logger.error("JMXConnector is wrong with closing: ", e);
		}
	}
	
	public static void main(String[] args) throws IOException, MalformedObjectNameException, InstanceNotFoundException, MBeanException, ReflectionException, IntrospectionException, AttributeNotFoundException {
		 JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9001/jmxrmi");
		 JMXConnector conn = JMXConnectorFactory.connect(url);
		 MBeanServerConnection mbsc = conn.getMBeanServerConnection();
		 ObjectName documentCacheObject = new ObjectName("solr/collection1:type=documentCache,id=org.apache.solr.search.LRUCache");
		// cacheObject.
	
		 System.out.println("lookups: " + mbsc.getAttribute(documentCacheObject, "lookups"));
		 System.out.println("hitratio: " + mbsc.getAttribute(documentCacheObject, "hitratio"));
		 
		 ObjectName memoryObject = new ObjectName("java.lang:type=Memory");
		 CompositeData compositeData = (CompositeData)mbsc.getAttribute(memoryObject, "HeapMemoryUsage");
		 System.out.println(compositeData);
		 System.out.println("heap comitted: " + (Long)compositeData.get("committed")/1024 + "KB");
		 System.out.println("heap used: " + (Long)compositeData.get("used")/1024 + "KB");
		 
		 conn.close();
		 /* MBeanInfo distInfo = mbsc.getMBeanInfo(cacheObject);
		 String description = distInfo.getDescription();
		 System.out.println("description: " + description);
		 MBeanAttributeInfo[] attrs = distInfo.getAttributes();
		 for(MBeanAttributeInfo mBeanAttributeInfo : attrs) {
				System.out.println(mBeanAttributeInfo);
				//mBeanAttributeInfo.
		 }
		 MBeanConstructorInfo[] ctors = distInfo.getConstructors();
		 MBeanNotificationInfo[] nots = distInfo.getNotifications();
		 MBeanOperationInfo[] opers = distInfo.getOperations();*/
	}
}
