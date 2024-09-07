package com.ecommerce.MazdaCart.repository;

import com.ecommerce.MazdaCart.model.AppRole;
import com.ecommerce.MazdaCart.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> getByRoleName (AppRole appRole);
}
