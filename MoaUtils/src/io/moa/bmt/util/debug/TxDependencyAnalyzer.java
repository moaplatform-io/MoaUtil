package io.moa.bmt.util.debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import moa.bmt.test.TransactionUtil;
import moa.bmt.test.model.Input;
import moa.bmt.test.model.Transaction;

/*
 * This is an utility class to analyze dependency between transactions from a mempool dump file
 */


public class TxDependencyAnalyzer {
	
	private static Map<String, Dependency> index =  new LinkedHashMap<String, Dependency>();
	
	
	public class Dependency {
		Transaction tx;
		Map<String, Dependency> parents;
		int uid;
		
		public Dependency(Transaction tx, int id) {
			this.tx = tx;
			this.parents = new HashMap<String, Dependency>();
			this.uid = id;
		}
	}
	
	public int printParent(StringBuffer strBuf, Dependency dependency, int currentDepth, int maxDepth) {
		currentDepth++;
		
		strBuf.append(dependency.uid);
		
		for (Dependency parent : dependency.parents.values()) {
			strBuf.append("(");
			printParent(strBuf, parent, 0, 0);
			strBuf.append(")");
		}
		
		
		if(currentDepth >= maxDepth) {
			return currentDepth;
		} else {
			return maxDepth;
		}
	}

	public void printSummary(StringBuffer strBuf, Dependency dependency) {
		
		strBuf.append("ID: " + dependency.uid + ", Hash: " + dependency.tx.getId());
		
		for (Dependency parent : dependency.parents.values()) {
			strBuf.append(", Dep.: (");
			printParent(strBuf, parent, 0, 0);
			strBuf.append(")");
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Invalid Number of Arguments: " + args.length);
			System.err.println("Usage: java {class_name} {dump_tx_file_path}");

			throw new IOException("Invalid Number of Arguments: " + args.length);
		}
		
		TxDependencyAnalyzer analyzer = new TxDependencyAnalyzer();
		
		int id = 0;
		// parse a dump file and add to a list
		try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	id++;
		    	Transaction tx = TransactionUtil.parseRawTransaction(line);
		    	Dependency dep = analyzer.new Dependency(tx, id);
				index.put(tx.getId(), dep);
		    }
		}
		
		// collect root txs that are not depends on others
		for(Entry<String, Dependency> txEntry : index.entrySet()) {
			for(Input input :txEntry.getValue().tx.getInputs()) {
				String dependTxId = input.outputTransactionId;
				
				// check dependency
				Dependency dependency = null;
				if(null != (dependency = index.get(dependTxId))) {
					txEntry.getValue().parents.put(dependTxId, dependency);
				}
			}
		}
		
		for(Entry<String, Dependency> txEntry : index.entrySet()) {
			StringBuffer strBuf = new StringBuffer();
			analyzer.printSummary(strBuf, txEntry.getValue());
			System.out.println(strBuf.toString());
		}
	}
}
