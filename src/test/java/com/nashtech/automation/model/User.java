package com.nashtech.automation.model;

public class User {
	public String token;
	public String id;
	public String username;
	public String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User() {}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
