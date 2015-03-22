package edu.indiana.d2i.solrperf.metrics;

public class Cache {
	private int size;
	private long lookups;
	private long hits;
	private float hitratio;
	private long evictions;
	private long currentTimeStamp;
	
	public Cache(int size, long lookups, long hits, float hitratio, long evictions, long currentTimeStamp) {
		this.size = size;
		this.lookups = lookups;
		this.hits = hits;
		this.hitratio = hitratio;
		this.evictions = evictions;
		this.currentTimeStamp = currentTimeStamp;
	}
	
	public int getSize() {
		return size;
	}
	public long getLookups() {
		return lookups;
	}
	public long getHits() {
		return hits;
	}
	public float getHitratio() {
		return hitratio;
	}
	public long getEvictions() {
		return evictions;
	}
	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		//<size	lookups	hits	hitratio	evictions	timstamp>
		sb.append(size).append('\t').append(lookups).append('\t').append(hits).append('\t').append(hitratio).append('\t').append(evictions).append('\t').append(currentTimeStamp);
		return sb.toString();
		
	}
}
