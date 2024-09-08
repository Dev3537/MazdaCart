package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.payload.CartDTO;
import com.ecommerce.mazdacart.service.CartService;
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


	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/carts/users/cart")
	public ResponseEntity<CartDTO> getCartsByUser () {
		CartDTO response = cartService.getCartsByUser();
		return ResponseEntity.ok().body(response);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PutMapping("/public/carts/products/{productName}/quantity/{operation}/update")
	public ResponseEntity<CartDTO> updateProductToCart (@PathVariable String productName,
	                                                    @PathVariable String operation) {
		int quantity;

		if (operation.equalsIgnoreCase("decrement")) {
			quantity = -1;
		} else if (operation.equalsIgnoreCase("increment")) {
			quantity = 1;
		} else {
			throw new APIException("Update operation not recognised:" + operation);
		}

		CartDTO response = cartService.updateProductToCart(productName, quantity);
		return ResponseEntity.ok().body(response);
	}


	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@DeleteMapping("/public/carts/{productName}/delete")
	public ResponseEntity<CartDTO> deleteProductFromCart (@PathVariable String productName) {
		CartDTO response = cartService.deleteProductFromCart(productName);
		return ResponseEntity.ok().body(response);
	}
}
