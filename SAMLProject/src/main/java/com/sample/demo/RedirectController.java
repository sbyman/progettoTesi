package com.sample.demo;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
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
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Element;

@Controller
public class RedirectController {
	
	private String encodeRedirectFormat( String samlXML ) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Deflater deflater = new Deflater( Deflater.DEFAULT_COMPRESSION, true ); //La comprimo (richiesto solo per il redirect)
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(os, deflater);
        deflaterOutputStream.write( samlXML.getBytes( "UTF-8" ) );
        deflaterOutputStream.close();
        os.close();
        String base64 = Base64.encodeBase64String(os.toByteArray()); //Codifico in base 64
        return URLEncoder.encode(base64, "UTF-8"); //Codifico in URL
	}
	
	@RequestMapping("/redirect")
	public RedirectView redirectRequest() {
		
		SAMLAuthnRequest req = new SAMLAuthnRequest();
		String encodedRequest = "";
		SAMLInputContainer input = null;
		String encodedSigAlg = null;
		
		try {
			//Crea l'input container con i parametri per una redirect
			input = new SAMLInputContainer();
			
			input.setIssuer("https://192.168.1.13:8443/metadata");
			input.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
			input.setDestination("https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/Redirect/SSO");
			input.setAssertionConsumerServiceURL("https://192.168.1.13:8443/response");
			
			//Crea la richiesta
			AuthnRequest authnRequest = req.createAssertion(input, "redirect");
			
			//Converto la richiesta in stringa
			AuthnRequestMarshaller marsh = new AuthnRequestMarshaller();
			Element plainTextElement = marsh.marshall(authnRequest);
			String originalAuthn = XMLHelper.nodeToString(plainTextElement);
			
			//(DEBUG) Scrivo la richiesta su file
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
			
			//(DEBUG) Stampa la richiesta sulla console
			System.out.println("AuthnRequest:\n" + originalAuthn);
			
			//Comprime, codifica in base 64 e codifica in url la richiesta
			encodedRequest = encodeRedirectFormat(originalAuthn);

			//Comprime, codifica in base 64 e codifica in url l'algoritmo utilizzato per la firma
			encodedSigAlg = encodeRedirectFormat("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
			
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
		
		//Prepara la concatenazione di elementi che andranno firmati
		String signableRequest = ("SAMLRequest=" + encodedRequest + "&SigAlg=" + encodedSigAlg);
		
		//(DEBUG) Mostra gli elementi concatenati sulla console
		System.out.println("Richiesta e algoritmo\n" + signableRequest);

		//Crea un nuovo keyEntry per ottenere successivamente una chiave privata
		KeyEntry keyEntry = new KeyEntry();
		
		
		//Firma la concatenazione in rsa-sha1
		Signature instance;
		byte[] signature = null;
		try {
			instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(keyEntry.getPrivateKey());
			instance.update(signableRequest.getBytes());
			signature = instance.sign();
			
			//(DEBUG) Mostra la firma sulla console
			System.out.println("Firma digitale della concatenazione:\n" + new String(signature, "UTF-8"));

		} catch (NoSuchAlgorithmException e) {
			System.err.println("Algoritmo non esistente");
			e.printStackTrace();
			System.exit(1);
		} catch (InvalidKeyException e) {
			System.err.println("Chiave non valida");
			e.printStackTrace();
			System.exit(1);
		} catch (SignatureException e) {
			System.err.println("Errore nella creazione della firma");
			e.printStackTrace();
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Codifica non supportata");
			e.printStackTrace();
			System.exit(1);
		}
		
		//Comprime, codifica in base 64 e codifica in url la firma digitale
		String encodedSignature = null;
		try {
			encodedSignature = encodeRedirectFormat(new String(signature, "UTF-8"));
			
			//(DEBUG) Mostra la firma codificata su console
			System.out.println("Firma digitale codificata:\n" + encodedSignature);
		} catch (IOException e) {
			System.err.println("Errore nella codifica della firma");
			e.printStackTrace();
			System.exit(1);
		}

		//(DEBUG) Mostra la pagina alla quale si sta per reindirizzare il browser
		System.out.println("Indirizzo:\n" + input.getDestination() + "?SAMLRequest=" + encodedRequest + "&Signature=" + encodedSignature);
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(input.getDestination() + "?SAMLRequest=" + encodedRequest + "&Signature=" + encodedSignature);
		
		return redirectView;

	}
	
}
