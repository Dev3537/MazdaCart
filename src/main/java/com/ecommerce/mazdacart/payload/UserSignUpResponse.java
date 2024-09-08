package com.ecommerce.mazdacart.payload;

import com.ecommerce.mazdacart.model.Roles;
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
public class UserSignUpResponse {

	private Long userId;

	private String userName;

	private String emailId;

	private Set<Roles> roles = new HashSet<>();

}