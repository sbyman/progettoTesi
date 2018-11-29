package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Json {

	private String at;
	private String exp;
	private String type;

	@JsonCreator
	public Json(@JsonProperty("access_token") String at, @JsonProperty("expires_in") String exp, @JsonProperty("toke_type") String type) {
		this.at = at;
		this.exp = exp;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public String getAt() {
		return at;
	}

	public String getExp() {
		return exp;
	}
	
	@Override
	public String toString() {
		return "{\n"
				+ "\"access_token:\"" + at + ",\n"
				+ "\"expires_in:\"" + exp + ",\n"
				+ "\"token_type:\"" + type + "\n"
				+ "}";
	}

}
