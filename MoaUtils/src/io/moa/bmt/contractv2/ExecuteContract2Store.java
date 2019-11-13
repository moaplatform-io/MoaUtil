package io.moa.bmt.contractv2;

import java.io.IOException;

import moa.bmt.test.ECKey;
import moa.bmt.test.contract.BMTFlexSourceCode;
import moa.bmt.test.exception.MoaException;
import moa.bmt.test.exception.MalformedInputException;
import io.moa.bmt.AbstarctBMTDataStore;

public class ExecuteContract2Store extends AbstarctBMTDataStore<BMTFlexSourceCode> {

	public ExecuteContract2Store(String filePath, boolean reuseFlag) {
		super(filePath, reuseFlag);
	}

	@Override
	public BMTFlexSourceCode parseLine(String[] lineSplit) throws IOException {
		if (lineSplit.length != 1) {
			throw new IOException("Invalid Address Info Format: Must be ({Smart Contrct Execution Code})");
		}

		try {
			return new BMTFlexSourceCode(lineSplit[0]);
		} catch (MoaException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String generateLine(BMTFlexSourceCode data) {
	
		try {
			return data.getCode();
		} catch (MoaException e) {
			return "";
		}
	}

	@Override
	public BMTFlexSourceCode generateData() {
		throw new RuntimeException(
				"Cannot generate data automatically. Before start, you must generate contract execution code your-self");
	}
}
