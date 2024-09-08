package com.ecommerce.mazdacart.configurations;

import com.ecommerce.mazdacart.model.AppRole;
import com.ecommerce.mazdacart.model.Roles;
import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.repository.RoleRepository;
import com.ecommerce.mazdacart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DefaultConfig {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;


	/**
	 * To add some default user profiles for testing
	 *
	 * @return
	 */
	@Bean
	@Transactional
	public CommandLineRunner initData () {
		return args -> {
			Roles adminRole = roleRepository.getByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> {
				Roles newAdminRole = new Roles(AppRole.ROLE_ADMIN);
				return roleRepository.save(newAdminRole);
			});

			Roles sellerRole = roleRepository.getByRoleName(AppRole.ROLE_SELLER).orElseGet(() -> {
				Roles newSellerRole = new Roles(AppRole.ROLE_SELLER);
				return roleRepository.save(newSellerRole);
			});

			Roles userRole = roleRepository.getByRoleName(AppRole.ROLE_USER).orElseGet(() -> {
				Roles newUserRole = new Roles(AppRole.ROLE_USER);
				return roleRepository.save(newUserRole);
			});

			Set<Roles> userRoleSet = new HashSet<>(List.of(userRole));
			Set<Roles> sellerRoleSet = new HashSet<>(Arrays.asList(sellerRole, userRole));
			Set<Roles> adminRoleSet = new HashSet<>(Arrays.asList(userRole, sellerRole, adminRole));

			Users user1 = new Users("user1", "user1@mazdacart.com", passwordEncoder.encode("password1"), userRoleSet);
			Users seller1 =
				new Users("seller1", "seller1@mazdacart.com", passwordEncoder.encode("password1"), sellerRoleSet);
			Users admin1 =
				new Users("admin1", "admin1@mazdacart.com", passwordEncoder.encode("password1"), adminRoleSet);

			userRepository.save(user1);
			userRepository.save(seller1);
			userRepository.save(admin1);


		};
	}
}
