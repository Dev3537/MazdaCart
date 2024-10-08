package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.CartItem;
import com.ecommerce.mazdacart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query("select c from CartItem c where c.product.productId=?1 and c.cart.cartId=?2")
	Optional<CartItem> findByProductIdAndCartId (Long productId, long cartId);

	CartItem findByProduct (Product product);
}
