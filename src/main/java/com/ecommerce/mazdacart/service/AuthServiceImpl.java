package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.AppRole;
import com.ecommerce.mazdacart.model.Roles;
import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.payload.UserSignInRequest;
import com.ecommerce.mazdacart.payload.UserSignInResponse;
import com.ecommerce.mazdacart.payload.UserSignUpRequest;
import com.ecommerce.mazdacart.payload.UserSignUpResponse;
import com.ecommerce.mazdacart.repository.RoleRepository;
import com.ecommerce.mazdacart.repository.UserRepository;
import com.ecommerce.mazdacart.security.jwt.JwtUtils;
import com.ecommerce.mazdacart.security.servicesofjwt.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {


	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ModelMapper modelMapper;


	@Override
	public UserSignInResponse signInUser (UserSignInRequest loginRequest, Authentication authentication) {

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.getJwtForUserName(userDetails);

		List<String> authorities =
			authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		return new UserSignInResponse(jwt, loginRequest.getUserName(), authorities);
	}


	@Override
	public UserSignUpResponse registerUser (UserSignUpRequest signUpRequest) {

		if (userRepository.findByUserNameIgnoreCase(signUpRequest.getUserName()).isPresent()) {
			throw new APIException("User Name Already exists");
		}

		if (userRepository.findByEmailId(signUpRequest.getEmailId()).isPresent()) {
			throw new APIException("Email Id Already exists");
		}

		Users newUser = new Users();
		newUser.setUserName(signUpRequest.getUserName());
		newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		newUser.setEmailId(signUpRequest.getEmailId());

		Set<String> userRoles = signUpRequest.getRoles();

		Set<Roles> rolesPermitted = new HashSet<>();

		if (userRoles.isEmpty()) {
			Roles role = roleRepository.getByRoleName(AppRole.ROLE_USER)
				             .orElseThrow(() -> new ResourceNotFoundException("Role User is not found in database"));
			rolesPermitted.add(role);
		} else {
			userRoles.forEach(roles -> {
				switch (roles) {
					case "admin":
						List<Roles> allRoles = roleRepository.findAll();
						if (allRoles.isEmpty()) {
							throw new ResourceNotFoundException("No Roles found in DB");
						}
						rolesPermitted.addAll(allRoles.stream().collect(Collectors.toSet()));
						break;

					case "seller":
						Roles sellerRole = roleRepository.getByRoleName(AppRole.ROLE_SELLER).orElseThrow(
							() -> new ResourceNotFoundException("Role Seller is not found in database"));
						rolesPermitted.add(sellerRole);
						break;

					default:
						Roles userRole = roleRepository.getByRoleName(AppRole.ROLE_USER).orElseThrow(
							() -> new ResourceNotFoundException("Role User is not found in database"));
						rolesPermitted.add(userRole);
						break;

				}
			});


		}


		newUser.setRoles(rolesPermitted);

		Users savedUser = userRepository.save(newUser);

		return modelMapper.map(savedUser, UserSignUpResponse.class);
	}


	@Override
	public UserSignInResponse getUserDetails (Authentication authentication, HttpServletRequest httpServletRequest) {

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		UserSignInResponse response = modelMapper.map(userDetails, UserSignInResponse.class);

		String jwt = jwtUtils.getJwtFromHeader(httpServletRequest);

		response.setJwtToken(jwt);

		response.setRoles(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

		return response;

	}
}
