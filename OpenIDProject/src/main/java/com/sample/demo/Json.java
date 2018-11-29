package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Json {

	private String at;
	private String exp;
	private String id;
	private String type;
	private String scope;
	private String refresh;

	@JsonCreator
	public Json(@JsonProperty("access_token") String at, 
			@JsonProperty("expires_in") String exp,
			@JsonProperty("id_token") String id,
			@JsonProperty("token_type") String type,
			@JsonProperty("scope") String scope,
			@JsonProperty("refresh_token") String refresh){		
		
		this.at = at;
		this.exp = exp;
		this.id = id;
		this.type = type;
		this.scope = scope;
		this.refresh = refresh;
	}
	
	public String getRefresh() {
		return refresh;
	}

	public String getScope() {
		return scope;
	}

	public String getId() {
		return id;
	}
	
	public String getAt() {
		return at;
	}

	public String getExp() {
		return exp;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "{\r\n" + 
				"    \"access_token\": \"" + at + "\",\r\n" + 
				"    \"id_token\": \"" + id + "\",\r\n" + 
				"    \"token_type\": \"" + type + "\",\r\n" +
				"    \"scope\": \"" + scope + "\",\r\n" + 
				"    \"refresh_token\": \"" + refresh + "\",\r\n" + 
				"    \"expires_in\": \"" + exp + "\"\r\n" + 
				"}";
	}
}
