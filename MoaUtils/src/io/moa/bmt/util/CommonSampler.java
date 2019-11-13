package io.moa.bmt.util;

import java.util.LinkedList;
import java.util.List;


public class CommonSampler {

	private long aggrLatency;
	private List<Long> latencyList;
	private long reqCount, success, fail;

	private long startTime;

	private long latency;

	public CommonSampler() {
		aggrLatency = 0L;
		latencyList = new LinkedList<Long>();
		success = 0L;
		fail = 0L;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void startBulk(int bulkNum) {
		startTime = System.currentTimeMillis();
	}

	private void setLatency(long latencyPerReq) {
		aggrLatency += latencyPerReq;
		latencyList.add(latencyPerReq);
	}
	
	public void endBulk(int bulkNum) {
		latency = System.currentTimeMillis() - startTime;
		success += bulkNum;
		setLatency(latency);
	}

	public void end() {
		latency = System.currentTimeMillis() - startTime;
		success += 1;
		setLatency(latency);
	}

	public void end(long count) {
		latency = System.currentTimeMillis() - startTime;
		success += count;
		setLatency(latency);
	}

	public void fail() {
		latency = System.currentTimeMillis() - startTime;
		fail += 1;
		setLatency(latency);
	}
	
	public long getStartTime() {
		return startTime;
	}

	public long getSuccessCount() {
		return success;
	}

	public long getFailCount() {
		return fail;
	}
	
	public long getTotalCount() {
		return success + fail;
	}

	public long getLantency() {
		return latency;
	}

	public long getAggrLatency() {
		return aggrLatency;
	}

	public int getAvrgLatency() {
		if(success + fail == 0)
			return 0;
		return (int) (aggrLatency / (success + fail));
	}
	
	public List<Long> getLatencyList() {
		return latencyList;
	}
}
