package com.sample.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.AssertionUnmarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sample.demo.assertion.AssertionType;
import com.sample.demo.assertion.ObjectFactory;
import com.sample.demo.wsse.SecurityHeaderType;

public class SAMLAssertionResponse {
	
	private Assertion assertion;
	
	public SAMLAssertionResponse() {}
	
	public SAMLAssertionResponse(Assertion assertion) {
		this.assertion = assertion;
	}

	public Assertion getAssertion() {
		return assertion;
	}

	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}
	
	public void writeAssertion() {
		AssertionMarshaller marshaller = new AssertionMarshaller();
		Element assertionElement = null;
		try {
			assertionElement = marshaller.marshall(assertion);
		} catch (MarshallingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/assertions/assertion.xml"));
			String stringResponse = XMLHelper.nodeToString(assertionElement);
			writer.write(stringResponse);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public AssertionType readAssertion(String location) {
		/*
		 * In questo caso l'unmarshalling si fa con JAXB
		 */
		
		
        JAXBContext context;
        AssertionType assertion = null;
        
		try {
			context = JAXBContext.newInstance(AssertionType.class);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	       
	        JAXBElement<AssertionType> assertionElement = unmarshaller.unmarshal(new StreamSource(new FileInputStream(location)), AssertionType.class);
	        assertion = assertionElement.getValue();
	        
		} catch (JAXBException e) {
			System.err.println("Errore nell'Unmarshalling dell'asserzione");
			e.printStackTrace();
			System.exit(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return assertion;

		
		/*
		//Leggo l'assersione e la scrivo in una stringa
		String content = "";
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(location));
			reader.readLine();
			while(reader.ready()) {
				content = content + reader.readLine();
			}
			reader.close();

		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(content);
		
		//Lo passo in elemento
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		Element element = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(content.getBytes("UTF-8"))));
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
		
		AssertionUnmarshaller unmarshaller = new AssertionUnmarshaller();
		Assertion assertion = null;
		try {
			assertion = (Assertion) unmarshaller.unmarshall(element);
		} catch (UnmarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  assertion;

		*/
		
	}
	
}
