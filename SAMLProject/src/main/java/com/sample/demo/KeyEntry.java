package com.sample.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyEntry {
	
	private PrivateKeyEntry getKeyEntry() {
		
		//Carica il keystore e lo ritorna
		KeyStore.PrivateKeyEntry keyEntry = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("src/main/resources/samlspsignkey.jks"), "changeit".toCharArray());
			keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry
					("samlspsignkey", new KeyStore.PasswordProtection("changeit".toCharArray()));
			
		} catch (KeyStoreException e) {
			System.err.println("Eccezione nell'istanziare il keystore");
			e.printStackTrace();
			System.exit(0);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Eccezione nel caricare il keystore - algoritmo");
			e.printStackTrace();
			System.exit(0);
		} catch (CertificateException e) {
			System.err.println("Eccezione nel caricare il keystore - certificato");
			e.printStackTrace();
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.err.println("Eccezione nel caricare il keystore - file non trovato");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Eccezione nel caricare il keystore - eccezione di IO");
			e.printStackTrace();
			System.exit(0);
		} catch (UnrecoverableEntryException e) {
			System.err.println("Alias non trovato all'interno del keystore");
			e.printStackTrace();
			System.exit(0);
		}
		
		return keyEntry;
	}
	
	public String getStringCertificate() {
		//Ottiene il certificato dal keystore e lo ritorna come stringa
		X509Certificate cert = (X509Certificate) getKeyEntry().getCertificate();
		System.out.println(cert);
		return cert.toString();
	}
	
	public String getStringPrivateKey() {
		//Carica la chiave privata dal keystore e la ritorna come stringa
		PrivateKey privateKey = getKeyEntry().getPrivateKey();
		return privateKey.toString();
	}
	
	public PrivateKey getPrivateKey() {
		//Ritorna la chiave privata
		return getKeyEntry().getPrivateKey();
	}
	
	public X509Certificate getX509Certificate() {
		//Ritorna il certificato
		return (X509Certificate) getKeyEntry().getCertificate();
	}

}
