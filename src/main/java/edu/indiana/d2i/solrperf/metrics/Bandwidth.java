package edu.indiana.d2i.solrperf.metrics;

public class Bandwidth {
	private double sent;
	private double received; // KB/sec
	private long currentTimeStamp;
	
	public Bandwidth(double sent, double received, long currentTimeStamp) {
		this.sent = sent;
		this.received = received;
		this.currentTimeStamp = currentTimeStamp;
	}
	
	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	public double getSent() {
		return sent;
	}
	public double getReceived() {
		return received;
	}
}
