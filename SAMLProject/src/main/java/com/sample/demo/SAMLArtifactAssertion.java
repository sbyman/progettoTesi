package com.sample.demo;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml1.core.impl.ResponseMarshaller;
import org.opensaml.saml2.core.ArtifactResponse;
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
import org.xml.sax.SAXException;

public class SAMLArtifactAssertion {
	
	public Assertion getArtifactAssertion(String resp) throws MarshallingException{
    	
    	System.out.println(resp);
    	
    	//Passa da stringa a documento per ottenere un Element
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		Element element = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(resp.getBytes("UTF-8")));
			element = doc.getDocumentElement();
		} catch (ParserConfigurationException e) {
			System.err.println("Errore nella configurazione del parser: Artifact");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException | IOException e) {
			System.err.println("Errore nella creazione del documento: Artifact");
			e.printStackTrace();
			System.exit(1);
		}
		
		
		//Tramite l'Element mi creo un SAMLObject ArtifactResponse
		ArtifactResponse artifactResponse = null;
		try {
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
			artifactResponse = (ArtifactResponse) unmarshaller.unmarshall(element);
		} catch (UnmarshallingException e) {
			System.err.println("Errore nell'unmarshalling");
			e.printStackTrace();
			System.exit(1);
		}
		
		return getArtifactAssertion(artifactResponse);
    }

	private Assertion getArtifactAssertion(ArtifactResponse artifactResponse) throws MarshallingException {
    	//Prendiamo i valori che ci possono interessare contenuti nella response
    	
    	//Otteniamo l'issuer del response
    	Issuer issuer = (Issuer) artifactResponse.getIssuer();
    	//Passo in stringa
    	IssuerMarshaller issuerMarshaller = new IssuerMarshaller();
    	Element plainTextIssuer = issuerMarshaller.marshall(issuer);
    	String issuerString = plainTextIssuer.getTextContent();
    	
    	//Otteniamo il momento del rilascio del response
    	DateTime issueInstant = artifactResponse.getIssueInstant();
    	
    	//Otteniamo lo status code del response
    	String statusCode = artifactResponse.getStatus().getStatusCode().getValue();
    	
    	/*
    	if(statusCode != StatusCode.SUCCESS_URI) {
    		return data;
    	}
    	*/
    	
    	//Otteniamo il response alla nostra richiesta
    	Response response = (Response) artifactResponse.getMessage();
    	
		//Converto la richiesta in stringa
		ResponseMarshaller marsh = new ResponseMarshaller();
		Element plainTextElement = marsh.marshall(response);
		String originalResponse = XMLHelper.nodeToString(plainTextElement);
		
		System.out.println("Original:" + originalResponse);
    	
		SAMLAssertion samlAssertion = new SAMLAssertion();
		
    	return samlAssertion.getAssertion(originalResponse);
    }

}
