package com.sample.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IntrospectionController {

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public String postRegister(@RequestParam("token") String token){
		
		String redirect_uri = "https://192.168.43.87:8443/resource";
		
		//(DEBUG) Stampa il token su console
		System.out.println("Token ricevuto:\n" + token);

		//Prepara il body della POST
		String body = "token=" + token + "&token_type_hint=access_token";

		//Template REST per lo scambio di POST
		RestTemplate restTemplate = new RestTemplate();
		
		//(DEBUG) Stampa il body sulla console
		System.out.println("Body:\n" + body);
		
		//Prepara l'header per la POST
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Bearer " + token);
		headers.add("Accept", "application/json");
		
		//Crea la richiesta
		RequestEntity<String> request = null;
		try {
			request = new RequestEntity<String>(body, headers, HttpMethod.POST,
					new URI("https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/introspection"));
		} catch (URISyntaxException e) {
			System.err.println("Errore nella creazione della URL");
			e.printStackTrace();
			System.exit(0);
		}

		//(DEBUG) Stampa la richiesta a console
		System.out.println("Request:\n" + request);
		
		//Crea la response e fa lo scambio inviando la POST e aspettando un Json in risposta
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		//(DEBUG Stampa la reponse a console
		System.out.println("Response:\n" + response);
		
		//Codifico il response in Base64
		String encodedResponse = Base64.encodeBase64URLSafeString(response.getBody().getBytes());

		//POST molto brutta verso l'endpoint
		return "	<form name=\"autoform\" method=\"post\" action=\"" + redirect_uri + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"Response\" value=\"" + encodedResponse + "\">\r\n" + 
				"	</form>" +
				"<script type=\"text/javascript\">\r\n" + 
				"    document.autoform.submit();\r\n" + 
				"</script>"; 
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String getRegister(@RequestParam("token") String token, @RequestParam("redirect_uri") String encodedUri){
				
		String redirect_uri = new String(Base64.decodeBase64(encodedUri));
		
		//(DEBUG) Stampa il token su console
		System.out.println("Token ricevuto:\n" + token);

		//Prepara il body della POST
		String body = "token=" + token + "&token_type_hint=access_token";

		//Template REST per lo scambio di POST
		RestTemplate restTemplate = new RestTemplate();
		
		//(DEBUG) Stampa il body sulla console
		System.out.println("Body:\n" + body);
		
		//Prepara l'header per la POST
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Bearer " + token);
		headers.add("Accept", "application/json");
		
		//Crea la richiesta
		RequestEntity<String> request = null;
		try {
			request = new RequestEntity<String>(body, headers, HttpMethod.POST,
					new URI("https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/introspection"));
		} catch (URISyntaxException e) {
			System.err.println("Errore nella creazione della URL");
			e.printStackTrace();
			System.exit(0);
		}

		//(DEBUG) Stampa la richiesta a console
		System.out.println("Request:\n" + request);
		
		//Crea la response e fa lo scambio inviando la POST e aspettando un Json in risposta
		ResponseEntity<Json> response = restTemplate.exchange(request, Json.class);
		
		//(DEBUG Stampa la reponse a console
		System.out.println("Response:\n" + response.getBody().toString());
		
		//Codifico il response in Base64
		String encodedResponse = Base64.encodeBase64URLSafeString(response.getBody().toString().getBytes());

		//POST molto brutta verso l'endpoint
		return "	<form name=\"autoform\" method=\"post\" action=\"" + redirect_uri + "\">\r\n" + 
				"		<input type=\"hidden\" name=\"Response\" value=\"" + encodedResponse + "\">\r\n" + 
				"	</form>" +
				"<script type=\"text/javascript\">\r\n" + 
				"    document.autoform.submit();\r\n" + 
				"</script>"; 
	}
}
