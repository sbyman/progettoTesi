package com.sample.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.sample.demo.assertion.AssertionType;
import com.sample.demo.wsse.SecurityHeaderType;

import hello.wsdl.GetCountryRequest;
import hello.wsdl.GetCountryResponse;

public class CountryClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(CountryClient.class);
	
	public GetCountryResponse getCountry(String country, AssertionType assertion) {

		GetCountryRequest request = new GetCountryRequest();
		request.setName(country);

		log.info("Requesting location for " + country);
		
		CountryClient quoteClient;
		HeaderBuilder headerBuilder = new HeaderBuilder();
		SecurityHeaderType securityHeader = headerBuilder.buildHeader(assertion);

		GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate().marshalSendAndReceive(
				"http://soap-server.com:8080/ws/countries", 
				request, 
				new SoapRequestHeaderModifier(securityHeader));

		return response;
	}

}