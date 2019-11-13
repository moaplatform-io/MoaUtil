package io.moa.bmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.moa.bmt.AbstractBmtThread;
import io.moa.bmt.util.CommonSampler;
import io.moa.bmt.util.ConfigManager;

public abstract class RunnerContext<E, R> {

	private static final Logger log = LoggerFactory.getLogger(RunnerContext.class);
	private Map<String, List<AbstractBmtThread<E, R>>> threadListMap = new HashMap<String, List<AbstractBmtThread<E, R>>>();
	private Map<String, ProgressContext> threadProgressMap = new HashMap<String, ProgressContext>();

	protected R resource;
	protected Map<String, ConcurrentLinkedQueue<E>> messageQueues;
	protected ConfigManager confManager;
	protected Map<String, Integer> throttlingTpsMap = new HashMap<String, Integer>();
	
	public RunnerContext(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Invalid Number of Arguments: " + args.length);
			System.err.println("Usage: java {class_name} {property_file_path}");

			throw new IOException("Invalid Number of Arguments: " + args.length);
		}

		// load configuration
		this.confManager = new ConfigManager(args[0]);
		
		initVariables();
		checkVariables();
		initResource();
		initThread();
	}

	public abstract void initVariables() throws Exception;

	private void checkVariables() throws Exception {
		if (resource == null) {
			throw new NullPointerException("RunnerContext.resource is null. You must initialize it at initVariables()");
		}
		if (messageQueues == null) {
			throw new NullPointerException("RunnerContext.resource is null. You must initialize it at initVariables()");
		}
	}

	public abstract void initResource() throws Exception;

	public abstract void initThread() throws Exception;

	public ConfigManager getConfMng() {
		return this.confManager;
	}

	public void addThread(AbstractBmtThread<E, R> thread) {
		if (threadListMap.get(thread.getTypeName()) == null) {
			threadListMap.put(thread.getTypeName(), new LinkedList<AbstractBmtThread<E, R>>());
			threadProgressMap.put(thread.getTypeName(), new ProgressContext());
		}

		threadListMap.get(thread.getTypeName()).add(thread);
	}

	public void start() {
		for (List<AbstractBmtThread<E, R>> threads : threadListMap.values()) {
			for (AbstractBmtThread<E, R> thread : threads) {
				thread.startWithCount();
			}
		}
	}

	public void stop() throws InterruptedException {
		for (List<AbstractBmtThread<E, R>> threads : threadListMap.values()) {
			for (AbstractBmtThread<E, R> thread : threads) {
				thread.terminateAndJoin();
			}
		}
	}

	public boolean isAllFinished() {
		for (List<AbstractBmtThread<E, R>> threads : threadListMap.values()) {
			for (AbstractBmtThread<E, R> thread : threads) {
				if (thread.isAlive()) {
					return false;
				}
			}
		}
		return true;
	}

	public Map<String, String> summary(int printSimple) {
		Map<String, String> retMap = new HashMap<String, String>();
		for (Map.Entry<String, List<AbstractBmtThread<E, R>>> threads : threadListMap.entrySet()) {
			retMap.put(threads.getKey(), 
					genSummary(threads.getKey(), threads.getValue(), printSimple));
		}

		return retMap;
	}

	public R getResource() {
		return this.resource;
	}

	public Map<String, ConcurrentLinkedQueue<E>> getQueues() {
		return this.messageQueues;
	}
	
	public Map<String, Integer> getThrottlingMap() {
		return throttlingTpsMap;
	}

	public static Logger logger() {
		return log;
	}

	public static final int HEADER_MSG_LEGNTH = 46;

	public Map<String, String> progress() {
		
		Map<String, String> retMap = new HashMap<String, String>();
		for (Map.Entry<String, List<AbstractBmtThread<E, R>>> threads : threadListMap.entrySet()) {
			
			retMap.put(threads.getKey(),
					genProgress(threads.getKey(), threads.getValue()));
		}

		return retMap;
	}
	
	// return string size is 16
	private String genProgress(String threadTypeName, List<AbstractBmtThread<E, R>> threadList) {
		ProgressContext pContext =  threadProgressMap.get(threadTypeName);
				 
		// print status
		long aggrCount = 0L;
		long aggrLatency = 0L;
		for (int i = 0; i < threadList.size(); i++) {
			aggrCount += threadList.get(i).getSampler().getTotalCount();
			aggrLatency += threadList.get(i).getSampler().getAggrLatency();
		}
		
		int count = (int) (aggrCount - pContext.lastAggrCount);
		float latency = (count != 0)?((aggrLatency - pContext.lastAggrLatency) / (float) count) :0;
		
		pContext.lastAggrCount = aggrCount; 
		pContext.lastAggrLatency = aggrLatency;
		
		return String.format("%8d%8.1f", count, latency);
	}
	
	private String genSummary(String threadTypeName, List<AbstractBmtThread<E, R>> threadList, int printSimple) {

		if (threadList.size() == 0)
			return "";

		// calculate running info
		long avrgLatency = 0;
		long totalReqCount = 0, success = 0L, fail = 0L;
		long totalAggrTime = 0L;
		long totalAggrLatency = 0L;

		StringBuffer strBuf = new StringBuffer();

		List<List<Long>> latencyListList = new LinkedList<List<Long>>();

		for (int i = 0; i < threadList.size(); i++) {
			CommonSampler sampler = threadList.get(i).getSampler();

			latencyListList.add(sampler.getLatencyList());

			avrgLatency += sampler.getAvrgLatency();
			totalReqCount += sampler.getTotalCount();
			success += sampler.getSuccessCount();
			fail += sampler.getFailCount();

			totalAggrLatency += sampler.getAggrLatency();
			totalAggrTime += threadList.get(i).getWorkingTime();
		}

		PriorityQueue<Long> pqueue = new PriorityQueue<Long>(Collections.reverseOrder());
		for (List<Long> latencyList : latencyListList) {
			pqueue.addAll(latencyList);
			long queueSize = pqueue.size();
			while (queueSize > 1 && queueSize > totalReqCount * 0.99) {
				queueSize--;
				pqueue.poll();
			}
		}
		long Percentile99Latency = 0;
		if (false == pqueue.isEmpty())
			Percentile99Latency = pqueue.poll();

		if (printSimple == 0) {
			// print status/summary info
			int separater_num = (HEADER_MSG_LEGNTH - threadTypeName.length()) / 2 - 1;
			for (int i = 0; i < separater_num; i++)
				strBuf.append("=");
			strBuf.append(" " + threadTypeName + " ");
			for (int i = 0; i < separater_num; i++)
				strBuf.append("=");

			strBuf.append(String.format("\n| Total Running Time per Thread: %7d ms |\n", totalAggrTime / threadList.size()));
			strBuf.append(
					String.format("| Real Working Time per Thread: %8d ms |\n", totalAggrLatency / threadList.size()));
			if (totalReqCount != 0)
				strBuf.append(String.format("| Total Req. (Missing %%): %10d (%3d%%) |\n", totalReqCount,
						(fail * 100 / totalReqCount)));
			if (totalAggrTime != 0)
				strBuf.append(String.format("| Througput: %24d req/s |\n",
						(success * 1000 / (totalAggrTime / threadList.size()))));
			strBuf.append(String.format("| Avrg. Latency: %23d ms |\n", avrgLatency / threadList.size()));
			strBuf.append(String.format("| 99 Per Latency: %22d ms |\n", Percentile99Latency));
		} else {
			// throughput / total request num / fail request num / avrg latency
			// / 99 per latency
			strBuf.append(String.format("%d/%d/%d/%d/%d", (success * 1000 / (totalAggrTime / threadList.size())),
					totalReqCount, fail, (avrgLatency / threadList.size()), Percentile99Latency));
		}
		return strBuf.toString();
	}
}
