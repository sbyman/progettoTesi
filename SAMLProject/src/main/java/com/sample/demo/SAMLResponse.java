package com.sample.demo;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.IssuerMarshaller;
import org.opensaml.saml2.core.impl.NameIDMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.util.XMLObjectHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class SAMLResponse {

	private static XMLObjectBuilderFactory builderFactory;

    public static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException{

        if(builderFactory == null){
            // OpenSAML 2.3
            DefaultBootstrap.bootstrap();
            builderFactory = Configuration.getBuilderFactory();
        }

        return builderFactory;
    }
    
    private Map<String, String> unbuildResponse(Response response, User user, SAMLAssertionResponse assertionResponse) throws MarshallingException {
    	
    	//Prendiamo i valori che ci possono interessare contenuti nella response
    	
    	//Otteniamo l'issuer del response
    	Issuer issuer = (Issuer) response.getIssuer();
    	//Passo in stringa
    	IssuerMarshaller issuerMarshaller = new IssuerMarshaller();
    	Element plainTextIssuer = issuerMarshaller.marshall(issuer);
    	String issuerString = plainTextIssuer.getTextContent();
    	
    	//Otteniamo il momento del rilascio del response
    	DateTime issueInstant = response.getIssueInstant();
    	
    	//Otteniamo lo status code del response
    	String statusCode = response.getStatus().getStatusCode().getValue();
    	
    	Map<String, String> data = new HashMap<String, String>();
    	data.put("Issuer", issuerString);
    	data.put("IssueInstant", issueInstant.toString());
    	data.put("StatusCode", statusCode);
    	
    	/*
    	if(statusCode != StatusCode.SUCCESS_URI) {
    		return data;
    	}
    	*/
    	
    	//Prendiamo le varie asserzioni
    	List<Assertion> assertionList = response.getAssertions();
    	for(int i = 0; i < assertionList.size(); i++) {
    		
        	Assertion assertion = assertionList.get(i);
        	
        	assertionResponse.setAssertion(assertion);
        	assertionResponse.writeAssertion();
        	
        	//IssueInstant e Issuer
        	DateTime assertionIssueInstant = assertion.getIssueInstant();
        	Issuer assertionIssuer = (Issuer) assertion.getIssuer();	
        	//Passo in stringa
        	IssuerMarshaller assertionIssuerMarshaller = new IssuerMarshaller();
        	Element plainTextAssertionIssuer = assertionIssuerMarshaller.marshall(assertionIssuer);
        	String assertionIssuerString = plainTextAssertionIssuer.getTextContent();
        	
        	
        	//Identificato unico del soggetto dell'asserzione
        	NameID nameId = (NameID) assertion.getSubject().getNameID();
        	//Passo in stringa
        	NameIDMarshaller nameIdMarshaller = new NameIDMarshaller();
        	Element plainTexNameIdMarshaller = nameIdMarshaller.marshall(nameId);
        	String nameIdString = plainTexNameIdMarshaller.getTextContent();
        	
        	data.put("AssertionIssuer", assertionIssuerString);
        	data.put("AssertionIssueInstant", assertionIssueInstant.toString());
        	data.put("NameID", nameIdString);
        	
        	user.setSamlNameId(nameIdString);
        	
        	//Attributi dell'utente
        	List<AttributeStatement> attributeStatementList = assertion.getAttributeStatements();
        	for(int j = 0; j < attributeStatementList.size(); j++ ) {

            	List<Attribute> attributeList = attributeStatementList.get(j).getAttributes();
            	for(int k = 0; k < attributeList.size(); k++ ) {
            		
            		//Ottengo il FriendlyName ovvero l'identificativo dell'attributo
            		String friendlyName = attributeList.get(k).getFriendlyName();
            		//Ottengo in valore dell'attributo
            		List<XMLObject> attributeValueList = attributeList.get(k).getAttributeValues();
        			XMLObject value = attributeValueList.get(0);
        			//Passo in stringa
        			Element plainTextValue = XMLObjectHelper.marshall(value);
        			String valueString = plainTextValue.getTextContent();
        			
        			data.put(friendlyName, valueString);
        			
            	}
        	}
        	user.setUser(data.get("uid"));
        	user.setEmail(data.get("email"));
    	}
    	
    	return data;
    }
    
    public Map<String, String> unbuildResponse(String resp, User user, SAMLAssertionResponse assertionResponse) throws MarshallingException{
    	
    	System.out.println("SAMLResponse:\n" + resp);
    	
    	//Passa da stringa a documento per ottenere un Element
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		Element element = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(resp.getBytes("UTF-8"))));
			element = doc.getDocumentElement();
		} catch (ParserConfigurationException e) {
			System.err.println("Errore nella configurazione del parser");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException | IOException e) {
			System.err.println("Errore nella creazione del documento");
			e.printStackTrace();
			System.exit(1);
		}
		
		
		//Tramite l'Element mi creo un SAMLObject Response
		Response response = null;
		try {
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
			response = (Response) unmarshaller.unmarshall(element);

		} catch (UnmarshallingException e) {
			System.err.println("Errore nell'unmarshalling");
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("response.xml"));
			String stringResponse = XMLHelper.nodeToString(element);
			writer.write(stringResponse);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unbuildResponse(response, user, assertionResponse);
    }
}
