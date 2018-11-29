package com.sample.demo;

import javax.xml.bind.JAXBElement;

import com.sample.demo.assertion.AssertionType;
import com.sample.demo.assertion.ObjectFactory;
import com.sample.demo.wsse.ReferenceType;
import com.sample.demo.wsse.SecurityHeaderType;
import com.sample.demo.wsse.SecurityTokenReferenceType;

public class HeaderBuilder {
	
	public void buildReferenceHeader(String location) {
		ReferenceType reference = new ReferenceType();
		reference.setURI(location);
		
		SecurityTokenReferenceType securityTokenReference = new SecurityTokenReferenceType();
		securityTokenReference.setId("SOAP_REQ_11");
		securityTokenReference.getAny().add(reference);

		SecurityHeaderType securityHeader= new SecurityHeaderType();
		securityHeader.getAny().add(securityTokenReference);
	}
	
	public SecurityHeaderType buildHeader(AssertionType assertion) {
		ObjectFactory objectAssertion = new ObjectFactory();
		JAXBElement<AssertionType> assertionElement = objectAssertion.createAssertion(assertion);
		
		SecurityHeaderType securityHeader = new SecurityHeaderType();
		securityHeader.getAny().add(assertionElement);
						
		return securityHeader;
	}

}
