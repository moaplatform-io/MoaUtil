package io.moa.bmt;

import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import moa.bmt.test.AbstractEndpoint;
import moa.bmt.test.MoaClient;
import moa.bmt.test.model.CredentialsProvider;

public abstract class MoaRunnerContext<E, R> extends RunnerContext<E, R> {

	public static final String ARG_CLIENT_CHARGE_FEE = "node";

	private ConcurrentLinkedQueue<String> transactionLogQueue;

	private int nextNodeIndex = 0;

	private List<String> nodeList;

	public MoaRunnerContext(String[] args) throws Exception {
		super(args);

		this.transactionLogQueue = new ConcurrentLinkedQueue<String>();
	}

	public void addLog(String logMsg) {
		this.transactionLogQueue.add(logMsg);
	}

	public String pollLog() {
		return this.transactionLogQueue.poll();
	}

	public MoaClient createNextNodeClient(int cacheSize) {
		if (nodeList == null) {
			nodeList = new LinkedList<String>();

			String blockchainNodes = confManager.getStrConf(ARG_CLIENT_CHARGE_FEE, "");

			String[] nodes = blockchainNodes.split(";");
			for (String node : nodes) {
				nodeList.add(node);
			}
		}
		
		String nodeSeq = nodeList.get(nextNodeIndex++ % nodeList.size());

		if (cacheSize > 0) {
			return new MoaClient(new SimpleCredentialProvider(), new SimpleEndpoint(nodeSeq),
					cacheSize);
		} else {
			return new MoaClient(new SimpleCredentialProvider(), new SimpleEndpoint(nodeSeq));
		}
	}

	public int getNodeNum() {
		return this.nodeList.size();
	}

	public class SimpleEndpoint implements AbstractEndpoint {

		private String address = "";

		public SimpleEndpoint(String address) {

			this.address = address;
		}

		public String endpoint() {
			return address;
		}

		public boolean mainnet() {
			return true;
		}

		public PublicKey getPublicKey() {
			return null;
		}

	}

	public class SimpleCredentialProvider extends CredentialsProvider {

		public SimpleCredentialProvider() {
			super();
		}

		@Override
		public String getAccessKey() {
			return "abc";
		}

		@Override
		public String getSecretKey() {
			return "abc";
		}
	}
}
