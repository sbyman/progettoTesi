package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRequest {

	private String client_id;
	private String client_secret;
	private String redirect_uri;
	private String code;
	private String grant_type;

	public String getClient_id() {
		return client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public String getCode() {
		return code;
	}

	public String getGrant_type() {
		return grant_type;
	}

	@JsonCreator
	public JsonRequest(@JsonProperty("client_id") String client_id, @JsonProperty("client_secret") String client_secret,
			@JsonProperty("redirect_uri") String redirect_uri, @JsonProperty("code") String code,
			@JsonProperty("grant_type") String grant_type) {

		this.code = code;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.redirect_uri = redirect_uri;
		this.grant_type = grant_type;

	}
}
