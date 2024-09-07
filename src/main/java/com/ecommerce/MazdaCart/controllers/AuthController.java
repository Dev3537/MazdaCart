package com.ecommerce.MazdaCart.controllers;

import com.ecommerce.MazdaCart.exceptions.BadCredsException;
import com.ecommerce.MazdaCart.payload.UserSignUpRequest;
import com.ecommerce.MazdaCart.payload.UserSignInRequest;
import com.ecommerce.MazdaCart.payload.UserSignInResponse;
import com.ecommerce.MazdaCart.payload.UserSignUpResponse;
import com.ecommerce.MazdaCart.security.jwt.JwtUtils;
import com.ecommerce.MazdaCart.service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthService authService;


	@PostMapping("/sign_in")
	public ResponseEntity<UserSignInResponse> authenticateUser (@RequestBody UserSignInRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		} catch (AuthenticationException e) {
			throw new BadCredsException(
				"The User ~ " + loginRequest.getUserName() + " ~failed to authenticate the credentials");
		}

		UserSignInResponse response = authService.signInUser(loginRequest, authentication);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	@PostMapping("/sign_up")
	public ResponseEntity<UserSignUpResponse> registerUser (@Valid @RequestBody UserSignUpRequest signUpRequest) {

		UserSignUpResponse response = authService.registerUser(signUpRequest);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@GetMapping("/getUserDetails")
	public ResponseEntity<UserSignInResponse> getUserDetails (Authentication authentication) {
		UserSignInResponse response;
		if (authentication != null) {
			response = authService.getUserDetails(authentication);
		} else {
			throw new BadCredsException("Authentication not found");
		}
		return ResponseEntity.ok().body(response);
	}

}
