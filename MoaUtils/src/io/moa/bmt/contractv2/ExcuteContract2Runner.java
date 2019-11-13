package io.moa.bmt.contractv2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import moa.bmt.test.ECKey;
import moa.bmt.test.contract.BMTFlexSourceCode;
import io.moa.bmt.MoaRunnerContext;
import io.moa.bmt.util.FeeCharger;

public class ExcuteContract2Runner extends MoaRunnerContext<BMTFlexSourceCode, ExecuteContract2Store> {

	public static final String ARG_CLIENT_CHARGE_FEE = "clientChargeFee";
	public static final String ARG_EXECUTION_CODE_FILE = "executeCodeFile";
	public static final String ARG_FEE_CHARGER_PK = "chargerPrivateKey";
	public static final String ARG_THROTTLING_TPS = "throttlingTps";
	public static final String ARG_DATA_NUM = "dataNum";
	public static final String ARG_EXECUTE_THREAD_NUM = "executeThreadNum";
	public static final String ARG_CACHE_SIZE = "cacheSize";
	public static final String ARG_CHARGER_PRIVATEKEY = "chargerPrivateKey";
	public static final String ARG_RUNNING_SEC = "runningSec";
	public static final String ARG_CONTRACT_ADDRESS = "contractAddress";
	
	private static List<String> addressMap = new LinkedList<String>(); 
	
	public ExcuteContract2Runner(String[] args) throws Exception {
		super(args);
	}

	@Override
	public void initVariables() throws Exception {
		String clientInfoFilePath = getConfMng().getStrConf(ARG_EXECUTION_CODE_FILE, "");
		
		resource = new ExecuteContract2Store(clientInfoFilePath, true);
		messageQueues = new HashMap<String, ConcurrentLinkedQueue<BMTFlexSourceCode>>();

		// set throttling
		int throttlingTps = getConfMng().getIntConf(ARG_THROTTLING_TPS, 0);
		throttlingTpsMap.put(ExecuteThread2.TYPE_NAME, throttlingTps);
	}

	@Override
	public void initResource() throws Exception {
		int clientNum = getConfMng().getIntConf(ARG_DATA_NUM, 10);
		
		resource.load(0, clientNum);
		
		// read file, parse or ...
	}

	@Override
	public void initThread() throws Exception {
		int threadNum = getConfMng().getIntConf(ARG_EXECUTE_THREAD_NUM, 1);
		int cacheSize = getConfMng().getIntConf(ARG_CACHE_SIZE, 1);
		String contractAddress = getConfMng().getStrConf(ARG_CONTRACT_ADDRESS, "");
		
		for (int i = 0; i < threadNum; i++) {
			// create and set key for executing smart contract
			String privateKey = ECKey.createNewPrivateKey();
		
			//String privateKey = "L13sG9EbKpXv9p4hS8w4m3yLTfTJnfVAq1cjwa2TZ9HoGUyR9etQ";
			String address = ECKey.deriveAddress(privateKey);
			
			//String address = "16MoCUDKxyGkGEbYr3QK7Uch18mXBvLkzD";
			
			
			// create and add threads
			addThread(new ExecuteThread2(this, createNextNodeClient(cacheSize), privateKey, contractAddress));
			
			addressMap.add(address);
		}
	}

	public static void main(String[] args) throws Exception {
		
		// initialize context
		MoaRunnerContext<BMTFlexSourceCode, ExecuteContract2Store> context = new ExcuteContract2Runner(args);
		
		// charge tx fee to each clients
		String chargerPrivateKey = context.getConfMng().getStrConf(ARG_CHARGER_PRIVATEKEY, "");
		String chargeFee = context.getConfMng().getStrConf(ARG_CLIENT_CHARGE_FEE, "1");
		
		FeeCharger.charge(context.createNextNodeClient(0), chargerPrivateKey, chargeFee, addressMap);
		
		// prepare
		int runningSec = context.getConfMng().getIntConf(ARG_RUNNING_SEC, 10) * 1000;

		long startT = System.currentTimeMillis();
		long lastT = startT;
		int seq = 1;

		System.out.println("================= Job Start =================");
		System.out.printf("%4s%16s\n", "", "SmartContract Executor");
		System.out.printf("%4s%8s%8s\n", "Seq#", "Tps", "lat");
		System.out.println("=============================================");

		// run thread
		context.start();
		
		while (false == context.isAllFinished()) {

			if (lastT + 1000 < System.currentTimeMillis()) {
				// update time
				lastT = System.currentTimeMillis();
				
				// print progress msg
				Map<String, String> progressMap = context.progress();
				System.out.printf("%4d%14s\n", seq, progressMap.get(ExecuteThread2.TYPE_NAME));
				
				seq++;
			}

			// print final summary message
			if (System.currentTimeMillis() - startT >= runningSec) {
				context.stop();
				break;
			}
			
			Thread.sleep(60);
		}
		
		// print progress msg
		Map<String, String> progressMap = context.progress();

		System.out.printf("%4d%14s\n", seq, progressMap.get(ExecuteThread2.TYPE_NAME));

		Map<String, String> summaryMap = context.summary(0);
		for (String summary : summaryMap.values()) {
			System.out.println(summary);
		}
	}
}
