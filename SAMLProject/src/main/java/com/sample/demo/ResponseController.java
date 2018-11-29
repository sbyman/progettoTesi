package com.sample.demo;

import java.io.IOException;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Element;

import com.sample.demo.assertion.AssertionType;

import hello.wsdl.GetCountryResponse;

@Controller
public class ResponseController {
	
	private User user;
	private Map<String, String> responseContent;
	private SAMLAssertionResponse assertionResponse = new SAMLAssertionResponse();
	
	private String decodePOSTFormat(byte[] samlResponse) throws IOException{
		byte[] base64 = Base64.decodeBase64(samlResponse);
		return new String(base64);
	}

	public Map<String, String> unbuildResponse(String response, boolean check){
		//(DEBUG) Stampa la response su console
		System.out.println(response);
		String decodedResponse = "";
		try {
			//Decodifica la response
			decodedResponse = decodePOSTFormat(response.getBytes());
			
			//(DEBUG) Stampa la response decodificata a schermo
			System.out.println(decodedResponse);
			
		} catch (IOException e) {
			System.err.println("Errore nella decodifica della richiesta");
			e.printStackTrace();
			System.exit(1);

		}
		//Controlla il tipo di response ricevuto
		try {
			if(check == true) {
				//Legge la response
				responseContent = new SAMLResponse().unbuildResponse(decodedResponse, user = new User(), assertionResponse);
			}
			else {
				//Legge l'artifact response
				responseContent = new SAMLArtifactResponse().unbuildResponse(decodedResponse, user = new User(), assertionResponse);
			}
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseContent;

	}
	
	public RedirectView redirect(String nameId) {
		RedirectView redirectView = new RedirectView();
		
		String encodedNameId = Base64.encodeBase64URLSafeString(nameId.getBytes());
		
		redirectView.setUrl("https://localhost:8443/query?user=" + encodedNameId );
		
		return redirectView;
	}
	
	@RequestMapping(value = "/response", method = RequestMethod.POST)
	@ResponseBody
	public String response(@RequestParam(value = "SAMLResponse") String response) {
		
		
		responseContent = unbuildResponse(response, true);
		
		stampMap(responseContent);
		System.out.println("SamlID: " + user.getSamlNameId());
		
		//return redirect(user.getSamlNameId());Il contenuto non è consentito nel prologo.
		
		return responseContent.toString();
	}
	
	@RequestMapping(value = "/response", method = RequestMethod.GET)
	@ResponseBody
	public String artifactResponse(@RequestParam(value = "SAMLResponse") String artifactResponse) {
		
		
		responseContent = unbuildResponse(artifactResponse, false);
		
		stampMap(responseContent);
		if(user.getSamlNameId() == null) {
			System.out.println("Nessun utente trovato. Nessun response ottenuto (Errore artifact)");
		}
		System.out.println(user.getSamlNameId());
		
		/* Errore: Il contenuto non è consentito nel prologo
		SAMLArtifactAssertion samlArtifactAssertion = new SAMLArtifactAssertion();
		Assertion assertion = null;
		try {
			assertion = samlArtifactAssertion.getArtifactAssertion(artifactResponse);
		} catch (MarshallingException e) {
			System.err.println("Errore nel marshalling dell'asserzione");
			e.printStackTrace();
		}
		*/
		
		SAMLAssertionResponse assertionResponse = new SAMLAssertionResponse();
		AssertionType assertion = assertionResponse.readAssertion("src/main/resources/assertions/assertion.xml");
		
		String reference = "https://192.168.1.19:8443/assertion";
		
		//Richiedi la risorsa
		CountryConfiguration countryConfiguration = new CountryConfiguration();
		CountryClient quoteClient = countryConfiguration.countryClient(countryConfiguration.marshaller());
		GetCountryResponse response = quoteClient.getCountry("Poland", assertion);
		
		/*
		String bodyString = "<getCountryRequest>"
				+ "<name>Spain</name>"
				+ "</getCountryRequest>";
		
		
		Body body = (Body)StringToXml.stringToXml(bodyString);
		
		String response = SendSOAPRequest.createRequestEnvelope(assertionResponse.getAssertion(), body, "http://soap-server.com:8443");
		*/
		
		System.out.println("Nome del paese: " + response.getCountry().getName());
		
		
		return responseContent.toString();
	}
	
	private void stampMap(Map<String, String> map) {
		
		for (Map.Entry<String, String> entry : map.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}
}
