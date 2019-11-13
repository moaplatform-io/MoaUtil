package io.moa.bmt.util;

import moa.bmt.test.model.CredentialsProvider;

public class EmptyCredentialProvider extends CredentialsProvider {

	private String accessKey = "";
	private String secretKey = "";

	public EmptyCredentialProvider() {
		super();

		this.accessKey = "";
		this.secretKey = "";
	}

	@Override
	public String getAccessKey() {
		return accessKey;
	}

	@Override
	public String getSecretKey() {
		return secretKey;
	}

}
