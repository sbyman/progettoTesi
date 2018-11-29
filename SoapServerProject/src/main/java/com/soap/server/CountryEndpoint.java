package com.soap.server;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sample.demo.assertion.AssertionType;
import com.sample.demo.assertion.AttributeStatementType;
import com.sample.demo.assertion.AttributeType;
import com.sample.demo.assertion.ObjectFactory;
import com.sample.demo.assertion.StatementAbstractType;
import com.sample.demo.wsse.SecurityHeaderType;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.GetCountryResponse;

@Endpoint
public class CountryEndpoint {
	private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

	private CountryRepository countryRepository;

	@Autowired
	public CountryEndpoint(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	@ResponsePayload
	public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request,
			@SoapHeader("{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}Security") SoapHeaderElement header)
			throws MarshallingException {
		
		GetCountryResponse response = new GetCountryResponse();
		SecurityHeaderType security = getCredential(header);
		AssertionType assertion = null;
		String assertionString = "";

		for (int i = 0; i < security.getAny().size(); i++) {
			System.out.println("Sono in posizione: " + i);

			try {
				/*
				 * Creo un contesto e marshallo l'asserzione
				 * così da poter sfruttare le librerie opensaml per verificare
				 * il contenuto della stessa
				 * 
				 */
				
				JAXBContext context = JAXBContext.newInstance(AssertionType.class);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				JAXBElement<AssertionType> assertionElement = (JAXBElement<AssertionType>) security.getAny().get(i);
				assertion = (AssertionType) assertionElement.getValue();

				ObjectFactory objectWsse = new ObjectFactory();

				JAXBElement<AssertionType> assertionElementToWrite = objectWsse.createAssertion(assertion);

				//Stampiamo il messaggio prima che parta
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
				marshaller.marshal(assertionElementToWrite, out);
	            assertionString = new String(out.toByteArray());
	            
	            System.out.println(assertionString);

			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

		if (isAdmin(assertionString)) {
			System.out.println("E' un admin");
			response.setCountry(countryRepository.findCountry(request.getName()));
		} else {
			System.out.println("Non e' un admin");
			response.setCountry(countryRepository.findCountry("Spain"));
		}

		return response;

	}

	private SecurityHeaderType getCredential(SoapHeaderElement header) {

		JAXBContext context;
		SecurityHeaderType security = null;

		if (header == null) {
			System.out.println("L'header e' null");
			return null;
		}

		try {
			context = JAXBContext.newInstance(SecurityHeaderType.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			JAXBElement<SecurityHeaderType> securityHeaderTypeElement = unmarshaller.unmarshal(header.getSource(),
					SecurityHeaderType.class);
			security = securityHeaderTypeElement.getValue();

		} catch (JAXBException e) {
			System.err.println("Errore nell'Unmarshalling dell'asserzione");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Ritorno il SecurityHeader");
		return security;
	}

	/*
	 * Parte di codice per verifica effettuata tramite JAXB
	 * NON FUNZIONANTE
	 */
	private boolean isAdmin(AssertionType assertion) {

		if (assertion == null) {
			System.out.println("L'asserione e' vuota");
			return false;
		}

		StatementAbstractType statementAbstract = null;
		AttributeStatementType attributeStatements = new AttributeStatementType();
		for (int i = 0; i < assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement().size(); i++) {
			statementAbstract = assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement().get(i);
			if (statementAbstract.getClass().equals(AttributeStatementType.class)) {
				System.out.println("Ho trovato gli attributi");
				attributeStatements = (AttributeStatementType) statementAbstract;
				break;
			}
		}

		List<AttributeType> attributeList = attributeStatements.getAttribute();
		for (int i = 0; i < attributeList.size(); i++) {
			AttributeType attribute = attributeList.get(i);
			System.out.println("FriendlyName attuale: " + attribute.getFriendlyName());

			if (attribute.getFriendlyName().equals("uid")) {
				System.out.println("Ho trovato il friendly name");
				List<Object> attributeValueList = attribute.getAttributeValue();
				for (int j = 0; j < attributeValueList.size(); j++) {
					Node value = (Node) attributeValueList.get(j);
					System.out.println("Nome del nodo: " + value.getNodeName());
					System.out.println("Valore del nodo: " + value.getNodeValue());
					if (value.equals("admin")) {
						System.out.println("Valore e' admin");
						return true;
					}
				}
			}

		}
		System.out.println("Non e' un admin oppure non si e' autenticato");
		return false;

	}
	
	/*
	 * Verifica effettuata con OpenSAML
	 * FUNZIONANTE
	 */
	public boolean isAdmin(String assertionString) {
        SAMLAssertion samlAssertion = new SAMLAssertion();
        Element assertion = samlAssertion.convertAssertion(assertionString);
        
        try {
			Map<String, String> attributeMap = samlAssertion.unbuildAssertion(assertion);
			if(attributeMap.get("uid").equals("admin")) {
				System.out.println("Coincidono");
				return true;
			}
			
		} catch (org.opensaml.xml.io.MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Non coincidono");
        return false;
	}

}