package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
