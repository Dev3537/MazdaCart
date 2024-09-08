package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Users;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUserNameIgnoreCase (String username);

	Optional<Users> findByEmailId (@Email String emailId);
}
