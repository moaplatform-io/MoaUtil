package io.moa.bmt.tool.contract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import moa.bmt.test.ECKey;
import moa.bmt.test.contract.BMTFlexSourceCode;
import io.moa.bmt.MoaRunnerContext;
import io.moa.bmt.util.FeeCharger;

public class QueryContractRunner extends MoaRunnerContext<BMTFlexSourceCode, QueryContractStore> {

	public static final String ARG_CLIENT_CHARGE_FEE = "clientChargeFee";
	public static final String ARG_QUERY_CODE_FILE = "queryCodeFile";
	public static final String ARG_FEE_CHARGER_PK = "chargerPrivateKey";
	public static final String ARG_THROTTLING_TPS = "throttlingTps";
	public static final String ARG_DATA_NUM = "dataNum";
	public static final String ARG_EXECUTE_THREAD_NUM = "executeThreadNum";
	public static final String ARG_CACHE_SIZE = "cacheSize";
	public static final String ARG_CHARGER_PRIVATEKEY = "chargerPrivateKey";
	public static final String ARG_RUNNING_SEC = "runningSec";
	public static final String ARG_CONTRACT_ADDRESS = "contractAddress";
	public static final String ARG_CONTRACT_KEY = "contrackPrivateKey";
	
	private static List<String> addressMap = new LinkedList<String>(); 
	
	public QueryContractRunner(String[] args) throws Exception {
		super(args);
	}

	@Override
	public void initVariables() throws Exception {
		String clientInfoFilePath = getConfMng().getStrConf(ARG_QUERY_CODE_FILE, "");
		
		resource = new QueryContractStore(clientInfoFilePath, true);
		messageQueues = new HashMap<String, ConcurrentLinkedQueue<BMTFlexSourceCode>>();

		// set throttling
		int throttlingTps = getConfMng().getIntConf(ARG_THROTTLING_TPS, 0);
		throttlingTpsMap.put(QueryThread.TYPE_NAME, throttlingTps);
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
		String privateKey = getConfMng().getStrConf(ARG_CONTRACT_KEY, "");
		
		for (int i = 0; i < threadNum; i++) {
			// create and set key for executing smart contract
			
			//String address = ECKey.deriveAddress(privateKey);
			
			// create and add threads
			addThread(new QueryThread(this, createNextNodeClient(cacheSize), privateKey, contractAddress));
			
			addressMap.add(contractAddress);
		}
	}

	public static void main(String[] args) throws Exception {
		
		// initialize context
		MoaRunnerContext<BMTFlexSourceCode, QueryContractStore> context = new QueryContractRunner(args);
		
		// charge tx fee to each clients
		//String chargerPrivateKey = context.getConfMng().getStrConf(ARG_CHARGER_PRIVATEKEY, "");
		//String chargeFee = context.getConfMng().getStrConf(ARG_CLIENT_CHARGE_FEE, "1");
		
		//FeeCharger.charge(context.createNextNodeClient(0), chargerPrivateKey, chargeFee, addressMap);
		
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
				System.out.printf("%4d%14s\n", seq, progressMap.get(QueryThread.TYPE_NAME));
				
				seq++;
			}

			// print final summary message
			if (System.currentTimeMillis() - startT >= runningSec) {
				context.stop();
				break;
			}
			
			Thread.sleep(10);
		}
		
		// print progress msg
		Map<String, String> progressMap = context.progress();

		System.out.printf("%4d%14s\n", seq, progressMap.get(QueryThread.TYPE_NAME));

		Map<String, String> summaryMap = context.summary(0);
		for (String summary : summaryMap.values()) {
			System.out.println(summary);
		}
	}
}
