package io.moa.bmt.tool.contract;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moa.test.Moa.sdkimpl.SmartContractMngConfig;
import com.moa.test.Moa.sdkimpl.SmartContractMngDao;

import moa.bmt.test.MoaClient;
import moa.bmt.test.contract.BMTFlexSourceCode;
import moa.bmt.test.exception.MoaException;
import io.moa.bmt.AbstractBmtThread;
import io.moa.bmt.RunnerContext;

public class ExecuteThreadForSDK_CP1 extends AbstractBmtThread<BMTFlexSourceCode, ExecuteContractStoreForSDK> {

	private static final Logger log = LoggerFactory.getLogger(ExecuteThreadForSDK_CP1.class);
			
	public static final String TYPE_NAME = "ExecuteThread";
	
	private static final int MAX_TRANSACTION_RETRY = 3;

	private SmartContractMngDao dao;
	
	public ExecuteThreadForSDK_CP1(RunnerContext<BMTFlexSourceCode, ExecuteContractStoreForSDK> context, 
			MoaClient cstClient, String privateKey, String contractAddress) {
		super(context);
		SmartContractMngConfig config=null;
		try {
			config = new SmartContractMngConfig("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dao=new SmartContractMngDao(config);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public void run() {
		// fetch resource file
		ExecuteContractStoreForSDK store = (ExecuteContractStoreForSDK) getResource();

		while (false == isTerminate()) {
			// fetch random data
			List<String> executionCode = store.next();

			// traffic control 
			throttling();
			getSampler().start();
			String res = null;
			// send smart contract execution tx
			int retry = 0;
			for (retry = 0; retry < MAX_TRANSACTION_RETRY; retry++) {
				try {
					res=dao.excutionCommon(executionCode);
					if(res != null) break;
					//log.info("res txid: " + res);
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					// saturation at
					// moa.bmt.spongycastle.crypto.digests.LongDigest.update(LongDigest.java:120)
					log.warn("Random Satuation Error. Retry", e);
					continue;
				} catch (IOException e) {
					log.error("Fail to Send Tx: " + e.getMessage(), e);
					continue;
				} catch (MoaException e) {
					log.error("Invalid Tx: " + e.getDetailedMessage(), e);
					continue;
				} catch (Exception e) {
					log.error("Invalid Tx: " + e.getMessage(), e);
					continue;
				}
			}
			
			if (res == null) {
				getSampler().fail();
			} else {
				getSampler().end();
			}
		}
	}

}
