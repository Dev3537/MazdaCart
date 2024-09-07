package com.ecommerce.MazdaCart.util;

import com.ecommerce.MazdaCart.exceptions.ResourceNotFoundException;
import com.ecommerce.MazdaCart.model.Users;
import com.ecommerce.MazdaCart.repository.UserRepository;
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
		String userName = authentication.getName();
		return userRepository.findByUserNameIgnoreCase(userName).orElseThrow(
			() -> new ResourceNotFoundException("User not found in DB while accessing authenticated user details"));
	}
}
