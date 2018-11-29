package com.sample.demo;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Element;

@Controller
public class NativeRequestController {

	public SAMLInputContainer input;
	
	private String encodeRedirectFormat(String samlXML) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true); // La comprimo (richiesto solo per il
																				// redirect)
		DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(os, deflater);
		deflaterOutputStream.write(samlXML.getBytes("UTF-8"));
		deflaterOutputStream.close();
		os.close();
		String base64 = Base64.encodeBase64String(os.toByteArray()); // Codifico in base 64
		return URLEncoder.encode(base64, "UTF-8"); // Codifico in URL
	}
	
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


	@RequestMapping("/native/request")
	public RedirectView redirectRequest(@RequestParam("protocolBinding") String protocolBinding, 
			@RequestParam("destination") String destination) {

		SAMLAuthnRequest req = new SAMLAuthnRequest();
		String encodedRequest = "";

		try {
			// Crea l'input container con i parametri per una redirect
			input = new SAMLInputContainer();

			input.setIssuer("https://192.168.1.13:8443/metadata");
			input.setProtocolBinding(protocolBinding);
			input.setDestination(destination);

			AuthnRequest authnRequest;

			// Crea la richiesta
			if (protocolBinding.equals("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST")) {

				input.setAssertionConsumerServiceURL("https://192.168.1.13:8443/native/response");

				authnRequest = req.createAssertion(input, "redirect");

				// Converto la richiesta in stringa
				AuthnRequestMarshaller marsh = new AuthnRequestMarshaller();
				Element plainTextElement = marsh.marshall(authnRequest);
				String originalAuthn = XMLHelper.nodeToString(plainTextElement);

				// (DEBUG) Scrivo la richiesta su file
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("requestRedirect.xml"));
					String stringResponse = XMLHelper.nodeToString(plainTextElement);
					writer.write(stringResponse);
					writer.close();
				} catch (IOException e) {
					System.err.println("Errore nella creazione del documento");
					e.printStackTrace();
					System.exit(1);
				}

				// (DEBUG) Stampa la richiesta sulla console
				System.out.println("AuthnRequest:\n" + originalAuthn);

				// Comprime, codifica in base 64 e codifica in url la richiesta
				encodedRequest = encodeRedirectFormat(originalAuthn);

				// (DEBUG) Mostra la pagina alla quale si sta per reindirizzare il browser
				System.out.println("Indirizzo:\n" + input.getDestination() + "?SAMLRequest=" + encodedRequest);

				RedirectView redirectView = new RedirectView();
				redirectView.setUrl(input.getDestination() + "?SAMLRequest=" + encodedRequest);

				return redirectView;

			} else {				
				input.setAssertionConsumerServiceURL("https://192.168.1.13:8443/native/artifact");

				authnRequest = req.createAssertion(input, "post");
				
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
				input.setSAMLRequest(encodedRequest);
				
				
				//(DEBUG) Stampa la richiesta codificata sulla console
				System.out.println("Richiesta codificata: " + encodedRequest);
				
				// (DEBUG) Mostra la pagina alla quale si sta per reindirizzare il browser
				System.out.println("Indirizzo:\n" + input.getDestination()+ "?SAMLRequest=" + encodedRequest);

				RedirectView redirectView = new RedirectView();
				redirectView.setUrl("https://localhost:8443/native/post");

				return redirectView;				
			}
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
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://localhost:8443/errors?error=blabla");

		return redirectView;
	}
	
	@RequestMapping("/native/post")
	@ResponseBody
	public String nativePost() {
		System.out.println("Destinazione: " + input.getDestination() + "\nAsserzione: " + input.getSAMLRequest());
		//POST molto brutta verso l'endpoint
		return  "	<form name=\"autoform\" method=\"post\" action=\"" + input.getDestination() + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + input.getSAMLRequest() + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"RelayState\" value=\"" + generateRandomHexString(8) + "\">\r\n" + 
				"	</form>" +
				"<script type=\"text/javascript\">\r\n" + 
				"    document.autoform.submit();\r\n" + 
				"</script>";
	}

}
