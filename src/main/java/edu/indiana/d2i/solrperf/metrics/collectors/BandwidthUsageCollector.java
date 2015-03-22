package edu.indiana.d2i.solrperf.metrics.collectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import edu.indiana.d2i.solrperf.util.Conf;
import edu.indiana.d2i.solrperf.util.Tools;

/**
 * in HTRC environment, index file is read from Network attached filesystem. So
 * minitoring eth0 traffic is the key instead of real disk IO
 * 
 * @author Zong
 * 
 */
public class BandwidthUsageCollector implements Runnable{
	private static Logger logger = Logger.getLogger("logger");
	private static Logger bandwidthLogger = Logger.getLogger("bandwidthLog");
	private static final String PWD = "itismine";

	public void run() {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append("echo ").append(PWD).append(" | sudo -S ").append(Conf.getProperty("NETHOGS_PATH")).append("/nethogs -d 2 -t ").append(Tools.getNetworkInterfaceName());;
		
		// time span in minutes * 60 / 2<time interval in seconds>
		int loop = Integer.parseInt(Conf.getProperty("TIME_SPAN")) * 60 / 2;
		String cmd = cmdBuilder.toString();
		
		try {
			Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", cmd});
			logger.info("...running " + cmd);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String outputLine = null;
			while((outputLine=br.readLine()) != null && loop > 0) {
				if(outputLine.contains("unknown TCP/0/0")) {
					bandwidthLogger.info(outputLine);
					loop --;
				}
			}
			process.destroy();
			br.close();
		} catch (IOException e) {
			logger.error("error running " + cmd, e);
		}
	}
}
