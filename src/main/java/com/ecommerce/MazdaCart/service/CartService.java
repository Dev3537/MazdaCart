package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.payload.CartDTO;

import java.util.List;

public interface CartService {

	/**
	 * Adds product to the current user's cart. Cannot add duplicate products.
	 * Retrieves cart details with the quantity added
	 *
	 * @param productName
	 * @param quantity
	 * @return
	 */
	CartDTO addProductToCart (String productName, Integer quantity);

	/**
	 * Gets all the carts in the system.
	 * Retrieves all the quantities per cart.
	 *
	 * @return
	 */
	List<CartDTO> getAllCarts ();

	/**
	 * Gets the cart for the current logged-in user.
	 * Retrieves cart details with the quantity added
	 *
	 * @return
	 */
	CartDTO getCartsByUser ();

	/**
	 * Updates the quantity of the product current present in the cart.
	 * If quantity is 0, deletes the product from the cart.
	 * Retrieves cart details with the quantity in sync.
	 *
	 * @param productName
	 * @param quantity
	 * @return
	 */
	CartDTO updateProductToCart (String productName, Integer quantity);
}
