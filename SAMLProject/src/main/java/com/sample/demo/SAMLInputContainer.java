package com.sample.demo;

public class SAMLInputContainer {

	private String issuer;
	private String protocolBinding;
	private String destination;
	private String assertionConsumerServiceURL;
	private String SAMLRequest;
	
	public SAMLInputContainer() {}
	
	public SAMLInputContainer(String issuer, String protocolBinding, String destination, String assertionConsumerServiceURL) {
		this.issuer = issuer;
		this.protocolBinding = protocolBinding;
		this.destination = destination;
		this.assertionConsumerServiceURL = assertionConsumerServiceURL;
	}

	public String getProtocolBinding() {
		return protocolBinding;
	}

	public void setProtocolBinding(String protocolBinding) {
		this.protocolBinding = protocolBinding;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getAssertionConsumerServiceURL() {
		return assertionConsumerServiceURL;
	}

	public void setAssertionConsumerServiceURL(String assertionConsumerServiceURL) {
		this.assertionConsumerServiceURL = assertionConsumerServiceURL;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getSAMLRequest() {
		return SAMLRequest;
	}
	
	public void setSAMLRequest(String SAMLRequest) {
		this.SAMLRequest = SAMLRequest;
	}
	
}
