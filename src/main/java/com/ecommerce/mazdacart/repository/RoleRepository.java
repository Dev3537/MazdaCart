package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.AppRole;
import com.ecommerce.mazdacart.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> getByRoleName (AppRole appRole);
}
