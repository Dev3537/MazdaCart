package com.ecommerce.mazdacart.util;

import com.ecommerce.mazdacart.exceptions.BadCredsException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtilHelperClass {

	@Autowired
	UserRepository userRepository;

	public Users getCurrentUser () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new BadCredsException("No user is signed in");
		}
		String userName = authentication.getName();
		return userRepository.findByUserNameIgnoreCase(userName).orElseThrow(
			() -> new ResourceNotFoundException("User not found in DB while accessing authenticated user details"));
	}

	public String getCurrentUserEmail () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users users = userRepository.findByUserNameIgnoreCase(authentication.getName()).orElseThrow(
			() -> new ResourceNotFoundException("User not found in DB while accessing authenticated user details"));
		return users.getEmailId();

	}
}
