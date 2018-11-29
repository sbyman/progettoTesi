package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IdTokenBody {

	private String iss;
	private String sub;
	private String aud;
	private String exp;
	private String iat;
	private String authTime;
	private String acr;

	@JsonCreator
	public IdTokenBody(@JsonProperty("iss") String iss, 
			@JsonProperty("sub") String sub,
			@JsonProperty("aud") String aud,
			@JsonProperty("exp") String exp,
			@JsonProperty("iat") String iat,
			@JsonProperty("auth_time") String authTime,
			@JsonProperty("acr") String acr){		
		
		this.iss = iss;
		this.sub= sub;
		this.aud = aud;
		this.exp = exp;
		this.iat = iat;
		this.authTime= authTime;
		this.acr = acr;
	}
	

	public String getIss() {
		return iss;
	}


	public String getSub() {
		return sub;
	}


	public String getAud() {
		return aud;
	}


	public String getExp() {
		return exp;
	}


	public String getIat() {
		return iat;
	}


	public String getAuthTime() {
		return authTime;
	}


	public String getAcr() {
		return acr;
	}


	@Override
	public String toString() {
		return "{\r\n" + 
				"    \"iss\": \"" + iss + "\",\r\n" + 
				"    \"sub\": \"" + sub + "\",\r\n" + 
				"    \"aud\": \"" + aud + "\",\r\n" +
				"    \"exp\": \"" + exp + "\",\r\n" + 
				"    \"iat\": \"" + iat + "\",\r\n" + 
				"    \"auth_time\": \"" + authTime + "\",\r\n" + 
				"    \"acr\": \"" + acr + "\"\r\n" + 
				"}";
	}
}
