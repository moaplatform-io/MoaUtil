package io.moa.bmt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstarctBMTDataStore<D> {
	private static final Logger log = LoggerFactory.getLogger(AbstarctBMTDataStore.class);

	protected Random random;

	private String filePath;
	private LinkedList<D> dataList;
	private Iterator<D> iter;

	private boolean reuseFlag = true;

	public AbstarctBMTDataStore(String filePath, boolean reuseFlag) {
		this.random = new Random(System.currentTimeMillis());
		this.dataList = new LinkedList<D>();
		this.iter = null;
		this.filePath = filePath;
		this.reuseFlag = reuseFlag;
	}

	abstract public D parseLine(String[] lineSplit) throws Exception;

	abstract public String generateLine(D data);

	abstract public D generateData();

	// This method generate more data until a number of data is reaching to a total number
	// if this already has same or more than the total number, skip this method 
	private void generate(long total) throws InterruptedException, SecurityException, IOException {
		
		if (total > size()) {
			// generate and add more info
			log.info(String.format("Generate %d More Datas", total - size()));

			List<Thread> threadList = new ArrayList<Thread>();
			int powerfulThreadNum = Runtime.getRuntime().availableProcessors() * 2;

			// because generate keys takes long time, let's use multi-threads
			// call cpu processor * 2 number of threads
			for (int i = 0; i < powerfulThreadNum; i++) {
				Thread thread = new Thread() {
					private long num = 0;

					public Thread setTargetSize(long num) {
						this.num = num;
						return this;
					}

					@Override
					public void run() {
						while (true) {
							D data = generateData();
							long size = add(data);
							if (size >= num) {
								return;
							}
						}
					}
				}.setTargetSize(total);

				thread.start();
				threadList.add(thread);
			}

			for (int i = 0; i < threadList.size(); i++) {
				threadList.get(i).join();
			}
			
			// save generate data to a file
			dump();
		}
	}

	public void load(long fromline, long lineNum)
			throws FileNotFoundException, IOException, SecurityException, InterruptedException {
		BufferedReader bufferedfileReader = null;
		dataList.clear();
		iter = null;
		long count = 0l;

		String line = "";

		if (false == reuseFlag) {
			// purge all stored information
			log.info("Clear all previously existing data");
			clear();
		} else {
			try {
				// read a server info file
				bufferedfileReader = new BufferedReader(new FileReader(filePath));

				while ((line = bufferedfileReader.readLine()) != null) {

					if (count < fromline) {
						count++;

						continue; // skip line
					} else if (count >= fromline + lineNum) {
						break;
					} else {
						count++;
					}

					String[] lineSplit = line.split("\t");

					D data = parseLine(lineSplit);

					dataList.add(data);
				}

			} catch (Exception e) {
				log.error("Fail to parse input data (" + line + "): " + e.getMessage());
			} finally {
				// close buffered readers
				if (bufferedfileReader != null)
					bufferedfileReader.close();
				log.info("Load {} data ({} ~ {})", dataList.size(), fromline, fromline + lineNum);
			}
		}
		
		// if not enough data at the file, then generate more and dump it
		generate(fromline + lineNum);
		
		resetIter();
	}

	public void clear() {
		synchronized (this) {
			dataList.clear();
			iter = null;
		}
	}

	public boolean hasNext() {
		synchronized (this) {
			if (iter != null && this.dataList.size() != 0)
				return iter.hasNext();
			else
				return false;
		}
	}

	public void resetIter() {
		synchronized (this) {
			iter = dataList.iterator();
		}
	}

	public D next() {
		synchronized (this) {
			if (iter == null || iter.hasNext() == false)
				// reset iterator
				iter = dataList.iterator();

			return iter.next();
		}
	}

	public D poll() {
		synchronized (this) {
			return dataList.pollFirst();
		}
	}

	public long add(D data) {

		long size = 0L;
		
		synchronized (this) {
			dataList.add(data);
			size = dataList.size();
		}

		return size;
	}

	public void reduce(long targetSize) {
		synchronized (this) {
			if (targetSize > dataList.size())
				return;

			for (int i = dataList.size(); i > targetSize; i--) {
				dataList.remove(i - 1);
			}

			iter = null;
		}
	}

	public int size() {
		synchronized (this) {
			return dataList.size();
		}
	}

	private void dump() throws SecurityException, IOException {
		synchronized (this) {
			// writes contents to file
			BufferedWriter bufferedWriter = null;

			try {
				// dump server info to the file
				bufferedWriter = new BufferedWriter(new FileWriter(filePath, false)); // overwrite

				Iterator<D> iter = dataList.iterator();

				while (iter.hasNext()) {
					D data = iter.next();
					bufferedWriter.write(generateLine(data) + "\n");
				}
			} finally {
				bufferedWriter.close();
			}
		}
	}

	public D getData(int seq) {
		synchronized (this) {
			return dataList.get(seq);
		}
	}

	public D getRandomData() {
		synchronized (this) {
			return dataList.get(random.nextInt(size()));
		}
	}

	public D getShardRandomData(int totalThreadNum, int threadSeq) {

		synchronized (this) {

			if (size() < totalThreadNum) {
				log.error("A Number of thread cannot be bigger than a number of data. Finish a Process");
				System.exit(0);
			}

			int rangeMax = size() / totalThreadNum;
			int randomIndex = rangeMax * threadSeq + random.nextInt(rangeMax);

			return getData(randomIndex);
		}
	}

}
