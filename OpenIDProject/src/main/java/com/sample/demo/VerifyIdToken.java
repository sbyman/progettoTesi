package com.sample.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VerifyIdToken {
	
	private static IdTokenHeader header;
	private static IdTokenBody body;
	private static byte[] signature;
	
	public static boolean isValid(String idToken) {
		
		getElements(idToken);
		System.out.println("Header: \n" + header.toString());
		System.out.println("Body: \n" + body.toString());
		System.out.println("Signature: \n" + new String(signature));
		
		
		return true;
	}
	
	private static void getElements(String idToken) {
		String encodedHeader = "";
		String encodedBody = "";
		String encodedSignature = "";
		int counter = 0;
		
		for (char c : idToken.toCharArray()) {
			if(counter == 0) {
				if(c != '.') {
					encodedHeader = encodedHeader + c;
				}
				else {
					counter++;
				}
			}
			else if (counter == 1) {
				if(c != '.') {
					encodedBody = encodedBody + c;
				}
				else {
					counter++;
				}
			}
			else {
				encodedSignature = encodedSignature + c;
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			header = mapper.readValue(Base64.decodeBase64(encodedHeader), IdTokenHeader.class);
			body = mapper.readValue(Base64.decodeBase64(encodedBody), IdTokenBody.class);
			signature = Base64.decodeBase64(encodedSignature);
			
			/*
			 * Bisogna aggiungere la firma che si trova
			 * all'endpoint jwk di gluu ordinata
			 * ordinata tramite il kid
			 * e codificata in base64
			 */
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = digest.digest((encodedHeader + "." + encodedBody).getBytes(StandardCharsets.UTF_8));
			
			System.out.println("Hash generato: \n" + encodedHash);
			
			if(encodedHash.equals(encodedSignature.getBytes(StandardCharsets.UTF_8))) {
				//Hello!
			}
			

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
