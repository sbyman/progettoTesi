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
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.IssuerMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SAMLAssertion {

	private Assertion getAssertion(Response response) throws MarshallingException {

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
    	
    	return assertionList.get(0);
    	
	}

	public Assertion getAssertion(String resp) throws MarshallingException {

		System.out.println("SAMLResponse:\n" + resp);

		// Passa da stringa a documento per ottenere un Element
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		Element element = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(resp.getBytes("UTF-8"))));
			element = doc.getDocumentElement();
		} catch (ParserConfigurationException e) {
			System.err.println("Errore nella configurazione del parser: Response");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException | IOException e) {
			System.err.println("Errore nella creazione del documento: Response");
			e.printStackTrace();
			System.exit(1);
		}

		// Tramite l'Element mi creo un SAMLObject Response
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

		return getAssertion(response);
	}

}
