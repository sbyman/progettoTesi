package com.sample.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class StringToXml {

	public static XMLObject stringToXml(String resp) {
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
			System.err.println("Errore nella configurazione del parser");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException | IOException e) {
			System.err.println("Errore nella creazione del documento");
			e.printStackTrace();
			System.exit(1);
		}

		// Tramite l'Element mi creo un XMLObject
		XMLObject response = null;
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
		response = (XMLObject) unmarshaller;
		
		return response;
	}

}
