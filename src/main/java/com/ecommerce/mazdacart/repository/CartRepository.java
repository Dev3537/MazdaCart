package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Cart;
import com.ecommerce.mazdacart.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUsers (Users user);

	@Query(nativeQuery = true, value =
		                           "select c.* from cart c left join cart_item ci on c.cart_id=ci.cart_id  left join " +
			                           "product p on p" +
			                           ".product_id=ci.product_id where p.product_id=:productId")
	List<Cart> findCartsByProductId (Long productId);


	@Query(nativeQuery = true, value =
		                           "select c.* from cart c , users u where c.user_id=u.user_id  and u.email_id=:emailId")
	Optional<Cart> findCartByEmailId (String emailId);
}
