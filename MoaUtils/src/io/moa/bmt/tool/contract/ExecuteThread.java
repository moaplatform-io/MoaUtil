package io.moa.bmt.tool.contract;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moa.bmt.test.MoaClient;
import moa.bmt.test.TransactionBuildTip;
import moa.bmt.test.TransactionSender;
import moa.bmt.test.contract.BMTFlexSourceCode;
import moa.bmt.test.exception.MoaException;
import io.moa.bmt.AbstractBmtThread;
import io.moa.bmt.RunnerContext;

public class ExecuteThread extends AbstractBmtThread<BMTFlexSourceCode, ExecuteContractStore> {

	private static final Logger log = LoggerFactory.getLogger(ExecuteThread.class);
			
	public static final String TYPE_NAME = "ExecuteThread";
	private String contractAddress;
	private TransactionSender txSender = null;
	
	private static final int MAX_TRANSACTION_RETRY = 3;

	public ExecuteThread(RunnerContext<BMTFlexSourceCode, ExecuteContractStore> context, 
			MoaClient cstClient, String privateKey, String contractAddress) {
		super(context);
		this.contractAddress = contractAddress;
		this.txSender = new TransactionSender(cstClient, privateKey, TransactionBuildTip.STANDARD);
		
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public void run() {
		// fetch resource file
		ExecuteContractStore store = (ExecuteContractStore) getResource();

		while (false == isTerminate()) {
			// fetch random data
			BMTFlexSourceCode executionCode = store.next();
			
			// traffic control 
			throttling();
			getSampler().start();
			String res = null;
			// send smart contract execution tx
			int retry = 0;
			for (retry = 0; retry < MAX_TRANSACTION_RETRY; retry++) {
				try {
					res  = txSender.executeContract(contractAddress, executionCode);
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
