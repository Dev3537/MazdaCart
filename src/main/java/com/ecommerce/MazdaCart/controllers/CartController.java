package com.ecommerce.MazdaCart.controllers;

import com.ecommerce.MazdaCart.payload.CartDTO;
import com.ecommerce.MazdaCart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

	@Autowired
	CartService cartService;

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/public/carts/products/{productName}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart (@PathVariable String productName,
	                                                 @PathVariable Integer quantity) {

		CartDTO response = cartService.addProductToCart(productName, quantity);
		return ResponseEntity.ok().body(response);
	}


	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/admin/carts")
	public ResponseEntity<List<CartDTO>> getAllCarts () {
		List<CartDTO> allCarts = cartService.getAllCarts();
		return ResponseEntity.ok().body(allCarts);
	}


	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/admin/carts/users/cart")
	public ResponseEntity<CartDTO> getCartsByUser () {
		CartDTO response = cartService.getCartsByUser();
		return ResponseEntity.ok().body(response);
	}


}
