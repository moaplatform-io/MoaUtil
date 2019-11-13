package io.moa.bmt;

public class ProgressContext {

	public long lastAggrCount;
	public float lastAggrLatency;
	
	public ProgressContext() {
		lastAggrCount = 0;
		lastAggrLatency = 0;
	}
}
