package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
