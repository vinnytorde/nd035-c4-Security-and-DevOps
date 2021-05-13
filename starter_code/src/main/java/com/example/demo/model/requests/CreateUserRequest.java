package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	@Getter
	@Setter
	private String password;

	@JsonProperty
	@Getter
	@Setter
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
