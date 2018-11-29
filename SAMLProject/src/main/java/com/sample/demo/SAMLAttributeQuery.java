package com.sample.demo;

import java.util.Random;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml1.core.ConfirmationMethod;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml1.core.SubjectConfirmation;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;

public class SAMLAttributeQuery {
	
	private static XMLObjectBuilderFactory builderFactory;

    public static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException{

        if(builderFactory == null){
            // OpenSAML 2.3
            DefaultBootstrap.bootstrap();
            builderFactory = Configuration.getBuilderFactory();
        }

        return builderFactory;
    }
    
    public String generateRandomHexString(int length) {
    	Random random = new Random();
    	
    	StringBuilder sb = new StringBuilder();
    	
    	while (sb. length() < length) {
    		sb. append(Integer. toHexString(random. nextInt()));
    	}
    		
    	return sb.toString();
    }
    
    @SuppressWarnings("rawtypes")
    private AttributeQuery buildAttributeQuery(SAMLInputContainer input, String nameIdUser) throws ConfigurationException {
    	
		//Crea l'oggetto issuer
		SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
		Issuer issuer = (Issuer) issuerBuilder.buildObject();
		issuer.setValue(input.getIssuer());
		
		//Crea l'oggetto subject
		SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Subject.DEFAULT_ELEMENT_NAME);
		Subject subject = (Subject) subjectBuilder.buildObject();
		
		//Crea l'oggetto NameID
		SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(NameID.DEFAULT_ELEMENT_NAME);
		NameID nameId= (NameID) nameIdBuilder.buildObject();
		nameId.setValue(nameIdUser);
		
		//Inserisce il NameID nel Subject
		subject.setNameID(nameId);
    	
		//Crea l'oggetto SubjectConfirmation
		SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();
		
		//Crea l'oggetto confirmationMethod
		SAMLObjectBuilder confirmationMethodBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(ConfirmationMethod.DEFAULT_ELEMENT_NAME);
		ConfirmationMethod confirmationMethod = (ConfirmationMethod) confirmationMethodBuilder.buildObject();
		confirmationMethod.setConfirmationMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");
		
		//Aggiungi il confirmation method al subject confirmation
		subjectConfirmation.getConfirmationMethods().add(confirmationMethod);
		
		//Crea l'oggetto attribute
		SAMLObjectBuilder attributeBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
    	Attribute attribute = (Attribute) attributeBuilder.buildObject();
    	attribute.setFriendlyName("uid");
    	
		//Crea l'oggetto attribute query
    	SAMLObjectBuilder attributeQueryBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AttributeQuery.DEFAULT_ELEMENT_NAME);
    	AttributeQuery attributeQuery = (AttributeQuery) attributeQueryBuilder.buildObject("urn:oasis:names:tc:SAML:2.0:protocol", "AttributeQuery", "samlp");
    	attributeQuery.setID("SP_TEST_" + generateRandomHexString(32));
    	attributeQuery.setVersion(SAMLVersion.VERSION_20);
		attributeQuery.setIssueInstant(DateTime.now());
		attributeQuery.setDestination(input.getDestination());

		attributeQuery.setIssuer(issuer);
		attributeQuery.setSubject(subject);
		attributeQuery.getAttributes().add(attribute);
    	
		//Firma l'attribute query
		SAMLSignature samlSign = new SAMLSignature();
		samlSign.putSignature(attributeQuery);
		
		return attributeQuery;
    	
    }
    
	public AttributeQuery createQuery(SAMLInputContainer input, String nameId) throws ConfigurationException {
		return buildAttributeQuery(input, nameId);
	}

}
