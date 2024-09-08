package com.ecommerce.mazdacart.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

	@NotBlank(message = "UserName cannot be blank")
	@Size(min = 5, message = "UserName cannot be letter than 5 characters")
	private String userName;

	@NotBlank(message = "E-mail cannot be blank")
	@Size(min = 5, message = "E-mail id cannot be letter than 5 characters")
	@Email
	private String emailId;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 5, message = "Password cannot be letter than 5 characters")
	private String password;

	private Set<String> roles = new HashSet<>();


}
