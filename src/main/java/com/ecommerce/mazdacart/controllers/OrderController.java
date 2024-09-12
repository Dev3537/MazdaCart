package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.payload.OrderRequestDTO;
import com.ecommerce.mazdacart.payload.OrdersDTO;
import com.ecommerce.mazdacart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("/public/order")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<OrdersDTO> placeOrder (
	                                             @RequestBody OrderRequestDTO ordersDTO) {
		OrdersDTO responseDTO = orderService.placeOrder(ordersDTO);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

}
