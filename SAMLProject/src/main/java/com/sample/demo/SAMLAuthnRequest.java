package com.sample.demo;

import java.util.Random;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;


public class SAMLAuthnRequest {
	
	private static XMLObjectBuilderFactory builderFactory;

    private static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException{

        if(builderFactory == null){
            // OpenSAML 2.3
            DefaultBootstrap.bootstrap();
            builderFactory = Configuration.getBuilderFactory();
        }

        return builderFactory;
    }
    
    private String generateRandomHexString(int length) {
    	Random random = new Random();
    	
    	StringBuilder sb = new StringBuilder();
    	
    	while (sb. length() < length) {
    		sb. append(Integer. toHexString(random. nextInt()));
    	}
    		
    	return sb.toString();
    }
	
    @SuppressWarnings("rawtypes")
	private AuthnRequest buildAuthnRequest(SAMLInputContainer input, String type) throws ConfigurationException {
		//Crea l'oggetto issuer
		SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
		Issuer issuer = (Issuer) issuerBuilder.buildObject();
		issuer.setValue(input.getIssuer());
		
		//Crea l'oggetto NameIDPolicy
		SAMLObjectBuilder nameIdPolicyBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(NameIDPolicy.DEFAULT_ELEMENT_NAME);
		NameIDPolicy nameIdPolicy = (NameIDPolicy) nameIdPolicyBuilder.buildObject();
		nameIdPolicy.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:transient");
		nameIdPolicy.setAllowCreate(true);
		
		//Crea l'oggetto AuthnContextClassRef
		SAMLObjectBuilder authnContextClassRefBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
		AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authnContextClassRefBuilder.buildObject();
		authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
		
		//Crea l'oggetto RequestedAuthnContext
		SAMLObjectBuilder requestedAuthnContextBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(RequestedAuthnContext.DEFAULT_ELEMENT_NAME);
		RequestedAuthnContext requestedAuthnContext = (RequestedAuthnContext) requestedAuthnContextBuilder.buildObject();
		requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.EXACT);
		
		//Inserisci l'AuthnContextClassRef in RequestedAuthnContext
		requestedAuthnContext.getAuthnContextClassRefs().add(authnContextClassRef);
		
		//Crea l'AuthnRequest
		SAMLObjectBuilder authnRequestBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnRequest.DEFAULT_ELEMENT_NAME);
		AuthnRequest authnRequest = (AuthnRequest) authnRequestBuilder.buildObject("urn:oasis:names:tc:SAML:2.0:protocol", "AuthnRequest", "samlp");
		authnRequest.setVersion(SAMLVersion.VERSION_20);
		authnRequest.setProviderName("SP-SOAP");
		authnRequest.setID("SP_TEST_" + generateRandomHexString(32));
		authnRequest.setIssueInstant(DateTime.now());
		authnRequest.setProtocolBinding(input.getProtocolBinding());
		authnRequest.setDestination(input.getDestination());
		authnRequest.setAssertionConsumerServiceURL(input.getAssertionConsumerServiceURL());
		
		authnRequest.setNameIDPolicy(nameIdPolicy);
		authnRequest.setIssuer(issuer);
		authnRequest.setRequestedAuthnContext(requestedAuthnContext);
		
		//Controlla se devo eseguire una POST o un Redirect
		if(type != "redirect") {
			//In caso di POST firma l'authn request
			SAMLSignature samlSign = new SAMLSignature();
			samlSign.putSignature(authnRequest);
		}

		return authnRequest;
	}
	
	public AuthnRequest createAssertion(SAMLInputContainer input, String type) throws ConfigurationException {
			
		return buildAuthnRequest(input, type);
	}
	

}
