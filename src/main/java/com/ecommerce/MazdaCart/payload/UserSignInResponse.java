package com.ecommerce.MazdaCart.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserSignInResponse {


	private String jwtToken;

	private String userName;

	private List<String> roles;

	public UserSignInResponse () {
	}

	public UserSignInResponse (String jwtToken, String userName, List<String> roles) {
		this.jwtToken = jwtToken;
		this.userName = userName;
		this.roles = roles;
	}
}
