package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
