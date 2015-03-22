package edu.indiana.d2i.solrperf.metrics.collectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import edu.indiana.d2i.solrperf.util.Conf;
import edu.indiana.d2i.solrperf.util.Tools;

public class CpuUsageCollector implements Runnable{
	private static Logger logger = Logger.getLogger("logger");
	private static Logger cpuLogger = Logger.getLogger("cpuLog");

	private static final int TIME_INTERVAL_IN_SEC = 2;
	//private static final int SAMPLES = 300;
	public void run() {
		String pid = Tools.getSolrPID(Conf.getProperty("HOSTNAME"));
		StringBuilder cmdBuilder = new StringBuilder();
		int sample = Integer.parseInt(Conf.getProperty("TIME_SPAN")) * 60 / TIME_INTERVAL_IN_SEC;
		cmdBuilder.append("pidstat -u -p ").append(pid).append(' ').append(TIME_INTERVAL_IN_SEC).append(' ').append(sample);
		String cmd = cmdBuilder.toString();
		try {
			Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", cmd});
			logger.info("...running command: " + cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String outputLine = null;
			while((outputLine=br.readLine()) != null) {
				cpuLogger.info(outputLine.replaceAll("\\s\\s+", "	"));
			}
			br.close();
		} catch (IOException e) {
			logger.error("error reading output of " + cmd, e);
		}
	}
	
	public static void main(String[] args) {
		String outputLine = "04:33:29 PM     31115    0.00    0.00    0.00    0.00     3  java";
		System.out.println(outputLine);
		System.out.println(outputLine.replaceAll("\\s\\s+", "	"));
	}
}
