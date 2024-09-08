package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.payload.UserSignInRequest;
import com.ecommerce.mazdacart.payload.UserSignInResponse;
import com.ecommerce.mazdacart.payload.UserSignUpRequest;
import com.ecommerce.mazdacart.payload.UserSignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

public interface AuthService {

	/**
	 * Signing in user and retrieving the jwt token
	 *
	 * @param loginRequest
	 * @param authentication
	 * @return
	 */
	UserSignInResponse signInUser (UserSignInRequest loginRequest, Authentication authentication);

	/**
	 * Register a new Individual
	 *
	 * @param signUpRequest
	 * @return
	 */
	UserSignUpResponse registerUser (@Valid UserSignUpRequest signUpRequest);

	/**
	 * Gets the details of the current user logged in
	 *
	 * @param authentication
	 * @param httpServletRequest
	 * @return
	 */
	UserSignInResponse getUserDetails (Authentication authentication, HttpServletRequest httpServletRequest);
}
