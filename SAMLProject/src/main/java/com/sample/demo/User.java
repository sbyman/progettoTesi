package com.sample.demo;


public class User {

	private String id;
	private String user;
	private String email;
	private String samlNameId;
	private String samlEmail;
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSamlNameId() {
		return samlNameId;
	}
	
	public void setSamlNameId(String samlNameId) {
		this.samlNameId = samlNameId;
	}
	
	public String getSamlEmail() {
		return samlEmail;
	}
	
	public void setSamlEmail(String samlEmail) {
		this.samlEmail = samlEmail;
	}

}
