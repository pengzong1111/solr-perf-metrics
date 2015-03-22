package edu.indiana.d2i.solrperf.metrics;

public class QueryResultCache {
	private Cache cache;
	
	public QueryResultCache(int size, long lookups, long hits, float hitratio, long evictions, long currentTimeStamp) {
		cache = new Cache(size, lookups, hits, hitratio, evictions, currentTimeStamp);
	}
	
	public int getSize() {
		return cache.getSize();
	}
	public long getLookups() {
		return cache.getLookups();
	}
	public long getHits() {
		return cache.getHits();
	}
	public float getHitratio() {
		return cache.getHitratio();
	}
	public long getEvictions() {
		return cache.getEvictions();
	}
	public long getCurrentTimeStamp() {
		return cache.getCurrentTimeStamp();
	}

}
