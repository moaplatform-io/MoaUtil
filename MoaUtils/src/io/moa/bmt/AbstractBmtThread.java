package io.moa.bmt;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.moa.bmt.util.CommonSampler;
import io.moa.bmt.util.ConfigManager;

public abstract class AbstractBmtThread<E, R> extends Thread {
	private static final Logger log = LoggerFactory.getLogger(AbstractBmtThread.class);

	Map<String, ConcurrentLinkedQueue<E>> messageQueues;
	R resource;

	CommonSampler sampler;
	ConfigManager confManager;
	
	long throttlingStartT = 0;
	long preTotalCount = 0;
	int throttlingTargetTPS = 0;

	// about thread working
	boolean isTerminate;
	long startTime = -1;
	long endTime = -1;

	public AbstractBmtThread(RunnerContext<E, R> context) {
		this.sampler = new CommonSampler();
		this.isTerminate = false;
		this.confManager = context.getConfMng();
		this.resource = context.getResource();
		this.messageQueues = context.getQueues();
		
		if(context.getThrottlingMap().containsKey(getTypeName())) {
			throttlingTargetTPS = context.getThrottlingMap().get(getTypeName());
		}
		
		setName(getTypeName() + getName());
	}
	
	public abstract String getTypeName();

	public CommonSampler getSampler() {
		return sampler;
	}
	
	public void startWithCount() {
		super.start();
		startTime = System.currentTimeMillis();
		throttlingStartT = startTime;
	}

	public void terminateAndJoin() throws InterruptedException {
		this.isTerminate = true;
		this.join();
		endTime = System.currentTimeMillis();
	}
	
	public long getWorkingTime() {
		if(startTime == -1 || endTime == -1) {
			return -1;
		}
		
		return (endTime - startTime);
	}

	public boolean isTerminate() {
		return isTerminate;
	}

	public R getResource() {
		return this.resource;
	}

	public ConcurrentLinkedQueue<E> getQueue(String queueName) {
		return this.messageQueues.get(queueName);
	}

	public static Logger logger() {
		return log;
	}
	
	public void throttling() {
		// throttling is disabled
		if (throttlingTargetTPS <= 0) {
			return;
		}
		
		// throttling; limit a max number of requests per seconds
		if (preTotalCount + throttlingTargetTPS <=  sampler.getTotalCount()) {
			
			long remaingTime = (throttlingStartT + 1000) - System.currentTimeMillis();
			if (remaingTime > 0) {
				try {
					Thread.sleep(remaingTime);
					//System.out.println("sleep " + remaingTime + " - " + preTotalCount + " / " + sampler.getTotalCount());
					
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			} 

			preTotalCount = sampler.getTotalCount();
			
			throttlingStartT = System.currentTimeMillis();
		} else {
			//System.out.println(preTotalCount + " / " + sampler.getTotalCount());
		}
	}


	@Override
	abstract public void run();
}