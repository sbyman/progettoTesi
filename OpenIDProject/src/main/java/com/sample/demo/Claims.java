package com.sample.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Claims {

	private String email;
	private String sub;
	private String gender;
	private String updatedAt;
	private String name;
	private String nickname;
	private String givenName;
	private String familyName;
	private String emailVerified;

	@JsonCreator
	public Claims(@JsonProperty("email") String email, @JsonProperty("sub") String sub,
			@JsonProperty("gender") String gender, @JsonProperty("updated_at") String updatedAt,
			@JsonProperty("name") String name, @JsonProperty("nickname") String nickname,
			@JsonProperty("given_name") String givenName, @JsonProperty("family_name") String familyName,
			@JsonProperty("email_verified") String emailVerified) {
		this.email = email;
		this.sub = sub;
		this.gender = gender;
		this.updatedAt = updatedAt;
		this.name = name;
		this.nickname = nickname;
		this.givenName = givenName;
		this.familyName = familyName;
		this.emailVerified = emailVerified;
	}

	public String getSub() {
		return sub;
	}

	public String getGender() {
		return gender;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public String getName() {
		return name;
	}

	public String getNickname() {
		return nickname;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailVerified() {
		return emailVerified;
	}
	
	@Override
	public String toString() {
		return "{\r\n" + 
				"    \"sub\": \"" + sub + "\",\r\n" + 
				"    \"gender\": \"" + gender + "\",\r\n" + 
				"    \"updated_at\": " + updatedAt + " ,\r\n" + 
				"    \"name\": \"" + name + "\",\r\n" + 
				"    \"nickname\": \"" + nickname + "\",\r\n" + 
				"    \"given_name\": \"" + givenName + "\",\r\n" + 
				"    \"family_name\": \"" + familyName + "\",\r\n" + 
				"    \"email_verified\": " + emailVerified + ",\r\n" + 
				"    \"email\": \"" + email + "\"\r\n" + 
				"}";
	}

}
