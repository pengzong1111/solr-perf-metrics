package edu.indiana.d2i.solrperf.metrics;

import edu.indiana.d2i.solrperf.util.Tools;

public class Memory {
	private long heapComitted;
	private long heapUsed;
	private long currentTimeStamp;
	
	public Memory(long heapComitted, long heapUsed, long currentTimeStamp) {
		this.heapComitted = heapComitted;
		this.heapUsed = heapUsed;
		this.currentTimeStamp = currentTimeStamp;
	}
	
	public long getHeapComitted() {
		return heapComitted;
	}

	public long getHeapUsed() {
		return heapUsed;
	}

	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// divide by 1024 to make MB as unit. <committed	used	time>
		sb.append(heapComitted/1024).append('\t').append(heapUsed/1024).append('\t').append(Tools.convertTime(currentTimeStamp));
		return sb.toString();
	}
}
