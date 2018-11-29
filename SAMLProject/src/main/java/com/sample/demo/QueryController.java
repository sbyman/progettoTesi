package com.sample.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.impl.AttributeQueryMarshaller;
import org.opensaml.ws.soap.common.SOAPException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Element;

@Controller
public class QueryController {
	
	private String encodePOSTFormat(String samlXML) throws IOException {
		String base64 = Base64.encodeBase64String(samlXML.getBytes()); // Codifico in base 64
		return URLEncoder.encode(base64, "UTF-8"); // Codifico in URL
	}

	
	@RequestMapping("/query")
	public RedirectView query(@RequestParam("user") String nameIdUser) {
		
		SAMLInputContainer input = new SAMLInputContainer();
		input.setDestination("https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/SOAP/AttributeQuery");
		input.setIssuer("https://192.168.43.87:8443/metadata");
		
		SAMLAttributeQuery req = new SAMLAttributeQuery();
		RedirectView redirectView = new RedirectView();
		
		byte[] decNameId = Base64.decodeBase64(nameIdUser);
		String decodedNameId = new String(decNameId);
		
		
		try {
			AttributeQuery attributeQuery = req.createQuery(input, decodedNameId);
			
			//Converto la richiesta in stringa
			AttributeQueryMarshaller marsh = new AttributeQueryMarshaller();
			Element plainTextElement = marsh.marshall(attributeQuery);
			String originalAttributeQuery = XMLHelper.nodeToString(plainTextElement);
			
			//(DEBUG) Scrivo la richista su file
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("query.xml"));
				String stringResponse = XMLHelper.nodeToString(plainTextElement);
				writer.write(stringResponse);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//(DEBUG) Stampa la richiesta sulla console
			System.out.println("AttributeQuery:\n" + originalAttributeQuery);
		
			//Crea una nuova richiesta per inviare un artefatto come SOAP
			SendSOAPArtifact sea = new SendSOAPArtifact();
			String responseString = sea.createAttributeEnvelope(attributeQuery, input);
			
			//Codifica la richiesta
			String encodedResponseString = encodePOSTFormat(responseString);

			//Redirect verso la pagina che gestisce la response
			redirectView.setUrl("https://localhost:8443/user?SAMLResponseQuery=" + encodedResponseString);
			
			return redirectView;

			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		redirectView.setUrl("https://localhost:8443/errors?error=blabla");
		
		return redirectView;
	}

}
