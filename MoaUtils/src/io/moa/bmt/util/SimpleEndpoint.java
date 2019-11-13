package io.moa.bmt.util;

import java.security.PublicKey;

import moa.bmt.test.AbstractEndpoint;

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
