package io.moa.bmt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import moa.bmt.test.MoaClient;
import moa.bmt.test.ECKey;
import moa.bmt.test.TransactionBuildTip;
import moa.bmt.test.TransactionSender;
import moa.bmt.test.contract.BMTFlexSourceCode;
import moa.bmt.test.exception.MoaException;

public class SmartContractInit {

	public static void main(String[] args) throws IOException, MoaException {
		if (args.length != 4) {
			System.err.println(
					"Usage: java moa.bmt.bmt.util.SmartContractInit {ServerEndpoint} {DEF/EXE} {ContractCodePath} {ContractAddrPrivateKey}");
			throw new RuntimeException("Invalid Number of Arguments: " + args.length);
		}

		String serverEndpoint = args[0];
		String defOrExe = args[1];
		String contractCodePath = args[2];
		String contractAddrPrivateKey = args[3];

		MoaClient csClient = new MoaClient(serverEndpoint);

		TransactionSender txSender = new TransactionSender(csClient, contractAddrPrivateKey,
				TransactionBuildTip.STANDARD);
		
		String code = new String(Files.readAllBytes(Paths.get(contractCodePath)));
		
		String contractId = ECKey.deriveAddress(contractAddrPrivateKey);
		
		if(defOrExe.equalsIgnoreCase("DEF")) {
			txSender.defineContract(contractId, new BMTFlexSourceCode(code));
		} else if(defOrExe.equalsIgnoreCase("EXE")) {
			txSender.executeContract(contractId, new BMTFlexSourceCode(code));
		} else {
			System.err.println("2nd argument must be a DEF or EXE");
		}
	}
}
