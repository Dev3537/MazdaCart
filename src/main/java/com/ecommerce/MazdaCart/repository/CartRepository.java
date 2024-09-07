package com.ecommerce.MazdaCart.repository;

import com.ecommerce.MazdaCart.model.Cart;
import com.ecommerce.MazdaCart.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUsers (Users user);
}
