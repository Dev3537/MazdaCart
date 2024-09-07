package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.payload.UserSignInRequest;
import com.ecommerce.MazdaCart.payload.UserSignInResponse;
import com.ecommerce.MazdaCart.payload.UserSignUpRequest;
import com.ecommerce.MazdaCart.payload.UserSignUpResponse;
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
	 * @return
	 */
	UserSignInResponse getUserDetails (Authentication authentication);
}
