package io.moa.bmt.tool.contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.moa.bmt.AbstarctBMTDataStore;

public class ExecuteContractStoreForSDK extends AbstarctBMTDataStore<List<String>> {

	public ExecuteContractStoreForSDK(String filePath, boolean reuseFlag) {
		super(filePath, reuseFlag);
	}

	@Override
	public List parseLine(String[] lineSplit) throws IOException {
		if (lineSplit.length != 1) {
			throw new IOException("Invalid Address Info Format: Must be ({Smart Contrct Execution Code})");
		}
		try {
			List<String> array=new ArrayList<String>();
			for (int i = 0; i < lineSplit.length; i++) {
				array.add(lineSplit[i]);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String generateLine(List<String> data) {
	
		try {
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public List<String> generateData() {
		throw new RuntimeException(
				"Cannot generate data automatically. Before start, you must generate contract execution code your-self");
	}
}
