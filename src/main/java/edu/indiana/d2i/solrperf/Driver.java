package edu.indiana.d2i.solrperf;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.indiana.d2i.solrperf.metrics.collectors.BandwidthUsageCollector;
import edu.indiana.d2i.solrperf.metrics.collectors.CpuUsageCollector;
import edu.indiana.d2i.solrperf.metrics.collectors.JmxInfoCollector;

public class Driver {

	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("log4j.configuration", new File("conf/log4j.properties").toURI().toURL().toString());
		
		System.out.println("set log4j.configuration");
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		
		executorService.execute(new CpuUsageCollector());
		executorService.execute(new JmxInfoCollector());
		executorService.execute(new BandwidthUsageCollector());
		
		System.out.println("waiting for metric collectors...");
		executorService.shutdown();
		executorService.awaitTermination(15, TimeUnit.MINUTES);
		System.out.println("metric collectors are done!");
		
	}

}
