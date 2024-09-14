package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.exceptions.BadCredsException;
import com.ecommerce.mazdacart.payload.UserSignUpRequest;
import com.ecommerce.mazdacart.payload.UserSignInRequest;
import com.ecommerce.mazdacart.payload.UserSignInResponse;
import com.ecommerce.mazdacart.payload.UserSignUpResponse;
import com.ecommerce.mazdacart.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
				"The User: '" + loginRequest.getUserName() + "' failed to authenticate | User does not exist in system");
		}

		UserSignInResponse response = authService.signInUser(loginRequest, authentication);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	@PostMapping("/sign_up")
	public ResponseEntity<UserSignUpResponse> registerUser (@Valid @RequestBody UserSignUpRequest signUpRequest) {

		UserSignUpResponse response = authService.registerUser(signUpRequest);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	@GetMapping("/getUserDetails")
	public ResponseEntity<UserSignInResponse> getUserDetails (Authentication authentication, HttpServletRequest httpServletRequest) {
		UserSignInResponse response;
		if (authentication != null) {
			response = authService.getUserDetails(authentication, httpServletRequest);
		} else {
			throw new BadCredsException("Authentication header not found");
		}
		return ResponseEntity.ok().body(response);
	}

}
