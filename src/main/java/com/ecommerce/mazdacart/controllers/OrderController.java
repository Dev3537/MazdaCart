package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.payload.OrdersDTO;
import com.ecommerce.mazdacart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("/public/order/method/{paymentMethod}/address/{addressId}")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<OrdersDTO> placeOrder (@PathVariable Long addressId, @PathVariable String paymentMethod) {
		OrdersDTO responseDTO = orderService.placeOrder(paymentMethod, addressId);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

}
