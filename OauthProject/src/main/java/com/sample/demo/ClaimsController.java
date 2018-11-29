package com.sample.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class ClaimsController {

	@RequestMapping("/claims")
	@ResponseBody
	public Claims getClaims(@RequestParam("code") String code){
		
		//Crea la url per l'endpoint
		URI claimsEndpoint= null;
		try {
			claimsEndpoint = new URI("https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/userinfo");
		} catch (URISyntaxException e) {
			System.err.println("Errore di creazione URI");
			e.printStackTrace();
			System.exit(0);
		}
		
		RestTemplate restTemplate = new RestTemplate();
		
		//Prepara l'authorization header
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", "Bearer " + code);
		
		//Prepara la richiesta (le specifica consigliano una GET quindi body = null
		RequestEntity<String> request = new RequestEntity<String>(null, headers, HttpMethod.GET, claimsEndpoint);
		ResponseEntity<Claims> response = null;
		
		//Invio la GET e prendo il JSON in response
		response = restTemplate.exchange(request, Claims.class);
		
		//(DEBUG) Stampa su console la response
		System.out.println("Response:\n" + response.getBody().toString());
		
		return response.getBody();
	
	}
}
