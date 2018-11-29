package com.sample.demo;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.SignableXMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Element;

public class SAMLSignature {

	private KeyEntry keyEntry;

	@SuppressWarnings("unused")
	public void putSignature(SignableXMLObject obj) throws ConfigurationException {

		keyEntry = new KeyEntry();

		// Prendi le credenziali dell'SP (certificato e chiave privata)
		BasicX509Credential bc = new BasicX509Credential();
		bc.setEntityCertificate(keyEntry.getX509Certificate());
		bc.setPrivateKey(keyEntry.getPrivateKey());

		// Creo l'elemento Signature
		SignatureBuilder signatureBuilder = new SignatureBuilder();
		Signature signature = (Signature) signatureBuilder.buildObject();
		signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
		signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
		signature.setSigningCredential(bc);
		
		//Imposta la configurazione di sicurezza
		SecurityConfiguration secConfig = Configuration.getGlobalSecurityConfiguration();
		BasicCredential basicCredential = new BasicCredential();
		basicCredential.setPrivateKey(keyEntry.getPrivateKey());
		Credential credential = bc;
		
		//Imposta la firma per l'oggetto xml
		obj.setSignature(signature);
		
		//Prepara i parametri, fa l'unmarshalling dell'oggetto e lo firma
		try {
			SecurityHelper.prepareSignatureParams(signature, credential, secConfig, null);
			Element element = Configuration.getMarshallerFactory().getMarshaller(obj).marshall(obj);
			Signer.signObject(signature);
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.opensaml.xml.signature.SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
