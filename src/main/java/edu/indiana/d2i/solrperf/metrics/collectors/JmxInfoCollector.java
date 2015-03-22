package edu.indiana.d2i.solrperf.metrics.collectors;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;

import org.apache.log4j.Logger;

import edu.indiana.d2i.solrperf.jmx.JmxClient;
import edu.indiana.d2i.solrperf.metrics.Cache;
import edu.indiana.d2i.solrperf.metrics.DocumentCache;
import edu.indiana.d2i.solrperf.metrics.Memory;
import edu.indiana.d2i.solrperf.util.Conf;

/**
 * for solr, JMX info collector get heap usage, cache lookups and hit ratio for 3 built-in solr caches. 
 * @author Zong
 *
 */
public class JmxInfoCollector implements Runnable{
	private static Logger logger = Logger.getLogger("logger");
	private static Logger heapStatsLogger = Logger.getLogger("heapStatsLog");
	private static Logger filterCacheStatsLogger = Logger.getLogger("filterCacheStatsLog");
	private static Logger documentCacheStatsLogger = Logger.getLogger("documentCacheStatsLog");
	private static Logger queryResultCacheStatsLogger = Logger.getLogger("queryResultCacheStatsLog");
	
	private JmxClient jmxClient;
	
	private static final String DOCUMENT_CACHE = "solr/ocr:type=documentCache,id=org.apache.solr.search.LRUCache";
	private static final String FILTER_CACHE = "solr/ocr:type=filterCache,id=org.apache.solr.search.FastLRUCache";
	private static final String QUERY_RESULT_CACHE = "solr/ocr:type=queryResultCache,id=org.apache.solr.search.LRUCache";
	
	private static final String LOOK_UPS = "lookups";
	private static final String SIZE = "size";
	private static final String HITS = "hits";
	private static final String HIT_RATIO = "hitratio";
	private static final String EVICTIONS = "evictions";
	
	private static final String MEMORY = "java.lang:type=Memory";
	private static final String HEAP_MEMORY_USAGE = "HeapMemoryUsage";
	private static final String COMMITTED = "committed";
	private static final String USED = "used";

	//
	private static final int TIME_INTERVAL_IN_MILLISECONDS = 2000; //get metrics every 2 seconds
	public JmxInfoCollector() throws IOException {
		//currently, hard code the url. so this thread only connect to the JMX server of it local solr shard
		jmxClient = new JmxClient("service:jmx:rmi:///jndi/rmi://localhost:9001/jmxrmi");
	}
	
	public void run() {
		int sample = Integer.parseInt(Conf.getProperty("TIME_SPAN")) * 60 * 1000 / TIME_INTERVAL_IN_MILLISECONDS ; //get 300 samples with a time interval<milliseconds> between successive samples
		while (sample > 0) {
			MBeanServerConnection connection = jmxClient.getMBeanServerConnection();
			Cache docCache = getCacheStats(connection, DOCUMENT_CACHE);
			Cache filterCache = getCacheStats(connection, FILTER_CACHE);
			Cache queryResultCache = getCacheStats(connection, QUERY_RESULT_CACHE);
			Memory memory = getMemoryStats(connection);
			sample--;
			heapStatsLogger.info("heap stats:" + '\t' + memory.toString());
			queryResultCacheStatsLogger.info("query result cache stats:" + '\t' + queryResultCache.toString());
			filterCacheStatsLogger.info("filter cache stats:" + '\t' + filterCache.toString());
			documentCacheStatsLogger.info("document cache stats:" + '\t' + docCache.toString());
			try {
				Thread.sleep(TIME_INTERVAL_IN_MILLISECONDS);
			} catch (InterruptedException e) {
				logger.error("unexpectedly interrupted:", e);
				return;
			}
		}
		jmxClient.close();
	}

	private Cache getCacheStats(MBeanServerConnection connection, String cacheName){
		try {
			ObjectName cacheObject = new ObjectName(cacheName);
			long cacheLookups = (Long)connection.getAttribute(cacheObject, LOOK_UPS);
			Object cacheSizeAttr = connection.getAttribute(cacheObject, SIZE);
			int cacheSize = 0;
			if(cacheSizeAttr instanceof Integer) {
				cacheSize = (Integer) cacheSizeAttr;
			} else if(cacheSizeAttr instanceof Long) {
				cacheSize = (int)((Long) cacheSizeAttr).longValue();
			}
			long cachehits = (Long)connection.getAttribute(cacheObject, HITS);
			long cacheEvictions = (Long)connection.getAttribute(cacheObject, EVICTIONS);
			float cacheHitRatio = (Float)connection.getAttribute(cacheObject, HIT_RATIO);
			
			Cache cache = new Cache(cacheSize, cacheLookups, cachehits, cacheHitRatio, cacheEvictions, System.currentTimeMillis());
			return cache;
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Memory getMemoryStats(MBeanServerConnection connection) {
		try {
			 ObjectName memoryObject = new ObjectName(MEMORY);
			 CompositeData compositeData = (CompositeData)connection.getAttribute(memoryObject, HEAP_MEMORY_USAGE);
			// System.out.println(compositeData);
			// System.out.println("heap comitted: " + (Long)compositeData.get("committed")/1024 + "KB");
			 long committedHeapSize = (Long)compositeData.get("committed"); //bytes
			// System.out.println("heap used: " + (Long)compositeData.get("used")/1024 + "KB");
			 long usedHeapSize = (Long)compositeData.get("used");
			 Memory memory = new Memory(committedHeapSize, usedHeapSize, System.currentTimeMillis());
			 return memory;
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
