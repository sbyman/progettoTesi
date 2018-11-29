package com.sample.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.saml2.core.ArtifactResolve;
import org.opensaml.saml2.core.impl.ArtifactResolveMarshaller;
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
public class ArtifactController {
	
	private String encodePOSTFormat(String samlXML) throws IOException {
		String base64 = Base64.encodeBase64String(samlXML.getBytes()); // Codifico in base 64
		return URLEncoder.encode(base64, "UTF-8"); // Codifico in URL
	}

	@RequestMapping("/artifact")
	public RedirectView artifact(@RequestParam("SAMLart") String SAMLart, @RequestParam("RelayState") String RelayState) {
		SAMLArtifactResolve req = new SAMLArtifactResolve();
		SAMLInputContainer input = null;
		RedirectView redirectView = new RedirectView();		
		
		try {
			//Crea l'input container con i parametri per una redirect
			input = new SAMLInputContainer();
			
			input.setIssuer("https://192.168.1.13:8443/metadata");
			input.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:SOAP");
			input.setDestination("https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/SOAP/ArtifactResolution");
			
			//Crea la richiesta
			ArtifactResolve artifactResolve = req.createArtifactResolve(input, SAMLart);
			
			//Converto la richiesta in stringa
			ArtifactResolveMarshaller marsh = new ArtifactResolveMarshaller();
			Element plainTextElement = marsh.marshall(artifactResolve);
			String originalArtifact = XMLHelper.nodeToString(plainTextElement);
			
			//(DEBUG) Scrivo la richiesta su file
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("artifactRequest.xml"));
				String stringResponse = XMLHelper.nodeToString(plainTextElement);
				writer.write(stringResponse);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//(DEBUG) Stampa la richiesta sulla console
			System.out.println("ArtifactResolve:\n" + originalArtifact);
			
			//Crea una nuova richiesta per inviare un artefatto come SOAP
			SendSOAPArtifact sea = new SendSOAPArtifact();
			String responseString = sea.createArtifactEnvelope(artifactResolve, input);
			
			//Codifica la richiesta
			String encodedResponseString = encodePOSTFormat(responseString);

			redirectView.setUrl("https://localhost:8443/response?SAMLResponse=" + encodedResponseString);
			
			return redirectView;
			
		} catch (ConfigurationException e) {
			System.err.println("Errore nella creazione della richiesta");
			e.printStackTrace();
			System.exit(1);
		} catch (MarshallingException e) {
			System.err.println("Errore marshalling della richiesta");
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			System.err.println("Errore di accesso illegale");
			e.printStackTrace();
			System.exit(1);
		} catch (SOAPException e) {
			System.err.println("Errore di eccezione nel SOAP");
			e.printStackTrace();
			System.exit(1);
		} catch (SecurityException e) {
			System.err.println("Errore di sicurezza");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Errore nella codifica");
			e.printStackTrace();
			System.exit(1);
		}
		
		redirectView.setUrl("https://localhost:8443/errors?error=blabla");
		
		return redirectView;
		
	}

}
