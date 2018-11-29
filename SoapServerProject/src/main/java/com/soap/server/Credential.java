package com.soap.server;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = Credential.CRED_NS)
public class Credential {

	public static final String CRED_NS = "http://localhost:8080/security";

	@XmlElement(namespace = CRED_NS)
	private String username;
	@XmlElement(namespace = CRED_NS)
	private String password;

	public Credential() {
	}

	public Credential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
