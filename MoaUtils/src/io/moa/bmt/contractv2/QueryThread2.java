package io.moa.bmt.contractv2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moa.bmt.test.MoaClient;
import moa.bmt.test.contract.BMTFlexSourceCode;
import moa.bmt.test.exception.MoaException;
import moa.bmt.test.model.ContractResult;
import io.moa.bmt.AbstractBmtThread;
import io.moa.bmt.RunnerContext;

public class QueryThread2 extends AbstractBmtThread<BMTFlexSourceCode, QueryContract2Store> {

	private static final Logger log = LoggerFactory.getLogger(QueryThread2.class);
			
	public static final String TYPE_NAME = "QueryThread";
	private String contractAddress;
	private String privateKey;
	private MoaClient cstClient = null;
	
	public QueryThread2(RunnerContext<BMTFlexSourceCode, QueryContract2Store> context, 
			MoaClient cstClient, String privateKey, String contractAddress) {
		super(context);
		this.contractAddress = contractAddress;
		this.cstClient = cstClient;
		this.privateKey = privateKey;
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public void run() {
		// fetch resource file
		QueryContract2Store store = (QueryContract2Store) getResource();
		ContractResult res = null;
		
		while (false == isTerminate()) {
		
			// fetch random data
			BMTFlexSourceCode queryCode = store.next();
			
			// traffic control 
			throttling();
			getSampler().start();
			
			// smart contract query
			try {
				
				res = cstClient.queryContract(contractAddress, "LSC", queryCode.getCode());
				
			} catch (IOException e) {
				log.error("Fail to Query Contract : " + e.getMessage(), e);
			} catch (MoaException e) {
				log.error("Invalid : " + e.getDetailedMessage(), e);
			}
			
			//contract query result
			if(res.getSuccess()){
				getSampler().end();
				//log.info("query res json result :: "+ res.asJson());
			}else {
				
				getSampler().fail();
				//log.info("query res fail "+ res.asJson());
			}
					
		}
	}

}
