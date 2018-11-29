package com.sample.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class APIController {
	
	@RequestMapping("/getResource")
	public void getResource(@RequestParam("token") String token) {
		
		//Crea la redirect_uri
		String redirect_uri = "https://192.168.1.6:8443/resource";
		
		String body = "token=" + token + "&redirect_uri=" + redirect_uri;
		
		RestTemplate restTemplate = new RestTemplate();
		
		RequestEntity<String> request = null;
		
		try {
			request = new RequestEntity<String>(body, HttpMethod.POST, new URI("https://api-check.it:8443/check"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		System.out.println(response.getBody().toString());
		
	}
	
	@RequestMapping("/resource")
	@ResponseBody
	public String resource(@RequestParam("Response") String response) {
		
		byte[] decodedResponse = Base64.decodeBase64(response);
		System.out.println("Response:\n" + new String(decodedResponse));
		return new String(decodedResponse);
		
	}

}
