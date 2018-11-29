package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IdTokenHeader {
	private String typ;
	private String alg;
	private String kid;

	@JsonCreator
	public IdTokenHeader(@JsonProperty("typ") String typ, 
			@JsonProperty("alg") String alg,
			@JsonProperty("kid") String kid){		
		
		this.typ = typ;
		this.alg = alg;
		this.kid = kid;
	}

	public String getTyp() {
		return typ;
	}

	public String getAlg() {
		return alg;
	}

	public String getKid() {
		return kid;
	}
	
	@Override
	public String toString() {
		return "{\r\n" + 
				"    \"kid\": \"" + kid + "\",\r\n" + 
				"    \"alg\": \"" + alg + "\",\r\n" + 
				"    \"typ\": \"" + typ + "\"\r\n" +
				"}";
	}

}
