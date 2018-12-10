package com.sample.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Random;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Element;

@RestController
public class PostController {

	private String encodePOSTFormat(String samlXML) throws IOException {
		return Base64.encodeBase64String(samlXML.getBytes()); // Codifico in base 64
	}
	
    public String generateRandomHexString(int length) {
    	Random random = new Random();
    	
    	StringBuilder sb = new StringBuilder();
    	
    	while (sb. length() < length) {
    		sb. append(Integer. toHexString(random. nextInt()));
    	}
    		
    	return sb.toString();
    }
    
	@RequestMapping("/request")
	public String redirectRequest() throws MalformedURLException, IOException, URISyntaxException {
		
		SAMLAuthnRequest req = new SAMLAuthnRequest();
		String encodedRequest = "";
		SAMLInputContainer input = null;
		try {
			//Crea l'input container con i parametri per una redirect
			input = new SAMLInputContainer();
			
			input.setIssuer("https://192.168.1.7:8443/metadata");
			input.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact");
			input.setDestination("https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/POST/SSO");
			input.setAssertionConsumerServiceURL("https://192.168.1.7:8443/artifact");
			
			//Crea la richiesta
			AuthnRequest authnRequest = req.createAssertion(input, "POST");
			
			//Converto la richiesta in stringa
			AuthnRequestMarshaller marsh = new AuthnRequestMarshaller();
			Element plainTextElement = marsh.marshall(authnRequest);
			String originalAuthn = XMLHelper.nodeToString(plainTextElement);
			
			//(DEBUG) Scrivo la richiesta di File
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("requestPost.xml"));
				String stringResponse = XMLHelper.nodeToString(plainTextElement);
				writer.write(stringResponse);
				writer.close();
			} catch (IOException e) {
				System.err.println("Errore nella creazione del documento");
				e.printStackTrace();
				System.exit(1);
			}
			
			//(DEBUG) Stampa la richiesta sulla console
			System.out.println("AuthnRequest:\n" + originalAuthn);
			
			//Codifica in base 64 la richiesta
			encodedRequest = encodePOSTFormat(originalAuthn);
			
			//(DEBUG) Stampa la richiesta codificata sulla console
			System.out.println("Richiesta codificata: " + encodedRequest);
			
		} catch (ConfigurationException e) {
			System.err.println("Errore nella creazione della richiesta");
			e.printStackTrace();
			System.exit(1);
		} catch (MarshallingException e) {
			System.err.println("Errore marshalling della richiesta");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Errore nella codifica della richiesta");
			e.printStackTrace();
			System.exit(1);

		}		

		//POST molto brutta verso l'endpoint
		return  "	<form name=\"autoform\" method=\"post\" action=\"" + input.getDestination() + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + encodedRequest + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"RelayState\" value=\"" + generateRandomHexString(8) + "\">\r\n" + 
				"	</form>" +
				"<script type=\"text/javascript\">\r\n" + 
				"    document.autoform.submit();\r\n" + 
				"</script>";
	}
	
}
