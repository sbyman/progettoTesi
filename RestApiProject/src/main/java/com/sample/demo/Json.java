package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Json {

	private String clientId;
	private String active;
	private String[] scopes;
	private String username;
	private String tokenType;
	private String aud;
	private String iss;
	private String iat;
	private String exp;

	@JsonCreator
	public Json(@JsonProperty("client_id") String clientId, @JsonProperty("exp") String exp,
			@JsonProperty("token_type") String tokenType, @JsonProperty("active") String active,
			@JsonProperty("scopes") String[] scopes, @JsonProperty("username") String username,
			@JsonProperty("aud") String aud, @JsonProperty("iss") String iss,
			@JsonProperty("iat") String iat) {
		this.clientId = clientId;
		this.exp = exp;
		this.tokenType = tokenType;
		this.active = active;
		this.aud = aud;
		this.iat = iat;
		this.iss = iss;
		this.scopes = scopes;
		this.username = username;
	}


	public String getClientId() {
		return clientId;
	}


	public String getActive() {
		return active;
	}


	public String[] getScope() {
		return scopes;
	}


	public String getUsername() {
		return username;
	}


	public String getTokenType() {
		return tokenType;
	}


	public String getAud() {
		return aud;
	}


	public String getIss() {
		return iss;
	}


	public String getIat() {
		return iat;
	}


	public String getExp() {
		return exp;
	}

	@Override
	public String toString() {
		String scp = "    \"scopes\": [ ";
		for(int i = 0; i < scopes.length; i++) {
			scp = scp + scopes[i] + " ";
		}

		scp = scp + "],\r\n";
		
		return "{\r\n" + 
				"    \"active\": \"" + active + "\",\r\n" + 
				scp +
				"    \"client_id\": " + clientId + " ,\r\n" + 
				"    \"username\": \"" + username + "\",\r\n" + 
				"    \"token_tyscopespe\": \"" + tokenType + "\",\r\n" + 
				"    \"exp\": \"" + exp + "\",\r\n" + 
				"    \"iat\": \"" + iat + "\",\r\n" + 
				"    \"iss\": " + iss + ",\r\n" + 
				"    \"aud\": \"" + aud + "\"\r\n" + 
				"}";
	}
}