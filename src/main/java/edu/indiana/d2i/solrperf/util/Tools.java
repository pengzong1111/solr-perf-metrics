package edu.indiana.d2i.solrperf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class Tools {
	private static Logger logger = Logger.getLogger("logger");
	public static String convertTime(long t) {
		Date date = new Date(t);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss,SSS z");
		return format.format(date);
	}
	
	/**
	 * get the pid of solr instance running on host with given hostname
	 * @param hostname
	 * @return
	 */
	public static String getSolrPID(String hostname) {
		String prefix = hostname.split("\\.")[0];
		String node = Conf.getProperty(prefix);
		String path = "/home/drhtrc/solrcloud/solr-4.7.1/" + node + "/pid";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String pid = br.readLine();
			br.close();
			return pid;
		} catch (FileNotFoundException e) {
			logger.error("pid file not found for " + node, e);
			return null;
		} catch (IOException e) {
			logger.error("error reading pid file for " + node, e);
			return null;
		}
	}
	
	public static String getNetworkInterfaceName() {
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
				String name = netint.getName();
				if (name.startsWith("eth")) {
					return name;
				}
			}
		} catch (SocketException e) {
			logger.error(e);
		}

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(Tools.convertTime(System.currentTimeMillis())); 
	}
}
