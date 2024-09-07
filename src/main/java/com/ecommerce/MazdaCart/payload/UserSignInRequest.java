package com.ecommerce.MazdaCart.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSignInRequest {

	private String userName;

	private String password;

	public UserSignInRequest () {
	}

	public UserSignInRequest (String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
}
