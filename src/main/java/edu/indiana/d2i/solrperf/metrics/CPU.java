package edu.indiana.d2i.solrperf.metrics;

public class CPU {
	private double cpu;// pidstat %CPU
	private long currentTimeStamp;

	public CPU(double cpu, long currentTimeStamp) {
		this.cpu = cpu;
		this.currentTimeStamp = currentTimeStamp;
	}

	public double getCpu() {
		return cpu;
	}
	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}
}
