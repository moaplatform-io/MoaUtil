package io.moa.bmt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moa.bmt.test.MoaClient;
import moa.bmt.test.ECKey;
import moa.bmt.test.TransactionBuildTip;
import moa.bmt.test.TransactionBuilder;
import moa.bmt.test.TransactionSender;
import moa.bmt.test.exception.MoaException;
import moa.bmt.test.exception.InsufficientFundException;

public class FeeCharger {
	private static final Logger log = LoggerFactory.getLogger(FeeCharger.class);
	private static long MyFee = 1000000;

	public static void charge(MoaClient cstClient, String chargerPrivateKey, String btcAmount,
			List<String> targetAddresses) throws IOException, MoaException {
		long chargeFee = moa.bmt.test.Math.convertToSatoshi(btcAmount);

		long currnetBalance = 0L;
		long accRequiredBalance = 0L;
		TransactionBuilder txBuilder = new TransactionBuilder();

		// get remaining balances of target addresses
		for (String clientAddr : targetAddresses) {

			currnetBalance = cstClient.getBalance(clientAddr);

			log.debug("Remaining Balance to " + clientAddr + " = " + currnetBalance);

			if (chargeFee > currnetBalance) {
				accRequiredBalance += (chargeFee - currnetBalance);
				txBuilder.addOutput(clientAddr, (chargeFee - currnetBalance));
			}
		}

		// get charger's remaining balance
		String chargerAddr = ECKey.deriveAddress(chargerPrivateKey);

		currnetBalance = cstClient.getBalance(chargerAddr);

		log.debug("Remaining Balance to Charger({}) = {}", chargerAddr, currnetBalance);

		// check current remaining balance to charger
		if (currnetBalance < accRequiredBalance + MyFee) {
			String msg = String.format("A Charger Requires " + accRequiredBalance
					+ " more Balance. Current Remaining is " + currnetBalance);
			log.error(msg);

			throw new InsufficientFundException(msg);
		}

		txBuilder.setFee(MyFee);

		if (accRequiredBalance > 0) {
			log.info(String.format("Send Total %d Balances to each Clients", accRequiredBalance));

			String signedTx = cstClient.createSignedTransaction(txBuilder, chargerPrivateKey);
			cstClient.sendTransaction(signedTx);
		}

		cstClient.close();
	}

	public static void main(String[] args) throws IOException, MoaException {
		if (args.length != 4) {
			System.err.println(
					"Usage: java moa.bmt.bmt.util.FeeCharger {ServerEndpoint} {TargetAddress} {AmountOfCoin} {ProviderPrivateKey}");
			throw new RuntimeException("Invalid Number of Arguments: " + args.length);
		}

		String serverEndpoint = args[0];
		String targetAddress = args[1];
		String amountOfCoin = args[2];
		String providerPrivateKey = args[3];

		MoaClient csClient = new MoaClient(serverEndpoint);
		List<String> addressList = new LinkedList<String>();
		addressList.add(targetAddress);

		charge(csClient, providerPrivateKey, amountOfCoin, addressList);
	}
}
