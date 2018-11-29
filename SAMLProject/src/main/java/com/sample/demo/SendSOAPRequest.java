package com.sample.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.soap.client.BasicSOAPMessageContext;
import org.opensaml.ws.soap.client.http.HttpClientBuilder;
import org.opensaml.ws.soap.client.http.HttpSOAPClient;
import org.opensaml.ws.soap.common.SOAPException;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Node;

public class SendSOAPRequest {

	
	public static String createRequestEnvelope(Assertion assertion, Body message, String destination) {
		
		//Crea l'envelope per la nostra richiesta di artifact resolve
		Envelope envelope;
		String responseString = null;
		try {
			envelope = OpenSAMLUtils.wrapInSOAPEnvelope(message, assertion);
			//Definisce un contesto e il messaggio da inviare in uscita
			BasicSOAPMessageContext soapContext = new BasicSOAPMessageContext();
			soapContext.setOutboundMessage(envelope);
			HttpClientBuilder httpClientBuilder = new HttpClientBuilder();
			//Definisce un nuovo client per inviare il messaggio
			HttpSOAPClient soapClient = new HttpSOAPClient(httpClientBuilder.buildClient(), new BasicParserPool());
			
			String requestURL = destination;
			
			//Invia il messaggio
			soapClient.send(requestURL, soapContext);
			
			//Ottiene il messaggio in risposta, lo estrae dall'envelope e lo trasforma in stringa
			Envelope responseEnvelope = (Envelope) soapContext.getInboundMessage();
			Node responseElement = responseEnvelope.getBody().getDOM().getFirstChild();;
			responseString = XMLHelper.nodeToString(responseElement);

			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter("soapResponse.xml"));
			writer.write(responseString);
			writer.close();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//(DEBUG) Stampa la risposta sulla console
		System.out.println(responseString);
		
		return responseString;
	}
}
